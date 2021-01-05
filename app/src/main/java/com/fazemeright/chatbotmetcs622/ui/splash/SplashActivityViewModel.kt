package com.fazemeright.chatbotmetcs622.ui.splash

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel
import com.fazemeright.library.api.result.Result
import kotlinx.coroutines.launch

class SplashActivityViewModel(application: Application) : BaseViewModel(application) {
    private val userAuthStateMutable = MutableLiveData<Result<Boolean>>()

    var userAuthState: LiveData<Result<Boolean>> = userAuthStateMutable

    /**
     * Observe for user authentication state changes and report it.
     */
    private fun observeForUserAuthenticationState() {
        viewModelScope.launch {
            messageRepository
                    .reloadCurrentUserAuthState().let {
                        userAuthStateMutable.value = it
                    }
        }
    }

    init {
        observeForUserAuthenticationState()
    }
}