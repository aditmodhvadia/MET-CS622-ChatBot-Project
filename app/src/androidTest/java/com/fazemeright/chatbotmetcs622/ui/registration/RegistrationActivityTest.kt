package com.fazemeright.chatbotmetcs622.ui.registration

import android.widget.EditText
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.fazemeright.chatbotmetcs622.R
import org.hamcrest.core.IsNot
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationActivityTest {
    @Rule
    var mActivityRule = ActivityTestRule(RegistrationActivity::class.java)
    @Test
    @Throws(Exception::class)
    fun incorrect_email_address_error() {
        val expected = mActivityRule.activity.getString(R.string.incorrect_email_err_msg)
        Espresso.onView(ViewMatchers.withId(R.id.userLoginEmailEditText)).perform(ViewActions.typeText(INCORRECT_EMAIL_ADDRESS))
        Espresso.onView(ViewMatchers.withId(R.id.btnRegister)).perform(ViewActions.click())
        val etEmail = mActivityRule.activity.findViewById<EditText>(R.id.userLoginEmailEditText)
        Assert.assertEquals(etEmail.error.toString(), expected)
    }

    @Test
    @Throws(Exception::class)
    fun registration_flow_isCorrect() {
//        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.userLoginEmailEditText)).perform(ViewActions.typeText(CORRECT_EMAIL_ADDRESS))
        Espresso.onView(ViewMatchers.withId(R.id.userPasswordEditText)).perform(ViewActions.typeText(CORRECT_PASSWORD))
        Espresso.onView(ViewMatchers.withId(R.id.userConPasswordEditText)).perform(ViewActions.typeText(CORRECT_PASSWORD), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btnRegister)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btnRegister)).check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
    }

    companion object {
        const val INCORRECT_EMAIL_ADDRESS = "abcd@gmail"
        const val CORRECT_EMAIL_ADDRESS = "unittesting@gmail.com"
        const val CORRECT_PASSWORD = "12345678"
    }
}