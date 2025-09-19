package com.paint.randompeoplek

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextReplacement
import com.paint.randompeoplek.domain.RandomPeopleListUseCase
import com.paint.randompeoplek.domain.model.Name
import com.paint.randompeoplek.domain.model.Picture
import com.paint.randompeoplek.domain.model.User
import com.paint.randompeoplek.domain.model.UserResponse
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_LOADING_INDICATOR
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_RANDOM_PEOPLE_LIST_USER
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_RANDOM_PEOPLE_LIST_USERS
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_SEARCH_CLOSE_ICON
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_SEARCH_ICON
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_SEARCH_TEXT_FILED
import com.paint.randompeoplek.ui.randompeopleprofile.TEST_TAG_USER_EMAIL_ICON
import com.paint.randompeoplek.ui.randompeopleprofile.TEST_TAG_USER_IMAGE
import com.paint.randompeoplek.ui.randompeopleprofile.TEST_TAG_USER_LOCATION_ICON
import com.paint.randompeoplek.ui.randompeopleprofile.TEST_TAG_USER_NAME_ICON
import com.paint.randompeoplek.ui.randompeopleprofile.TEST_TAG_USER_PHONE_ICON
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Test
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
class RandomPeopleMainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<RandomPeopleMainActivity>()

    @BindValue
    @JvmField // JvmField is necessary for Hilt to correctly process the @BindValue field
    val mockUseCase: RandomPeopleListUseCase = mockk()

    private fun getFakeUserResponse(count: Int = 10): UserResponse {
        val users = List(count) { i ->
            User(
                id = "id$i",
                name = Name(shortName = "First$i", fullName = "Last$i"),
                location = "Location $i",
                email = "email$i@example.com",
                phone = "123-456-789$i",
                picture = Picture(
                    medium = "https://randomuser.me/api/portraits/med/men/${i + 1}.jpg",
                    thumbnail = "https://randomuser.me/api/portraits/thumb/men/${i + 1}.jpg"
                )
            )
        }
        return UserResponse(users = users, networkError = null)
    }

    @Test
    fun testSuccessScenario() = runBlocking<Unit> {
        // Configure the mock to return a successful response with fake data
        coEvery { mockUseCase.getUserList(any()) } returns getFakeUserResponse(10)

        hiltRule.inject()


        composeRule.onNodeWithText("Random People").assertIsDisplayed()

        composeRule.onNodeWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USERS).assertIsDisplayed()
        composeRule.onNodeWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USERS)
            .performScrollToNode(
                hasTestTag(TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER)
            )
        composeRule.onAllNodesWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USER)
            .assertCountEquals(9)
        composeRule.onAllNodesWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER)
            .assertCountEquals(1)
        composeRule.onNodeWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USERS).onChildAt(1).performClick()

        composeRule.waitForIdle()

        composeRule.onNodeWithTag(TEST_TAG_LOADING_INDICATOR).assertIsNotDisplayed()

        composeRule.onNodeWithTag(TEST_TAG_USER_IMAGE).assertIsDisplayed()
        composeRule.onNodeWithTag(TEST_TAG_USER_NAME_ICON).assertIsDisplayed()
        composeRule.onNodeWithTag(TEST_TAG_USER_LOCATION_ICON).assertIsDisplayed()
        composeRule.onNodeWithTag(TEST_TAG_USER_PHONE_ICON).assertIsDisplayed()
        composeRule.onNodeWithTag(TEST_TAG_USER_EMAIL_ICON).assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Navigation image").performClick()

        composeRule.waitForIdle()

        composeRule.onNodeWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USERS).assertIsDisplayed()
    }

    @Test
    fun testSearchScenarioSuccess() = runBlocking<Unit> {
        hiltRule.inject()

        composeRule.onNodeWithText("Random People").assertIsDisplayed()

        composeRule.onNodeWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USERS).assertIsDisplayed()
        composeRule.onNodeWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USERS)
            .performScrollToNode(
                hasTestTag(TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER)
            )
        composeRule.onAllNodesWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USER)
            .assertCountEquals(9)
        composeRule.onAllNodesWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER)
            .assertCountEquals(1)

        composeRule.onNodeWithTag(TEST_TAG_SEARCH_ICON).performClick()
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_CLOSE_ICON).performClick()
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_ICON).performClick()

        val nodeInteraction = composeRule.onNodeWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USERS).onChildAt(1)
        val node = nodeInteraction.fetchSemanticsNode()
        //node.config.getOrNull(SemanticsProperties.Text)

        composeRule.onNodeWithTag(TEST_TAG_SEARCH_TEXT_FILED).performTextReplacement("Test")
    }
}