package com.fazemeright.chatbotmetcs622.repositories

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import com.fazemeright.chatbotmetcs622.database.ChatBotDatabase
import com.fazemeright.chatbotmetcs622.database.message.Message
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
import com.fazemeright.library.api.UserAuthentication
import com.fazemeright.library.api.firebase.FireBaseDatabaseStore
import com.fazemeright.library.api.firebase.FireBaseUserAuthentication
import com.fazemeright.library.api.result.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.*

class MessageRepository private constructor(
        private val database: ChatBotDatabase, private val apiManager: ApiManager,
        private val userAuthentication: UserAuthentication, private val onlineDatabaseStore: DatabaseStore) : Repository {
    /**
     * Call to insert given message into database with thread safety.
     *
     * @param newMessage given message
     */
    private suspend fun insertMessageInRoom(newMessage: Message): Result<Message> {
        return withContext(Dispatchers.IO) {
            Timber.i("Insert message in Room called%s", newMessage.msg)
            database.messageDao().insert(newMessage)
            database.messageDao().getLatestMessage(newMessage.chatRoomId).let {
                return@withContext Result.Success(it)
            }
        }
    }

    /**
     * Register new user and store the details.
     *
     * @param userEmail              email
     * @param password               password
     * @param firstName              first name
     * @param lastName               last name
     */
    override suspend fun createNewUserAndStoreDetails(userEmail: String,
                                                      password: String,
                                                      firstName: String,
                                                      lastName: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                userAuthentication.createNewUserWithEmailPassword(
                        userEmail,
                        password).await()
                userAuthentication.currentUserUid?.let {
                    onlineDatabaseStore.storeUserData(
                            it,
                            getStorableFromUserDetails(userEmail, firstName, lastName))
                } ?: throw UnsupportedOperationException("User not logged in")
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    private fun getStorableFromUserDetails(userEmail: String, firstName: String,
                                           lastName: String): Storable {
        return object : Storable {
            override val hashMap: Map<String, Any>
                get() = mapOf(
                        "emailAddress" to userEmail,
                        "firstName" to firstName,
                        "lastName" to lastName
                )

            override val id: Long = 0
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
    override fun getMessage(messageId: Long): Message {
        throw UnsupportedOperationException("Not implemented yet")
    }

    /**
     * Call to delete message with given message.
     *
     * @param message given Message
     */
    override fun deleteMessage(message: Message) {
        runOnThread { database.messageDao().deleteItem(message) }
    }

    /**
     * Get messages from the given chat room.
     *
     * @param chatRoom chat room
     * @return List of Messages
     */
    override fun getMessagesForChatRoom(chatRoom: ChatRoom): LiveData<List<Message>> {
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
    override suspend fun newMessageSent(
            context: Context,
            newMessage: Message): Result<Message> {
        return withContext(Dispatchers.IO) {
            val result =
                    insertMessageInRoom(newMessage)
//            insertMessageInFireBase(context, result)

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
//                            val newMessageResult = insertMessageInRoom(queryResponseMessage)
//                            insertMessageInFireBase(context, newMessageResult.data)
                            Result.Success(queryResponseMessage)
                        }

                        override fun onError(error: NetError?) {
                            Result.Error(error)
                        }
                    })
            Result.Error()
        }
    }

    /**
     * Call to insert the given new message to FireStore database.
     *
     * @param context    context
     * @param newMessage given new message
     */
    private fun insertMessageInFireBase(context: Context, newMessage: Message) {
        Intent(context, FireBaseIntentService::class.java).apply {
            putExtra(FireBaseIntentService.ACTION_INTENT,
                    FireBaseIntentService.Actions.ACTION_ADD_MESSAGE)
            putExtra(FireBaseIntentService.MESSAGE, newMessage)
        }.also {
            context.startService(it)
        }
    }

    /**
     * Get all messages stored inside local database.
     *
     * @return `List` of  messages
     */
    val allMessagesInLocal: ArrayList<Message>
        get() = database.messageDao().allMessages as ArrayList<Message>

    /**
     * Clear all given chat room messages - From Room - From FireStore.
     *
     * @param chatRoom given chat room
     */
    override fun clearAllChatRoomMessages(chatRoom: ChatRoom) {
        database.messageDao().clearChatRoomMessages(chatRoom.id)
    }

    /**
     * Call to logout user and clear all messages from Room.
     */
    override fun logOutUser() {
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
    override suspend fun addMessageToFireBase(message: Message): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                onlineDatabaseStore
                        .storeMessage(message, userAuthentication.currentUserUid
                                ?: throw UnsupportedOperationException("User not logged in"))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    /**
     * Call to add the given messages to FireStore.
     *
     * @param messageList given messages list
     */
    override suspend fun addMessagesToFireBase(messageList: List<Message>): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            val results = mutableListOf<Deferred<Result<Boolean>>>()
            messageList.forEach {
                results.add(async { addMessageToFireBase(it) })
            }
            var result: Result<Boolean> = Result.Success(true)
            for (tempResult in results.awaitAll()) {
                result = when (tempResult) {
                    is Result.Success -> Result.Success(tempResult.data)
                    is Result.Error -> {
                        Result.Error(tempResult.exception)
                        break
                    }
                }
            }
            result
        }
    }

    /**
     * Call to sync messages from FireStore to Room for the logged in user.
     */
    override suspend fun syncMessagesFromFireStoreToRoom() {
        onlineDatabaseStore.getAllMessagesForUser(userAuthentication.currentUserUid!!)
    }

    /**
     * Get user name of the current user.
     *
     * @return user name
     */
    override val userName: String?
        get() = userAuthentication.userName

    /**
     * Sign in user with email and password.
     *
     * @param email    email
     * @param password password
     * @param listener task completion listener`
     */
    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                userAuthentication.signInWithEmailAndPassword(email, password).await().let {
                    Result.Success(true)
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    /**
     * Reload the current user authentication state.
     *
     */
    override suspend fun reloadCurrentUserAuthState(): Result<Boolean> {
        return try {
            userAuthentication.reloadCurrentUserAuthState()?.await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
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
                    val userAuthentication: UserAuthentication = FireBaseUserAuthentication.instance
                    val databaseStore: DatabaseStore = FireBaseDatabaseStore.instance
                    apiManager.init(NetworkManager.instance)
                    repository = MessageRepository(database, apiManager, userAuthentication,
                            databaseStore)
                }
            }
            return repository!!
        }
    }
}