package com.fazemeright.chatbotmetcs622.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class AppUtils {

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 8;
    }

    public static boolean arePasswordsValid(String password, String conPassword) {
        return password.equals(conPassword);
    }
}
