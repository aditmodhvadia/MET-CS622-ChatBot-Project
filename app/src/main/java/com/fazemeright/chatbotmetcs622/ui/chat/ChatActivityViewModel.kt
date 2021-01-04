package com.fazemeright.chatbotmetcs622.ui.chat

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel
import com.fazemeright.library.api.result.Result
import com.fazemeright.library.api.result.ResultAdapterForBooleanLiveUpdates

class ChatActivityViewModel(application: Application) : BaseViewModel(application) {
    private val messageSentMutable = MutableLiveData<Result<Boolean>>()
    var messageSent: LiveData<Result<Boolean>> = messageSentMutable
    fun getMessagesForChatRoom(chatRoom: ChatRoom): LiveData<List<Message>> {
        return messageRepository.getMessagesForChatRoom(chatRoom)
    }

    /**
     * Clear all messages for the chat room.
     *
     * @param chatRoom chat room
     */
    fun clearAllChatRoomMessages(chatRoom: ChatRoom) {
        runOnThread { messageRepository.clearAllChatRoomMessages(chatRoom) }
    }

    /**
     * Send new message.
     * Store in local database.
     *
     * @param context    context
     * @param newMessage message
     */
    fun sendNewMessage(context: Context, newMessage: Message) {
        runOnThread {
            messageRepository.newMessageSent(context, newMessage,
                    ResultAdapterForBooleanLiveUpdates(messageSentMutable))
        }
    }
}