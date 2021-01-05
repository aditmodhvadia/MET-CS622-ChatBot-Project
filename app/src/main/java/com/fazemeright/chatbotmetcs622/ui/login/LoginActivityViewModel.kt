package com.fazemeright.chatbotmetcs622.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel
import com.fazemeright.library.api.result.Result
import kotlinx.coroutines.launch

class LoginActivityViewModel(application: Application) : BaseViewModel(application) {
    private val userSignedInMutable = MutableLiveData<Result<Boolean>>()

    var userSignedIn: LiveData<Result<Boolean>> = userSignedInMutable

    /**
     * Sign in user with email and password.
     *
     * @param email    email
     * @param password password
     */
    fun signInWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            messageRepository.signInWithEmailAndPassword(email, password).let {
                userSignedInMutable.value = it
            }
        }
    }
}