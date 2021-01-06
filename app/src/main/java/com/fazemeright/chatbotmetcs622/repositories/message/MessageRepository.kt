package com.fazemeright.chatbotmetcs622.repositories.message

import android.content.Context
import androidx.lifecycle.LiveData
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.library.api.result.Result

interface MessageRepository {
    /**
     * Call to get Message with given Message ID.
     *
     * @param messageId given Message ID
     * @return Message with given ID
     */
    fun getMessage(messageId: Long): Message

    /**
     * Call to delete message with given message.
     *
     * @param message given Message
     */
    suspend fun deleteMessage(message: Message)

    /**
     * Get messages from the given chat room.
     *
     * @param chatRoom chat room
     * @return List of Messages
     */
    fun getMessagesForChatRoom(chatRoom: ChatRoom): LiveData<List<Message>>

    /**
     * Called when user sends a given new message in the ChatRoom - Add new Message to Room - Call API
     * to fetch answer for new message - Sync message with FireStore.
     *
     * @param newMessage given new message
     */
    suspend fun newMessageSent(
            context: Context,
            newMessage: Message): Result<Message>

    /**
     * Clear all given chat room messages - From Room - From FireStore.
     *
     * @param chatRoom given chat room
     */
    fun clearAllChatRoomMessages(chatRoom: ChatRoom)

    /**
     * Call to clear all messages from Room.
     */
    suspend fun clearAllMessages()

    /**
     * Call to add the given message to FireStore.
     *
     * @param message given message
     */
    suspend fun addMessageToFireBase(message: Message): Result<Boolean>

    /**
     * Call to add the given messages to FireStore.
     *
     * @param messageList given messages list
     */
    suspend fun addMessagesToFireBase(messageList: List<Message>): Result<Boolean>

    /**
     * Call to sync messages from FireStore to Room for the logged in user.
     */
    suspend fun syncMessagesFromFireStoreToRoom()

    suspend fun syncMessagesWithCloudAndLocal()
}