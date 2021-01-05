package com.fazemeright.chatbotmetcs622.ui.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.ui.registration.RegistrationActivityTest
import org.hamcrest.core.IsNot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @Rule
    var mActivityRule = ActivityTestRule(LoginActivity::class.java)
    @Test
    fun login_with_email_password_isCorrect() {
//        onView(withId(R.id.tvHaveAccount)).check(matches(isDisplayed()));
//        onView(withId(R.id.tvHaveAccount)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.userLoginEmailEditText)).perform(ViewActions.typeText(RegistrationActivityTest.Companion.CORRECT_EMAIL_ADDRESS))
        Espresso.onView(ViewMatchers.withId(R.id.userPasswordEditText)).perform(ViewActions.typeText(RegistrationActivityTest.Companion.CORRECT_PASSWORD), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click())
        //        TODO: Add IdlingResources to successfully run these tests
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        //        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
//        onView(withId(R.id.action_logout)).check(matches(withText("Logout")));
    }
}