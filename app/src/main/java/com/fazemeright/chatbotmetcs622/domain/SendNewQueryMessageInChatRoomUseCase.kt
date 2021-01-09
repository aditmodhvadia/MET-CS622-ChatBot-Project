package com.fazemeright.chatbotmetcs622.domain

import android.app.Application
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.repositories.message.MessageRepository
import com.fazemeright.chatbotmetcs622.repositories.message.MessageRepositoryImpl
import com.fazemeright.library.api.result.Result

class SendNewQueryMessageInChatRoomUseCase(private val application: Application) {
    private val messageRepository: MessageRepository

    init {
        messageRepository = MessageRepositoryImpl.getInstance(application)
    }

    suspend operator fun invoke(newMessage: Message): Result<Message> {
        return messageRepository.newMessageSent(newMessage)
    }
}