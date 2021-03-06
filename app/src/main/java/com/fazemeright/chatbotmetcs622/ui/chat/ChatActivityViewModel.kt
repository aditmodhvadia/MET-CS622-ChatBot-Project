package com.fazemeright.chatbotmetcs622.ui.chat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.domain.ClearAllMessagesForChatRoomUseCase
import com.fazemeright.chatbotmetcs622.domain.MessagesForChatRoomUseCase
import com.fazemeright.chatbotmetcs622.domain.SendNewQueryMessageInChatRoomUseCase
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel
import com.fazemeright.library.api.result.Result
import kotlinx.coroutines.launch

class ChatActivityViewModel(application: Application) : BaseViewModel(application) {
    private val messageForChatRoom: MessagesForChatRoomUseCase = MessagesForChatRoomUseCase(application)
    private val clearAllMessagesForChatRoom: ClearAllMessagesForChatRoomUseCase = ClearAllMessagesForChatRoomUseCase(application)
    private val sendNewQueryMessageInChatRoom: SendNewQueryMessageInChatRoomUseCase = SendNewQueryMessageInChatRoomUseCase(application)

    private val messageSentMutable = MutableLiveData<Result<Boolean>>()
    var messageSent: LiveData<Result<Boolean>> = messageSentMutable

    fun getMessagesForChatRoom(chatRoom: ChatRoom): LiveData<List<Message>> {
        return messageForChatRoom(chatRoom)
    }

    /**
     * Clear all messages for the chat room.
     *
     * @param chatRoom chat room
     */
    fun clearAllChatRoomMessages(chatRoom: ChatRoom) {
        viewModelScope.launch {
            clearAllMessagesForChatRoom(chatRoom)
        }
    }

    /**
     * Send new message.
     * Store in local database.
     *
     * @param newMessage message
     */
    fun sendNewMessage(newMessage: Message) {
        viewModelScope.launch {
            sendNewQueryMessageInChatRoom(newMessage)
            messageSentMutable.value = Result.Success(true)
        }
    }
}