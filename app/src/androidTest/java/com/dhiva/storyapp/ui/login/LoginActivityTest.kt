package com.dhiva.storyapp.ui.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dhiva.storyapp.R
import com.dhiva.storyapp.utils.EspressoIdlingResource
import com.dhiva.storyapp.utils.ToastMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun emailFormat_wrong() {
        onView(withId(R.id.button)).perform(click())
        onView(withId(R.id.et_email)).perform(click(), replaceText("tes"), closeSoftKeyboard())
        onView(withId(R.id.et_password)).perform(
            click(),
            replaceText("123456"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.bt_login)).perform(click())

        onView(withText(R.string.wrong_email_format))
            .inRoot(ToastMatcher().apply {
                matches(isDisplayed())
            })
    }

    @Test
    fun passFormat_wrong() {
        onView(withId(R.id.button)).perform(click())
        onView(withId(R.id.et_email)).perform(
            click(),
            replaceText("tes@gmail.com"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.et_password)).perform(click(), replaceText("123"), closeSoftKeyboard())
        onView(withId(R.id.bt_login)).perform(click())

        onView(withText(R.string.wrong_password_format))
            .inRoot(ToastMatcher().apply {
                matches(isDisplayed())
            })
    }

    @Test
    fun login() {
        onView(withId(R.id.button)).perform(click())
        onView(withId(R.id.et_email)).perform(
            click(),
            replaceText("dhiva@gmail.com"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.et_password)).perform(
            click(),
            replaceText("dhiva123"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.bt_login)).perform(click())
    }

}
