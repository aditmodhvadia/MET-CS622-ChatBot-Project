package com.fazemeright.library.api.firebase

import com.fazemeright.library.api.UserAuthResult
import com.fazemeright.library.api.UserAuthentication
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FireBaseUserAuthentication : UserAuthentication {
    override fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
    }

    override val isUserVerified: Boolean
        get() = FirebaseAuth.getInstance().currentUser != null

    override fun createNewUserWithEmailPassword(userEmail: String, password: String): Task<UserAuthResult> {
        val createUserTask = FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, password)
        return TaskAuthResultToUserAuthResultAdapter(createUserTask)
    }

    override fun signInWithEmailAndPassword(userEmail: String, password: String): Task<UserAuthResult> {
        val signInTask = FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, password)
        return TaskAuthResultToUserAuthResultAdapter(signInTask)
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

    override val currentUserUid: String?
        get() = if (currentUser != null) {
            currentUser!!.uid
        } else null

    companion object {
        private var mInstance: FireBaseUserAuthentication? = null

        /**
         * Get singleton instance.
         *
         * @return instance
         */
        @get:Synchronized
        val instance: FireBaseUserAuthentication
            get() {
                if (mInstance == null) {
                    mInstance = FireBaseUserAuthentication()
                }
                return mInstance!!
            }
    }
}