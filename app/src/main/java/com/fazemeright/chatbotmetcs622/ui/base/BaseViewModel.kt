package com.fazemeright.chatbotmetcs622.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fazemeright.chatbotmetcs622.repositories.message.MessageRepository
import com.fazemeright.chatbotmetcs622.repositories.message.MessageRepositoryImpl
import com.fazemeright.chatbotmetcs622.repositories.user.UserRepository
import com.fazemeright.chatbotmetcs622.repositories.user.UserRepositoryImpl
import kotlinx.coroutines.launch

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    @JvmField
    protected val messageRepository: MessageRepository = MessageRepositoryImpl.getInstance(application)
    protected val userRepository: UserRepository = UserRepositoryImpl.getInstance(application)

    /**
     * Run on a UI safe thread.
     *
     * @param runnable runnable
     */
    fun runOnThread(runnable: Runnable?) {
        Thread(runnable).start()
    }

    /**
     * Get username of the current logged in user.
     *
     * @return username if logged in, else `null`
     */
    val userName: String?
        get() = userRepository.userName

    /**
     * Logs out the user.
     */
    fun logOutUser() {
        userRepository.logOutUser()
        viewModelScope.launch {
            messageRepository.clearAllMessages()
        }
    }

    fun syncMessagesWithLocalAndCloud() {
        viewModelScope.launch {
            messageRepository.syncMessagesWithCloudAndLocal()
        }
    }
}