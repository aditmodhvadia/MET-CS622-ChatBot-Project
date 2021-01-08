package com.fazemeright.chatbotmetcs622.ui.splash

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fazemeright.chatbotmetcs622.domain.UserLoggedInUseCase
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel
import com.fazemeright.library.api.result.Result
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashActivityViewModel(application: Application) : BaseViewModel(application) {
    private val splashObserveUserAuthState by lazy { UserLoggedInUseCase() }

    private val userAuthStateMutable = MutableLiveData<Result<Boolean>>()
    var userAuthState: LiveData<Result<Boolean>> = userAuthStateMutable

    /**
     * Observe for user authentication state changes and report it.
     */
    @SuppressLint("NullSafeMutableLiveData")
    private fun observeForUserAuthenticationState() {
        Timber.d("Auth state observe called")
        viewModelScope.launch {
            userAuthStateMutable.value = splashObserveUserAuthState().also { Timber.d("$it") }
        }
    }

    init {
        observeForUserAuthenticationState()
    }
}