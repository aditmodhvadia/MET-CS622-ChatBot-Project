package com.fazemeright.chatbotmetcs622.repositories.message

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.network_library.retrofit.RetrofitApiManager
import com.fazemeright.chatbotmetcs622.database.ChatBotDatabase
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.database.message.Message.Companion.newMessage
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.library.api.Storable
import com.fazemeright.library.api.domain.authentication.firebase.FireBaseUserAuthentication.currentUserUid
import com.fazemeright.library.api.domain.database.DatabaseStore
import com.fazemeright.library.api.domain.database.firebase.FireBaseDatabaseStore
import com.fazemeright.library.api.result.Result
import com.fazemeright.library.api.result.safeApiCall
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import timber.log.Timber
import java.util.*

class MessageRepositoryImpl private constructor(
        private val database: ChatBotDatabase,
        private val apiManager: RetrofitApiManager,
        private val onlineDatabaseStore: DatabaseStore
) : MessageRepository {
    /**
     * Call to insert given message into database with thread safety.
     *
     * @param newMessage given message
     */
    private suspend fun insertMessageInRoom(newMessage: Message): Result<Message> {
        return safeApiCall {
            Timber.i("Insert message in Room called%s", newMessage.msg)
            database.messageDao().insert(newMessage)
            database.messageDao().getLatestMessage(newMessage.chatRoomId).let {
                Result.Success(it)
            }
        }
    }

    /**
     * Call to update given project into database with thread safety.
     *
     * @param oldMessage given project
     */
    private suspend fun updateMessage(oldMessage: Message) {
        //        insert into Room using AsyncTask
        safeApiCall {
            database.messageDao().update(oldMessage)
            Result.Success(true)
        }
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
    override suspend fun deleteMessage(message: Message) {
        safeApiCall {
            database.messageDao().deleteItem(message)
            Result.Success(true)
        }
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
        return safeApiCall {
            when (val msgInsertResult = insertMessageInRoom(newMessage)) {
                is Result.Success -> async { insertMessageInFireBase(msgInsertResult.data) }
                is Result.Error -> TODO()
            }

            apiManager.queryDatabase(newMessage.msg, getServerEndPoint(newMessage.chatRoomId.toInt())).let {
                val queryResponseMessage = newMessage(
                        it.data?.responseMsg ?: "Some error occurred",
                        newMessage.receiver,
                        newMessage.sender,
                        newMessage.chatRoomId)
                when (val newMessageResult = insertMessageInRoom(queryResponseMessage)) {
                    is Result.Success -> async { insertMessageInFireBase(newMessageResult.data) }
                    is Result.Error -> TODO()
                }
                Result.Success(queryResponseMessage)
            }
        }
    }

    /**
     * Call to insert the given new message to FireStore database.
     *
     * @param newMessage given new message
     */
    private suspend fun insertMessageInFireBase(newMessage: Message) {
        safeApiCall {
            addMessageToFireBase(newMessage)
        }
    }

    /**
     * Get all messages stored inside local database.
     *
     * @return `List` of  messages
     */
    private val allMessagesInLocal: ArrayList<Message>
        get() = database.messageDao().allMessages as ArrayList<Message>

    /**
     * Clear all given chat room messages - From Room - From FireStore.
     *
     * @param chatRoom given chat room
     */
    override suspend fun clearAllChatRoomMessages(chatRoom: ChatRoom) {
        safeApiCall {
            database.messageDao().clearChatRoomMessages(chatRoom.id)
            Result.Success(true)
        }
    }

    /**
     * Call to clear all messages from Room.
     */
    override suspend fun clearAllMessages() {
        safeApiCall {
            database.messageDao().clear()
            Result.Success(true)
        }
    }

    /**
     * Add given list of messages to Room.
     */
    private suspend fun addMessagesToLocal(messages: List<Message>) {
        safeApiCall {
            database.messageDao().insertAllMessages(messages)
            Result.Success(true)
        }
    }

    /**
     * Call to add the given message to FireStore.
     *
     * @param message given message
     */
    override suspend fun addMessageToFireBase(message: Message): Result<Boolean> {
        return safeApiCall {
            onlineDatabaseStore
                    .storeMessage(message, currentUserUid
                            ?: throw UnsupportedOperationException("User not logged in"))
        }
    }

    /**
     * Call to add the given messages to FireStore.
     *
     * @param messageList given messages list
     */
    override suspend fun addMessagesToFireBase(messageList: List<Message>): Result<Boolean> {
        return safeApiCall {
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
        safeApiCall {
            currentUserUid?.let {
                onlineDatabaseStore.getAllMessagesForUser(it)
                Result.Success(true)
            } ?: throw UnsupportedOperationException("User not logged in")
        }
    }

    override suspend fun syncMessagesWithCloudAndLocal() {
        safeApiCall {
            addMessagesToFireBase(allMessagesInLocal)
            syncMessagesFromFireStoreToRoom()
            Result.Success(true)
        }
    }

    private fun getServerEndPoint(chatRoomId: Int): String {
        return when (chatRoomId) {
            ChatRoom.BRUTE_FORCE_ID -> DatabaseUrl.BRUTE_FORCE
            ChatRoom.LUCENE_ID -> DatabaseUrl.LUCENE
            ChatRoom.MONGO_DB_ID -> DatabaseUrl.MONGO_DB
            else -> DatabaseUrl.MY_SQL
        }
    }

    /**
     * DatabaseUrl module Api sub url.
     */
    internal object DatabaseUrl {
        const val MONGO_DB = "/mongodb"
        const val LUCENE = "/lucene"
        const val MY_SQL = "/mysql"
        const val BRUTE_FORCE = "/bruteforce"
    }

    companion object {
        private var repository: MessageRepositoryImpl? = null

        /**
         * Call to get instance of MessageRepository with the given context.
         *
         * @param context given context
         * @return synchronized call to get Instance of MessageRepository class
         */
        fun getInstance(context: Context): MessageRepositoryImpl {
            if (repository == null) {
                synchronized(MessageRepositoryImpl::class.java) {
                    val database = ChatBotDatabase.getInstance(context)
                    repository = MessageRepositoryImpl(database, RetrofitApiManager,
                            FireBaseDatabaseStore)
                }
            }
            return repository!!
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
}