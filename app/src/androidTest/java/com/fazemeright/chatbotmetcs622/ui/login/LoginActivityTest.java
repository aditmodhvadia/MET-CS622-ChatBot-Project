package com.fazemeright.chatbotmetcs622.ui.login;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.ui.registration.RegistrationActivityTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void login_with_email_password_isCorrect() {
//        onView(withId(R.id.tvHaveAccount)).check(matches(isDisplayed()));
//        onView(withId(R.id.tvHaveAccount)).perform(click());
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.userLoginEmailEditText)).perform(typeText(RegistrationActivityTest.CORRECT_EMAIL_ADDRESS));
        onView(withId(R.id.userPasswordEditText)).perform(typeText(RegistrationActivityTest.CORRECT_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());
//        TODO: Add IdlingResources to successfully run these tests
        onView(withId(R.id.btnLogin)).check(matches(not(isDisplayed())));
//        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
//        onView(withId(R.id.action_logout)).check(matches(withText("Logout")));
    }
}