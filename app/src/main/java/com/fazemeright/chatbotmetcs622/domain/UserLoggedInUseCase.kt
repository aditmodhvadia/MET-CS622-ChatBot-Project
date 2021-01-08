package com.fazemeright.chatbotmetcs622.domain

import com.fazemeright.chatbotmetcs622.repositories.user.UserRepository
import com.fazemeright.chatbotmetcs622.repositories.user.UserRepositoryImpl
import com.fazemeright.library.api.result.Result

class UserLoggedInUseCase(private val userRepository: UserRepository = UserRepositoryImpl.getInstance()) {
    suspend operator fun invoke(): Result<Boolean> {
        return userRepository.reloadCurrentUserAuthState()
    }
}