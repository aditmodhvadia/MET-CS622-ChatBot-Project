package com.fazemeright.chatbotmetcs622.repositories.user

import com.fazemeright.chatbotmetcs622.repositories.message.MessageRepositoryImpl
import com.fazemeright.library.api.domain.authentication.UserAuthentication
import com.fazemeright.library.api.domain.authentication.firebase.FireBaseUserAuthentication
import com.fazemeright.library.api.result.Result
import com.fazemeright.library.api.result.safeApiCall
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
        private val userAuthentication: UserAuthentication
) : UserRepository {
    /**
     * Get user name of the current user.
     *
     * @return user name
     */
    override val userName: String?
        get() = userAuthentication.userName

    /**
     * Register new user and store the details.
     *
     * @param userEmail              email
     * @param password               password
     * @param firstName              first name
     * @param lastName               last name
     */
    override suspend fun createNewUser(userEmail: String, password: String, firstName: String, lastName: String): Result<Boolean> {
        return safeApiCall {
            userAuthentication.createNewUserWithEmailPassword(
                    userEmail,
                    password).await()

            userAuthentication.updateUserProfile {
                displayName = "$firstName $lastName"
            }?.await()
            Result.Success(true)
        }
    }


    /**
     * Call to logout user and clear all messages from Room.
     */
    override fun logOutUser() {
        userAuthentication.signOutUser()
    }

    /**
     * Sign in user with email and password.
     *
     * @param email    email
     * @param password password
     * @param listener task completion listener`
     */
    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Boolean> {
        return safeApiCall {
            userAuthentication.signInWithEmailAndPassword(email, password).await().let {
                Result.Success(true)
            }
        }
    }

    /**
     * Reload the current user authentication state.
     *
     */
    override suspend fun reloadCurrentUserAuthState(): Result<Boolean> {
        return safeApiCall {
            userAuthentication.reloadCurrentUserAuthState()?.await()
                    ?: throw Exception("User not logged in")
            Result.Success(true)
        }
    }

    companion object {
        private var repository: UserRepositoryImpl? = null

        /**
         * Call to get instance of MessageRepository with the given context.
         *
         * @param context given context
         * @return synchronized call to get Instance of MessageRepository class
         */
        fun getInstance(): UserRepositoryImpl {
            if (repository == null) {
                synchronized(MessageRepositoryImpl::class.java) {
                    repository = UserRepositoryImpl(FireBaseUserAuthentication)
                }
            }
            return repository!!
        }
    }
}