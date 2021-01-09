package com.fazemeright.chatbotmetcs622.domain

import com.fazemeright.chatbotmetcs622.repositories.user.UserRepository
import com.fazemeright.chatbotmetcs622.repositories.user.UserRepositoryImpl

class LogOutUserUseCase(private val userRepository: UserRepository = UserRepositoryImpl.getInstance()) {
    operator fun invoke() {
        return userRepository.logOutUser()
    }
}