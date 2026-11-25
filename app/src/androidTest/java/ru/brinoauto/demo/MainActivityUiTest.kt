package ru.brinoauto.demo

import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUiTest {
    @Test
    fun bottomNavigationOpensBookingAndProfile() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withText("Автосервис в Омске")).check(matches(isDisplayed()))

        onView(withText("Запись")).perform(click())
        onView(withText("Оставьте данные о визите")).check(matches(isDisplayed()))

        onView(withText("Профиль")).perform(click())
        onView(withText("Карточка клиента")).check(matches(isDisplayed()))
        scenario.close()
    }

    @Test
    fun systemInsetsAreAppliedToHeaderAndBottomNavigation() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity { activity ->
            val root = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as ViewGroup
            val header = root.getChildAt(0)
            val navigation = root.getChildAt(2)
            val basePadding = (15 * activity.resources.displayMetrics.density).toInt()

            assertTrue("Header must leave room for the status bar", header.paddingTop > basePadding)
            assertTrue("Navigation must preserve its own bottom padding", navigation.paddingBottom >= basePadding)
        }
        scenario.close()
    }
}
