package com.paint.randompeoplek

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.requestFocus
import com.paint.randompeoplek.di.RandomPeopleServiceModule
import com.paint.randompeoplek.service.RandomPeopleService
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_NO_USERS_IMAGE
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_RANDOM_PEOPLE_LIST_USER
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_RANDOM_PEOPLE_LIST_USERS
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_SEARCH_CLOSE_ICON
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_SEARCH_ICON
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_SEARCH_TEXT_FILED
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Test
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import retrofit2.Response

@HiltAndroidTest
@UninstallModules(RandomPeopleServiceModule::class)
class RandomPeopleMainActivityMockTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<RandomPeopleMainActivity>()

    @BindValue
    @JvmField
    val randomPeopleService: RandomPeopleService = object : RandomPeopleService {
        override suspend fun getUserList(userQuantity: String): Response<com.paint.randompeoplek.service.model.UserResponse> {
            return Response.success(
                200,
                getUserResponse()
            )
        }
    }

    @Test
    fun testSearchScenarioSuccess() {
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_ICON).performClick()
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_TEXT_FILED).assertIsDisplayed()
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_TEXT_FILED).requestFocus()
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_TEXT_FILED).performTextInput("Se")
        composeRule.onNodeWithText("Se").assertExists()

        composeRule.onAllNodesWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USER)
            .assertCountEquals(3)
        composeRule.onAllNodesWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER)
            .assertCountEquals(1)

        composeRule.onNodeWithTag(TEST_TAG_SEARCH_TEXT_FILED).requestFocus()
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_TEXT_FILED).performTextReplacement("Sean")
        composeRule.onNodeWithText("Sean").assertExists()

        composeRule.onAllNodesWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER).assertCountEquals(1)
        composeRule.onNodeWithText("Sean Shelton", substring = true).assertExists()

        composeRule.onNodeWithTag(TEST_TAG_SEARCH_CLOSE_ICON).performClick()

        composeRule.onNodeWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USERS)
            .performScrollToNode(
                hasTestTag(TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER)
            )
        composeRule.onAllNodesWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USER)
            .assertCountEquals(9)
        composeRule.onAllNodesWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER)
            .assertCountEquals(1)

        composeRule.onNodeWithTag(TEST_TAG_SEARCH_ICON).performClick()
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_TEXT_FILED).assertIsDisplayed()
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_TEXT_FILED).requestFocus()
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_TEXT_FILED).performTextInput("Madsen")

        composeRule.onAllNodesWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER).assertCountEquals(1)
        composeRule.onNodeWithText("Malou Madsen", substring = true).assertExists()
    }

    @Test
    fun testSearchScenarioNoUsersFound() {
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_ICON).performClick()
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_TEXT_FILED).assertIsDisplayed()
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_TEXT_FILED).requestFocus()
        composeRule.onNodeWithTag(TEST_TAG_SEARCH_TEXT_FILED).performTextInput("NoSuchUser")
        composeRule.onNodeWithText("NoSuchUser").assertExists()

        composeRule.onAllNodesWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_USER)
            .assertCountEquals(0)
        composeRule.onAllNodesWithTag(TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER)
            .assertCountEquals(0)

        composeRule.onNodeWithText("Push update button in order to obtain your random users list!").assertExists()
        composeRule.onNodeWithTag(TEST_TAG_NO_USERS_IMAGE).assertIsDisplayed()
    }
}