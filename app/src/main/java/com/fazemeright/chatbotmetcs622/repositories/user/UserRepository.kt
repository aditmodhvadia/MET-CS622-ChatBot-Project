package com.fazemeright.chatbotmetcs622.repositories.user

import com.fazemeright.library.api.result.Result

interface UserRepository {
    /**
     * Get user name of the current user.
     *
     * @return user name
     */
    val userName: String?

    /**
     * Register new user and store the details.
     *
     * @param userEmail              email
     * @param password               password
     * @param firstName              first name
     * @param lastName               last name
     */
    suspend fun createNewUser(userEmail: String,
                              password: String): Result<Boolean>

    suspend fun updateUserDetails(firstName: String, lastName: String): Result<Boolean>

    /**
     * Call to logout user and clear all messages from Room.
     */
    fun logOutUser()

    /**
     * Sign in user with email and password.
     *
     * @param email    email
     * @param password password
     * @param listener task completion listener`
     */
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Boolean>

    /**
     * Reload the current user authentication state.
     *
     */
    suspend fun reloadCurrentUserAuthState(): Result<Boolean>
}