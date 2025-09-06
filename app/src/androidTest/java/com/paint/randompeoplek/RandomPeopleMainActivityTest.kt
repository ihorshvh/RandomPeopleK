package com.paint.randompeoplek

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
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
    fun testSuccessScenario() = runBlocking<Unit> {
        hiltRule.inject()

        composeRule.onNodeWithText("Random People").assertIsDisplayed()
        composeRule.onNodeWithTag("loading_indicator").assertIsNotDisplayed()
        composeRule.onNodeWithTag("random_people_list_users").assertIsDisplayed()
//        composeRule.onNodeWithTag("random_people_list_users")
//            .onChildren()
//            .assertCountEquals(10)
        composeRule.onNodeWithTag("random_people_list_users").onChildAt(1).performClick()

        composeRule.waitForIdle()

        composeRule.onNodeWithTag("loading_indicator").assertIsNotDisplayed()

        composeRule.onNodeWithTag("user_image").assertIsDisplayed()
        composeRule.onNodeWithTag("user_name_icon").assertIsDisplayed()
        composeRule.onNodeWithTag("user_location_icon").assertIsDisplayed()
        composeRule.onNodeWithTag("user_phone_icon").assertIsDisplayed()
        composeRule.onNodeWithTag("user_email_icon").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Navigation image").performClick()

        composeRule.waitForIdle()

        composeRule.onNodeWithTag("random_people_list_users").assertIsDisplayed()
//        composeRule.onNodeWithTag("random_people_list_users")
//            .onChildren()
//            .assertCountEquals(10)
    }
}