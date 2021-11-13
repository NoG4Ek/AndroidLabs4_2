package com.example.myapplication


import android.content.pm.ActivityInfo
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
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
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun simpleDisplayTest() {
        launchActivity<MainActivity>()
        Thread.sleep(1000)

        checkFirstFragment()
        onView(withId(R.id.bnToSecond)).perform(click())
        checkSecondFragment()
        onView(withId(R.id.bnToThird)).perform(click())
        checkThirdFragment()
        openAbout()
        checkAbout()
    }

    @Test
    fun backStackTestBack() {
        tryToConfuseBackStack(1)
        checkFirstFragment()
        openAbout()
        checkAbout()
        pressBackAfterAbout()
        checkFirstFragment()

        onView(withId(R.id.bnToSecond)).perform(click())
        checkSecondFragment()
        openAbout()
        checkAbout()
        pressBackAfterAbout()
        checkSecondFragment()

        onView(withId(R.id.bnToThird)).perform(click())
        checkThirdFragment()
        openAbout()
        checkAbout()
        pressBackAfterAbout()
        checkThirdFragment()

        pressBack()
        checkSecondFragment()

        pressBack()
        checkFirstFragment()

        try {
            pressBack()
            assert(false)
        } catch (NoActivityResumedException: Exception) {
            assert(true)
        }
    }

    @Test
    fun backStackTestUp() {
        tryToConfuseBackStack(3)
        checkThirdFragment()
        openAbout()
        checkAbout()
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)).perform(click())
        checkThirdFragment()

        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)).perform(click())
        checkSecondFragment()

        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)).perform(click())
        checkFirstFragment()
    }

    @Test
    fun rotationScreenTestSimple() {
        checkFirstFragment()
        scenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Thread.sleep(1000)
        checkFirstFragment()
        scenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        Thread.sleep(1000)
        checkFirstFragment()

        onView(withId(R.id.bnToSecond)).perform(click())
        checkSecondFragment()
        scenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Thread.sleep(1000)
        checkSecondFragment()
        scenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        Thread.sleep(1000)
        checkSecondFragment()

        onView(withId(R.id.bnToThird)).perform(click())
        checkThirdFragment()
        scenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Thread.sleep(1000)
        checkThirdFragment()
        scenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        Thread.sleep(1000)
        checkThirdFragment()

        openAbout()
        checkAbout()
        scenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Thread.sleep(1000)
        checkAbout()
        scenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        Thread.sleep(1000)
        checkAbout()
    }

    @Test
    fun rotationScreenTestInOne() {
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        openAbout()
        checkAbout()
        scenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Thread.sleep(1000)
        checkAbout()

        pressBackAfterAbout()
        checkThirdFragment()
        scenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        Thread.sleep(1000)
        checkThirdFragment()
        scenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Thread.sleep(1000)
        checkThirdFragment()

        pressBack()
        pressBack()
        checkFirstFragment()
        openAbout()
        checkAbout()
        scenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        Thread.sleep(1000)
        pressBackAfterAbout()
        checkFirstFragment()
    }

    private fun tryToConfuseBackStack(last: Int) {
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())
        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())
        onView(withId(R.id.bnToSecond)).perform(click())
        openAbout()
        pressBackAfterAbout()
        onView(withId(R.id.bnToThird)).perform(click())
        pressBack()
        when(last){
            1 -> onView(withId(R.id.bnToFirst)).perform(click())
            3 -> onView(withId(R.id.bnToThird)).perform(click())
        }
    }

    private fun pressBackAfterAbout() {
        pressBack()
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close())
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