package com.dhiva.storyapp.ui.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dhiva.storyapp.R
import com.dhiva.storyapp.ui.detail.DetailStoryActivity
import com.dhiva.storyapp.ui.login.LoginActivity
import com.dhiva.storyapp.ui.maps.MapsActivity
import com.dhiva.storyapp.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityLargeTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loadStories() {
        onView(withId(R.id.rv_story)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.rv_story)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )
    }

    @Test
    fun loadDetailStory() {
        Intents.init()
        onView(withId(R.id.rv_story)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        Intents.intended(hasComponent(DetailStoryActivity::class.java.name))
        onView(withId(R.id.iv_story)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.tv_name)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.tv_description)).check(ViewAssertions.matches(isDisplayed()))
        pressBack()
        Intents.release()
    }

    @Test
    fun goToMaps() {
        Intents.init()
        onView(withId(R.id.ib_maps)).perform(click())
        Intents.intended(hasComponent(MapsActivity::class.java.name))
        onView(withId(R.id.map)).check(ViewAssertions.matches(isDisplayed()))
        pressBack()
        Intents.release()
    }

    @Test
    fun goToSettings() {
        Intents.init()
        onView(withId(R.id.ib_settings)).perform(click())
        onView(withId(R.id.setting_fragment)).check(ViewAssertions.matches(isDisplayed()))
        Intents.release()
    }

    @Test
    fun logout() {
        Intents.init()
        onView(withId(R.id.ib_settings)).perform(click())
        onView(withId(R.id.tv_logout)).perform(click())
        Intents.intended(hasComponent(LoginActivity::class.java.name))
        Intents.release()
    }
}