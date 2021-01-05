package com.fazemeright.chatbotmetcs622.ui.registration

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel
import com.fazemeright.library.api.result.Result
import kotlinx.coroutines.launch

class RegistrationActivityViewModel(
        application: Application) : BaseViewModel(application) {
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
    fun registerNewUser(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            messageRepository
                    .createNewUserAndStoreDetails(email, password, firstName, lastName).let {
                        userRegisteredMutable.value = it
                    }
        }
    }
}