package com.fazemeright.library.api.domain.authentication.firebase.authresult

import com.fazemeright.library.api.domain.authentication.UserAuthResult
import com.google.firebase.auth.AuthResult

class UserAuthResultAdapterForAuthResult(private val authResult: AuthResult?) : UserAuthResult {
    override val user: String?
        get() = authResult!!.user?.displayName

    // TODO: Parse error here
    override val errorMsg: String
        get() =// TODO: Parse error here
            "Some error occurred, Change this later"
}