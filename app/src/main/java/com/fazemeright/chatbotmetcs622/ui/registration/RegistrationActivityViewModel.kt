package com.fazemeright.chatbotmetcs622.ui.registration

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fazemeright.chatbotmetcs622.domain.RegisterUserDomain
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel
import com.fazemeright.library.api.result.Result
import kotlinx.coroutines.launch

class RegistrationActivityViewModel(
        application: Application) : BaseViewModel(application) {
    private val registerUserAndStoreDetails by lazy { RegisterUserDomain() }
    private val userRegisteredMutable = MutableLiveData<Result<Boolean>>()

    var userRegistered: LiveData<Result<Boolean>> = userRegisteredMutable

    /**
     * Register the new user and store the details.
     *
     * @param email     email
     * @param password  password
     * @param firstName first name
     * @param lastName  last name
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun registerNewUser(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            userRegisteredMutable.value = registerUserAndStoreDetails(email, password, firstName, lastName)
        }
    }
}