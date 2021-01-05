package com.fazemeright.chatbotmetcs622.ui.splash

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.fazemeright.chatbotmetcs622.BuildConfig
import com.fazemeright.chatbotmetcs622.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashActivityTest {
    @Rule
    var mActivityRule = ActivityTestRule(SplashActivity::class.java)
    @Test
    @Throws(Exception::class)
    fun display_app_version() {
        val expected = BuildConfig.VERSION_NAME
        Espresso.onView(ViewMatchers.withId(R.id.tvAppVersion)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvAppVersion)).check(ViewAssertions.matches(ViewMatchers.withText(expected)))
    }
}