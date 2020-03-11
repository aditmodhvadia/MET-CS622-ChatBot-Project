package com.fazemeright.chatbotmetcs622.ui.splash;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.fazemeright.chatbotmetcs622.BuildConfig;
import com.fazemeright.chatbotmetcs622.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void display_app_version() throws Exception {
        String expected = BuildConfig.VERSION_NAME;
        onView(withId(R.id.tvAppVersion)).check(matches(isDisplayed()));
        onView(withId(R.id.tvAppVersion)).check(matches(withText(expected)));
    }
}