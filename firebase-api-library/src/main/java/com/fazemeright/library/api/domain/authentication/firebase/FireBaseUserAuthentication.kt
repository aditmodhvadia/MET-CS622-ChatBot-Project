package com.fazemeright.library.api.domain.authentication.firebase

import com.fazemeright.library.api.domain.authentication.UserAuthentication
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest

object FireBaseUserAuthentication : UserAuthentication {
    override fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
    }

    override val isUserVerified: Boolean
        get() = FirebaseAuth.getInstance().currentUser != null

    override fun createNewUserWithEmailPassword(userEmail: String, password: String): Task<*> {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, password)
    }

    override fun signInWithEmailAndPassword(userEmail: String, password: String): Task<*> {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, password)
    }

    override fun sendEmailVerification(): Task<Void>? {
        return currentUser?.sendEmailVerification()
    }

    override val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser
    override val userName: String?
        get() = currentUser?.displayName
    override val currentUserEmail: String?
        get() = if (currentUser != null) {
            currentUser!!.email
        } else null

    override fun sendPasswordResetEmail(userEmail: String): Task<Void> {
        return FirebaseAuth.getInstance()
            .sendPasswordResetEmail(userEmail)
    }

    override fun reloadCurrentUserAuthState(): Task<Void>? {
        return currentUser?.reload()
    }

    override fun updateUserProfile(displayName: String): Task<Void>? {
        return currentUser?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(displayName).build()
        )
    }

    override val isUserLoggedIn: Boolean
        get() = currentUser != null

    override val currentUserUid: String?
        get() = if (currentUser != null) {
            currentUser!!.uid
        } else null
}