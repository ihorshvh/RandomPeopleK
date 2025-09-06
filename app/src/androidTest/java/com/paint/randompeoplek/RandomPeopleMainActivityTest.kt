package com.paint.randompeoplek

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Test
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Rule

@HiltAndroidTest
class RandomPeopleMainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<RandomPeopleMainActivity>()

    @Test
    fun testSuccessScenario() {
        hiltRule.inject()
        // composeRule.awaitIdle()

        composeRule.onNodeWithText("Random People").assertIsDisplayed()
    }
}