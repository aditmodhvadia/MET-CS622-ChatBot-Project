package com.fazemeright.library.api.domain.authentication

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser

interface UserAuthentication {
    /**
     * Sign out the current user.
     */
    fun signOutUser()

    /**
     * Create a new user.
     *
     * @param userEmail              email address
     * @param password               password
     * @param onTaskCompleteListener task listener
     */
    fun createNewUserWithEmailPassword(
            userEmail: String, password: String): Task<UserAuthResult>

    fun signInWithEmailAndPassword(
            userEmail: String, password: String): Task<UserAuthResult>

    fun sendEmailVerification(): Task<Void>?

    fun sendPasswordResetEmail(userEmail: String): Task<Void>
    fun reloadCurrentUserAuthState(): Task<Void>?
    fun updateUserProfile(displayName: String): Task<Void>?

    /**
     * Determine is user is authenticated or not.
     *
     * @return `true` if user is authenticated, else `false`
     */
    val isUserVerified: Boolean
    val currentUserEmail: String?
    val currentUserUid: String?
    val currentUser: FirebaseUser?
    val userName: String?
}