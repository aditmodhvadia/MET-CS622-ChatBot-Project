package com.fazemeright.library.api

interface UserAuthResult {
    //  TODO: Replace with custom user object
    val user: String?
    val errorMsg: String
}