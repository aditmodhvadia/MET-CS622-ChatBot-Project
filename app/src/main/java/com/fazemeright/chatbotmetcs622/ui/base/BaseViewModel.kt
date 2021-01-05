package com.fazemeright.chatbotmetcs622.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fazemeright.chatbotmetcs622.network.ApiManager
import com.fazemeright.chatbotmetcs622.network.NetworkManager
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository
import kotlinx.coroutines.launch

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    @JvmField
    protected var messageRepository: MessageRepository = MessageRepository.getInstance(application)
    protected var apiManager: ApiManager = ApiManager.instance

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
        get() = messageRepository.userName

    /**
     * Logs out the user.
     */
    fun logOutUser() {
        messageRepository.logOutUser()
    }

    fun syncMessagesWithLocalAndCloud() {
        viewModelScope.launch {
            messageRepository.syncMessagesWithCloudAndLocal()
        }
    }

    init {
        apiManager.init(NetworkManager.instance)
    }
}