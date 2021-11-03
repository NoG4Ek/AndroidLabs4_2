package com.example.myapplication


import android.content.pm.ActivityInfo
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkBackStack() {
        tryToConfuseBackStack()
        openAbout()

        checkAbout()

        pressBack()

        checkSecondFragment()

        onView(withId(R.id.bnToThird)).perform(click())
        openAbout()
        checkAbout()

        pressBack()

        checkThirdFragment()

        pressBack()

        checkSecondFragment()

        pressBack()

        checkFirstFragment()
    }

    @Test
    fun checkFragmentsDisplay() {
        checkFirstFragment()

        onView(withId(R.id.bnToSecond)).perform(click())

        checkSecondFragment()

        onView(withId(R.id.bnToThird)).perform(click())

        checkThirdFragment()

        openAbout()

        checkAbout()
    }

    @Test
    fun checkNavigation() {
        checkFirstFragment()

        openAbout()

        checkAbout()

        pressBack()

        onView(withId(R.id.bnToSecond)).perform(click())

        checkSecondFragment()

        openAbout()

        checkAbout()

        pressBack()

        onView(withId(R.id.bnToThird)).perform(click())

        checkThirdFragment()

        openAbout()

        checkAbout()

        pressBack()

        checkThirdFragment()

        onView(withId(R.id.bnToFirst)).perform(click())

        checkFirstFragment()
    }

    @Test
    fun checkRotationScreen() {

        // first fragment
        checkFirstFragment()

        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        checkFirstFragment()

        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        onView(withId(R.id.bnToSecond)).perform(click())

        // second fragment
        checkSecondFragment()

        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        checkSecondFragment()

        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        onView(withId(R.id.bnToThird)).perform(click())

        // third fragment
        checkThirdFragment()

        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        checkThirdFragment()

        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        openAbout()

        checkAbout()

        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        checkAbout()

        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun tryToConfuseBackStack() {
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())
        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())
        onView(withId(R.id.bnToSecond)).perform(click())
    }

    private fun checkFirstFragment(){
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
        onView(withId(R.id.bnToFirst)).check(doesNotExist())
        onView(withId(R.id.bnToSecond)).check(matches(isDisplayed()))
        onView(withId(R.id.bnToThird)).check(doesNotExist())
    }

    private fun checkSecondFragment(){
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))
        onView(withId(R.id.bnToFirst)).check(matches(isDisplayed()))
        onView(withId(R.id.bnToSecond)).check(doesNotExist())
        onView(withId(R.id.bnToThird)).check(matches(isDisplayed()))
    }

    private fun checkThirdFragment(){
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))
        onView(withId(R.id.bnToFirst)).check(matches(isDisplayed()))
        onView(withId(R.id.bnToSecond)).check(matches(isDisplayed()))
        onView(withId(R.id.bnToThird)).check(doesNotExist())
    }

    private fun checkAbout(){
        onView(withId(R.id.activity_main)).check(doesNotExist())
        onView(withId(R.id.activity_about)).check(matches(isDisplayed()))
        onView(withId(R.id.tvAbout)).check(matches(isDisplayed()))
    }

}