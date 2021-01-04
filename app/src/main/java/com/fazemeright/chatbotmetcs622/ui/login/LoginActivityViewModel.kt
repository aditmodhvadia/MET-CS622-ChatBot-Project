package com.fazemeright.chatbotmetcs622.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel
import com.fazemeright.library.api.result.Result
import com.fazemeright.library.api.result.ResultAdapterForBooleanLiveUpdates

class LoginActivityViewModel
/**
 * Constructor.
 *
 * @param application application
 */
(application: Application) : BaseViewModel(application) {
    private val userSignedInMutable = MutableLiveData<Result<Boolean>>()

    @JvmField
    var userSignedIn: LiveData<Result<Boolean>> = userSignedInMutable

    /**
     * Sign in user with email and password.
     *
     * @param email    email
     * @param password password
     */
    fun signInWithEmailPassword(email: String, password: String) {
        runOnThread {
            messageRepository.signInWithEmailAndPassword(email, password,
                    ResultAdapterForBooleanLiveUpdates(userSignedInMutable))
        }
    }
}