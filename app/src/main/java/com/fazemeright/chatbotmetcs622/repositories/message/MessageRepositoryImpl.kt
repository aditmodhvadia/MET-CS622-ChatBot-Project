package com.fazemeright.chatbotmetcs622.repositories.message

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.network_library.retrofit.RetrofitApiManager
import com.fazemeright.chatbotmetcs622.database.ChatBotDatabase
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.database.message.Message.Companion.newMessage
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.library.api.domain.authentication.UserAuthentication
import com.fazemeright.library.api.domain.authentication.firebase.FireBaseUserAuthentication
import com.fazemeright.library.api.domain.database.DatabaseStore
import com.fazemeright.library.api.domain.database.firebase.FireBaseDatabaseStore
import com.fazemeright.library.api.result.Result
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

class MessageRepositoryImpl private constructor(
        private val database: ChatBotDatabase,
        private val apiManager: RetrofitApiManager,
        private val userAuthentication: UserAuthentication,
        private val onlineDatabaseStore: DatabaseStore
) : MessageRepository {
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
    override suspend fun deleteMessage(message: Message) {
        withContext(Dispatchers.IO) {
            database.messageDao().deleteItem(message)
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
        return withContext(Dispatchers.IO) {
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
        withContext(Dispatchers.IO) {
            addMessageToFireBase(newMessage)
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
     * Call to clear all messages from Room.
     */
    override suspend fun clearAllMessages() {
        withContext(Dispatchers.IO) {
            database.messageDao().clear()
        }
    }

    /**
     * Add given list of messages to Room.
     */
    private suspend fun addMessagesToLocal(messages: List<Message>) {
        withContext(Dispatchers.IO) {
            database.messageDao().insertAllMessages(messages)
        }
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

    override suspend fun syncMessagesWithCloudAndLocal() {
        addMessagesToFireBase(allMessagesInLocal)
        syncMessagesFromFireStoreToRoom()
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
                    repository = MessageRepositoryImpl(database, RetrofitApiManager, FireBaseUserAuthentication,
                            FireBaseDatabaseStore)
                }
            }
            return repository!!
        }
    }
}