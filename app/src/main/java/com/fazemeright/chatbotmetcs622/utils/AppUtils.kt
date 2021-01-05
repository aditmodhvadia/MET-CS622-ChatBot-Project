package com.fazemeright.chatbotmetcs622.utils

import android.text.TextUtils
import android.util.Patterns

object AppUtils {
    /**
     * Call to check validity of given email address.
     *
     * @param email given email address
     * @return `true` if given email is valid, else `false`
     */
    @JvmStatic
    fun isValidEmail(email: String): Boolean =
            !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /**
     * Call to check if given password is valid i.e. it is longer than 7 characters.
     *
     * @param password given password
     * @return `true` if given password is valid, else `false`
     */
    @JvmStatic
    fun isValidPassword(password: String): Boolean =
            !TextUtils.isEmpty(password) && password.length > 7

    /**
     * Call to check if given password match or not.
     *
     * @param password    given password
     * @param conPassword given confirm password
     * @return `true` if both passwords match, else `false`
     */
    @JvmStatic
    fun arePasswordsValid(password: String, conPassword: String): Boolean = password == conPassword

    /**
     * Call to check if given name is valid or not. It should not be empty.
     *
     * @param name given name
     * @return `true` if given name is valid, else `false`
     */
    @JvmStatic
    fun isValidName(name: String?): Boolean = !TextUtils.isEmpty(name)
}