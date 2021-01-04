package com.fazemeright.chatbotmetcs622.ui.splash

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel
import com.fazemeright.library.api.result.Result
import com.fazemeright.library.api.result.ResultAdapterForBooleanLiveUpdates

class SplashActivityViewModel(application: Application) : BaseViewModel(application) {
    private val userAuthStateMutable = MutableLiveData<Result<Boolean>>()

    @JvmField
    var userAuthState: LiveData<Result<Boolean>> = userAuthStateMutable

    /**
     * Observe for user authentication state changes and report it.
     */
    private fun observeForUserAuthenticationState() {
        messageRepository
                .reloadCurrentUserAuthState(ResultAdapterForBooleanLiveUpdates(userAuthStateMutable))
    }

    init {
        observeForUserAuthenticationState()
    }
}