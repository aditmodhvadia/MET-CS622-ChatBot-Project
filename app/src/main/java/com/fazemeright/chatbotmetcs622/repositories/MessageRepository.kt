package com.fazemeright.chatbotmetcs622.repositories

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import com.fazemeright.chatbotmetcs622.database.ChatBotDatabase
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.database.message.Message.Companion.fromMap
import com.fazemeright.chatbotmetcs622.database.message.Message.Companion.newMessage
import com.fazemeright.chatbotmetcs622.intentservice.FireBaseIntentService
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.chatbotmetcs622.network.ApiManager
import com.fazemeright.chatbotmetcs622.network.NetworkManager
import com.fazemeright.chatbotmetcs622.network.handlers.NetworkCallback
import com.fazemeright.chatbotmetcs622.network.models.NetError
import com.fazemeright.chatbotmetcs622.network.models.NetResponse
import com.fazemeright.chatbotmetcs622.network.models.response.QueryResponseMessage
import com.fazemeright.library.api.DatabaseStore
import com.fazemeright.library.api.Storable
import com.fazemeright.library.api.UserAuthResult
import com.fazemeright.library.api.UserAuthentication
import com.fazemeright.library.api.firebase.FireBaseDatabaseStore
import com.fazemeright.library.api.firebase.FireBaseUserAuthentication
import com.fazemeright.library.api.result.Result
import com.fazemeright.library.api.result.TaskResult
import com.fazemeright.library.listeners.OnTaskCompleteListener
import timber.log.Timber
import java.util.*

class MessageRepository private constructor(
        private val database: ChatBotDatabase, private val apiManager: ApiManager,
        private val userAuthentication: UserAuthentication, private val onlineDatabaseStore: DatabaseStore) {
    /**
     * Call to insert given message into database with thread safety.
     *
     * @param newMessage given project
     * @param listener   task completion listener
     */
    private fun insertMessageInRoom(newMessage: Message,
                                    listener: OnTaskCompleteListener<Message>? = null) {
        Timber.i("Insert message in Room called%s", newMessage.msg)
        database.messageDao().insert(newMessage)
        val insertedMsg = database.messageDao().getLatestMessage(newMessage.chatRoomId)
        listener?.onComplete(Result.withData(insertedMsg))
    }

    /**
     * Register new user and store the details.
     *
     * @param userEmail              email
     * @param password               password
     * @param firstName              first name
     * @param lastName               last name
     * @param onTaskCompleteListener task completion listener
     */
    fun createNewUserAndStoreDetails(userEmail: String,
                                     password: String,
                                     firstName: String,
                                     lastName: String,
                                     onTaskCompleteListener: OnTaskCompleteListener<Void>? = null) {
        userAuthentication.createNewUserWithEmailPassword(userEmail, password) { result: TaskResult<UserAuthResult> ->
            if (result.isSuccessful) {
                userAuthentication.currentUserUid?.let {
                    onlineDatabaseStore.storeUserData(
                            it,
                            getStorableFromUserDetails(userEmail, firstName, lastName))
                }
                onTaskCompleteListener?.onComplete(Result.nullResult())
            } else {
                onTaskCompleteListener?.onComplete(Result.exception(result.exception))
            }
        }
    }

    private fun getStorableFromUserDetails(userEmail: String, firstName: String,
                                           lastName: String): Storable {
        return object : Storable {
            override fun getHashMap(): Map<String, Any> {
                return mapOf(
                        "emailAddress" to userEmail,
                        "firstName" to firstName,
                        "lastName" to lastName
                )
            }

            override fun getId(): Long {
                return 0
            }
        }
    }

    /**
     * Call to update given project into database with thread safety.
     *
     * @param oldMessage given project
     */
    private fun updateMessage(oldMessage: Message) {
        //        insert into Room using AsyncTask
        database.messageDao().update(oldMessage)
    }

    /**
     * Call to get Message with given Message ID.
     *
     * @param messageId given Message ID
     * @return Message with given ID
     */
    fun getMessage(messageId: Long): Message {
        throw UnsupportedOperationException("Not implemented yet")
    }

    /**
     * Call to delete message with given message.
     *
     * @param message given Message
     */
    fun deleteMessage(message: Message) {
        runOnThread { database.messageDao().deleteItem(message) }
    }

    /**
     * Get messages from the given chat room.
     *
     * @param chatRoom chat room
     * @return List of Messages
     */
    fun getMessagesForChatRoom(chatRoom: ChatRoom): LiveData<List<Message>> {
        return getChatRoomMessagesFromDatabase(chatRoom)
    }

    private fun getChatRoomMessagesFromDatabase(chatRoom: ChatRoom): LiveData<List<Message>> {
        return database.messageDao().getAllMessagesFromChatRoomLive(chatRoom.id)
    }

    /**
     * Called when user sends a given new message in the ChatRoom - Add new Message to Room - Call API
     * to fetch answer for new message - Sync message with FireStore.
     *
     * @param newMessage given new message
     */
    fun newMessageSent(
            context: Context,
            newMessage: Message,
            listener: OnTaskCompleteListener<Message>? = null) {
        insertMessageInRoom(newMessage) { result: TaskResult<Message> ->
            val roomLastMessage = result.data
            insertMessageInFireBase(context, roomLastMessage)
            apiManager.queryDatabase(
                    context,
                    newMessage,
                    object : NetworkCallback<QueryResponseMessage> {
                        override fun onSuccess(response: NetResponse<QueryResponseMessage>?) {
                            val queryResponseMessage = newMessage(
                                    response!!.response!!.data!!.responseMsg!!,
                                    newMessage.receiver,
                                    newMessage.sender,
                                    newMessage.chatRoomId)
                            insertMessageInRoom(queryResponseMessage) { result: TaskResult<Message> ->
                                val roomLastInsertedMessage = result.data
                                insertMessageInFireBase(context, roomLastInsertedMessage)
                                listener?.onComplete(Result.withData(queryResponseMessage))
                            }
                        }

                        override fun onError(error: NetError?) {
                            listener?.onComplete(Result.exception(error))
                        }
                    })
        }
    }

    /**
     * Call to insert the given new message to FireStore database.
     *
     * @param context    context
     * @param newMessage given new message
     */
    private fun insertMessageInFireBase(context: Context, newMessage: Message) {
        val intent = Intent(context, FireBaseIntentService::class.java)
        intent.putExtra(FireBaseIntentService.ACTION_INTENT,
                FireBaseIntentService.Actions.ACTION_ADD_MESSAGE)
        intent.putExtra(FireBaseIntentService.MESSAGE, newMessage)
        context.startService(intent)
    }

    /**
     * Get all messages stored inside local database.
     *
     * @return `List` of  messages
     */
    val allMessages: ArrayList<Message>
        get() = database.messageDao().allMessages as ArrayList<Message>

    /**
     * Clear all given chat room messages - From Room - From FireStore.
     *
     * @param chatRoom given chat room
     */
    fun clearAllChatRoomMessages(chatRoom: ChatRoom) {
        database.messageDao().clearChatRoomMessages(chatRoom.id)
    }

    /**
     * Call to logout user and clear all messages from Room.
     */
    fun logOutUser() {
        userAuthentication.signOutUser()
        clearAllMessages()
    }

    /**
     * Call to clear all messages from Room.
     */
    private fun clearAllMessages() {
        database.messageDao().clear()
    }

    /**
     * Add given list of messages to Room.
     */
    private fun addMessagesToLocal(messages: List<Message>) {
        runOnThread { database.messageDao().insertAllMessages(messages) }
    }

    private fun runOnThread(runnable: Runnable?) {
        Thread(runnable).start()
    }

    /**
     * Call to add the given message to FireStore.
     *
     * @param message given message
     */
    fun addMessageToFireBase(message: Message) {
        onlineDatabaseStore
                .storeMessage(message, userAuthentication.currentUserUid)
    }

    /**
     * Call to add the given messages to FireStore.
     *
     * @param messageList given messages list
     */
    fun addMessagesToFireBase(messageList: List<Message>) {
        for (message in messageList) {
            addMessageToFireBase(message)
        }
    }

    /**
     * Call to sync messages from FireStore to Room for the logged in user.
     */
    fun syncMessagesFromFireStoreToRoom() {
        onlineDatabaseStore.getAllMessagesForUser(userAuthentication.currentUserUid!!) { result: TaskResult<List<Map<String, Any>>> ->
            if (result.isSuccessful) {
                addMessagesToLocal(result.data.map(Message::fromMap))
            }
        }
    }

    /**
     * Get user name of the current user.
     *
     * @return user name
     */
    val userName: String?
        get() = userAuthentication.userName

    /**
     * Sign in user with email and password.
     *
     * @param email    email
     * @param password password
     * @param listener task completion listener`
     */
    fun signInWithEmailAndPassword(email: String, password: String,
                                   listener: OnTaskCompleteListener<UserAuthResult>?) {
        userAuthentication.signInWithEmailAndPassword(email, password, listener)
    }

    /**
     * Reload the current user authentication state.
     *
     * @param listener listener for updates
     */
    fun reloadCurrentUserAuthState(
            listener: OnTaskCompleteListener<Void>?) {
        userAuthentication.reloadCurrentUserAuthState(listener)
    }

    companion object {
        private var repository: MessageRepository? = null

        /**
         * Call to get instance of MessageRepository with the given context.
         *
         * @param context given context
         * @return synchronized call to get Instance of MessageRepository class
         */
        fun getInstance(context: Context): MessageRepository {
            if (repository == null) {
                synchronized(MessageRepository::class.java) {
                    val database = ChatBotDatabase.getInstance(context)
                    val apiManager = ApiManager.instance
                    val userAuthentication: UserAuthentication = FireBaseUserAuthentication.getInstance()
                    val databaseStore: DatabaseStore = FireBaseDatabaseStore.getInstance()
                    apiManager.init(NetworkManager.instance)
                    repository = MessageRepository(database, apiManager, userAuthentication,
                            databaseStore)
                }
            }
            return repository!!
        }
    }
}