package com.fazemeright.chatbotmetcs622.domain

import com.fazemeright.chatbotmetcs622.repositories.user.UserRepository
import com.fazemeright.chatbotmetcs622.repositories.user.UserRepositoryImpl
import com.fazemeright.library.api.result.Result
import com.fazemeright.library.api.result.safeApiCall

class RegisterUserUseCase(
        private val userRepository: UserRepository = UserRepositoryImpl.getInstance()
) {
    suspend operator fun invoke(email: String, password: String, firstName: String, lastName: String): Result<Boolean> {
        return safeApiCall {
            userRepository.createNewUser(email, password)
            userRepository.updateUserDetails(firstName, lastName)
        }
    }
}