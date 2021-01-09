package com.fazemeright.chatbotmetcs622.domain

import android.app.Application
import androidx.lifecycle.LiveData
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.chatbotmetcs622.repositories.message.MessageRepository
import com.fazemeright.chatbotmetcs622.repositories.message.MessageRepositoryImpl
import com.fazemeright.chatbotmetcs622.repositories.user.UserRepository
import com.fazemeright.chatbotmetcs622.repositories.user.UserRepositoryImpl
import com.fazemeright.library.api.result.Result

class ClearAllMessagesForChatRoomUseCase(private val application: Application) {
    private val messageRepository: MessageRepository

    init {
        messageRepository = MessageRepositoryImpl.getInstance(application)
    }

    suspend operator fun invoke(chatRoom: ChatRoom) {
        return messageRepository.clearAllChatRoomMessages(chatRoom)
    }
}