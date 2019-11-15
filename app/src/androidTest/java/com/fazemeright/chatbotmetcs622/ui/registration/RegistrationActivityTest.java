package com.fazemeright.chatbotmetcs622.ui.registration;

import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.fazemeright.chatbotmetcs622.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class RegistrationActivityTest {

    private static final String INCORRECT_EMAIL_ADDRESS = "abcd@gmail";
    private static final String CORRECT_EMAIL_ADDRESS = "abcd@gmail.com";
    private static final String CORRECT_PASSWORD = "12345678";

    @Rule
    public ActivityTestRule<RegistrationActivity> mActivityRule = new ActivityTestRule<>(RegistrationActivity.class);

    @Test
    public void incorrect_email_address_error() throws Exception {
        String expected = mActivityRule.getActivity().getString(R.string.incorrect_email_err_msg);
        onView(withId(R.id.userLoginEmailEditText)).perform(typeText(INCORRECT_EMAIL_ADDRESS));
        onView(withId(R.id.btnRegister)).perform(click());
        EditText etEmail = mActivityRule.getActivity().findViewById(R.id.userLoginEmailEditText);
        assertEquals(etEmail.getError().toString(), expected);
    }

    @Test
    public void registration_flow_isCorrect() throws Exception {
        onView(withId(R.id.userLoginEmailEditText)).perform(typeText(CORRECT_EMAIL_ADDRESS));
        onView(withId(R.id.userPasswordEditText)).perform(typeText(CORRECT_PASSWORD));
        onView(withId(R.id.userConPasswordEditText)).perform(typeText(CORRECT_PASSWORD));
        onView(withId(R.id.btnRegister)).perform(click());
//        onView(withId(R.id.btnRegister)).check(matches(withText(mActivityRule.getActivity().getString(R.string.registration_success_msg))));
    }

}