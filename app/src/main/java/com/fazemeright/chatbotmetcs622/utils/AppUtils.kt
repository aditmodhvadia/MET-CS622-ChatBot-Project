package com.fazemeright.chatbotmetcs622.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class AppUtils {

  /**
   * Call to check validity of given email address.
   *
   * @param email given email address
   * @return <code>true</code> if given email is valid, else <code>false</code>
   */
  public static boolean isValidEmail(String email) {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
  }

  /**
   * Call to check if given password is valid i.e. it is longer than 7 characters.
   *
   * @param password given password
   * @return <code>true</code> if given password is valid, else <code>false</code>
   */
  public static boolean isValidPassword(String password) {
    return !TextUtils.isEmpty(password) && password.length() > 7;
  }

  /**
   * Call to check if given password match or not.
   *
   * @param password    given password
   * @param conPassword given confirm password
   * @return <code>true</code> if both passwords match, else <code>false</code>
   */
  public static boolean arePasswordsValid(String password, String conPassword) {
    return password.equals(conPassword);
  }

  /**
   * Call to check if given name is valid or not. It should not be empty.
   *
   * @param name given name
   * @return <code>true</code> if given name is valid, else <code>false</code>
   */
  public static boolean isValidName(String name) {
    return !TextUtils.isEmpty(name);
  }
}
