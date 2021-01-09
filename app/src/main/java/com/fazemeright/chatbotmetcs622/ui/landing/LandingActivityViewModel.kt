package com.fazemeright.chatbotmetcs622.ui.landing

import android.app.Application
import com.fazemeright.chatbotmetcs622.domain.LogOutUserUseCase
import com.fazemeright.chatbotmetcs622.domain.UserNameUseCase
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel

class LandingActivityViewModel(application: Application) : BaseViewModel(application) {
    internal val userName by lazy { UserNameUseCase() }
    internal val logOutUser by lazy { LogOutUserUseCase() }
}