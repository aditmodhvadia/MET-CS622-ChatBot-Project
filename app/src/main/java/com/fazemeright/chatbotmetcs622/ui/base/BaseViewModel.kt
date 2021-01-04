package com.fazemeright.chatbotmetcs622.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.fazemeright.chatbotmetcs622.network.ApiManager
import com.fazemeright.chatbotmetcs622.network.NetworkManager
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    @JvmField
    protected var messageRepository: MessageRepository = MessageRepository.getInstance(application)
    protected var apiManager: ApiManager = ApiManager.getInstance()

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

    init {
        apiManager.init(NetworkManager.getInstance())
    }
}