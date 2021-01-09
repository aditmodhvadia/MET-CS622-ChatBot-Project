package com.fazemeright.chatbotmetcs622.domain

import android.app.Application
import com.fazemeright.chatbotmetcs622.repositories.message.MessageRepository
import com.fazemeright.chatbotmetcs622.repositories.message.MessageRepositoryImpl

class SyncLocalAndCloudMessagesUseCase(private val application: Application) {
    private val messageRepository: MessageRepository by lazy { MessageRepositoryImpl.getInstance(application) }

    suspend operator fun invoke() {
        return messageRepository.syncMessagesWithCloudAndLocal()
    }
}