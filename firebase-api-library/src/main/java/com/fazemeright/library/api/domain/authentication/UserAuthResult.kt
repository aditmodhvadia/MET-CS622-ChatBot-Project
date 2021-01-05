package com.fazemeright.library.api.domain.authentication

interface UserAuthResult {
    //  TODO: Replace with custom user object
    val user: String?
    val errorMsg: String
}