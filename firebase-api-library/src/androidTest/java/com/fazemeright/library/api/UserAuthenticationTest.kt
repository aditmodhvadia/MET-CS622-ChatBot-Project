package com.fazemeright.library.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fazemeright.library.api.domain.authentication.UserAuthentication
import com.fazemeright.library.api.domain.authentication.firebase.FireBaseUserAuthentication
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserAuthenticationTest {
    var userAuthentication: UserAuthentication? = null
    val EMAIL_ADDRESS = "test@test.com"
    val PASSWORD = "password123"
    @Before
    fun setUp() {
        userAuthentication = FireBaseUserAuthentication.instance
    }

    @Test
    fun signOutUser() {
        Assert.assertNotNull(userAuthentication!!.currentUserUid)
        userAuthentication!!.signOutUser()
        Assert.assertNull(userAuthentication!!.currentUserUid)
    }

    @get:Test
    val isUserVerified: Unit
        get() {}

    @Test
    fun createNewUserWithEmailPassword() {
    }

    @Test
    fun signInWithEmailAndPassword() {
    }

    @Test
    fun sendEmailVerification() {
    }

    @get:Test
    val currentUserEmail: Unit
        get() {}

    @Test
    fun sendPasswordResetEmail() {
    }

    @Test
    fun reloadCurrentUserAuthState() {
    }

    @get:Test
    val currentUserUid: Unit
        get() {}

    @get:Test
    val currentUser: Unit
        get() {}

    @get:Test
    val userName: Unit
        get() {}
}