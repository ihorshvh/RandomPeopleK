package com.paint.randompeoplek.ui.randompeopleprofile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.paint.randompeoplek.ui.model.Name
import com.paint.randompeoplek.ui.model.Picture
import com.paint.randompeoplek.ui.model.User
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class RandomPeopleProfileScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testUserProfileScreenInitialState() {
        composeRule.setContent {
            UserProfileScreenRoot(RandomPeopleProfileState.Initial, {})
        }
        composeRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun testUserProfileScreenErrorState() {
        composeRule.setContent {
            UserProfileScreenRoot(RandomPeopleProfileState.Error(), {})
        }
        composeRule.onNodeWithContentDescription("User profile error").assertIsDisplayed()
        composeRule.onNodeWithText("Unexpected error while the profile loading! Try later!").assertIsDisplayed()
    }

    @Test
    fun testUserProfileScreenSuccessState() {
        val user = User(
            id = "unique_id",
            name = Name("Ire Test", "Mr. Ire Test"),
            location = "8400 Jacksonwile road, Raintown, Greenwaland",
            "email@gmail.com",
            phone = "+12345678",
            picture = Picture("", "")
        )


        composeRule.setContent {
            UserProfileScreenRoot(RandomPeopleProfileState.Success(user), {})
        }

        composeRule.onNodeWithTag("user_image").assertIsDisplayed()
        composeRule.onNodeWithTag("user_name_icon").assertIsDisplayed()
        composeRule.onNodeWithText("Mr. Ire Test").assertIsDisplayed()
        composeRule.onNodeWithTag("user_location_icon").assertIsDisplayed()
        composeRule.onNodeWithText("8400 Jacksonwile road, Raintown, Greenwaland").assertIsDisplayed()

        composeRule.onNodeWithTag("user_phone_icon").assertIsDisplayed()
        composeRule.onNodeWithText("+12345678").assertIsDisplayed()

        composeRule.onNodeWithTag("user_email_icon").assertIsDisplayed()
        composeRule.onNodeWithText("email@gmail.com").assertIsDisplayed()
    }
}