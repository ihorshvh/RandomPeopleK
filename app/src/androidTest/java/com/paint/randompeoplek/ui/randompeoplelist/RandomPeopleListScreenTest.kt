package com.paint.randompeoplek.ui.randompeoplelist

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.paint.randompeoplek.ui.model.Name
import com.paint.randompeoplek.ui.model.Picture
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.theme.RandomPeopleKTheme
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class RandomPeopleListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testRandomPeopleListScreenInitialState() {
        composeRule.setContent {
            RandomPeopleKTheme {
                RandomPeopleListScreenRoot(
                    randomPeopleListState = RandomPeopleListState.Initial,
                    snackbarHostState = SnackbarHostState(),
                    isRefreshing = false,
                    pullRefreshState = PullToRefreshState(),
                    onItemClick = {  },
                    onRefreshClick = {  }
                )
            }
        }

        composeRule.onNodeWithText("Random People").assertIsDisplayed()
        composeRule.onNodeWithText("Profiles fetching…").assertIsDisplayed()
        composeRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun testRandomPeopleListScreenNoUsersState() {
        composeRule.setContent {
            RandomPeopleKTheme {
                RandomPeopleListScreenRoot(
                    randomPeopleListState = RandomPeopleListState.Success(emptyList()),
                    snackbarHostState = SnackbarHostState(),
                    isRefreshing = false,
                    pullRefreshState = PullToRefreshState(),
                    onItemClick = {  },
                    onRefreshClick = {  }
                )
            }
        }

        composeRule.onNodeWithText("Push update button in order to obtain your random users list!").assertIsDisplayed()
        composeRule.onNodeWithTag("no_users_image").assertIsDisplayed()
    }

    @Test
    fun testRandomPeopleListScreenSuccessState() {
        val fakeUsers = listOf(
            User(
                id = "unique_id_1",
                name = Name("Ire Test", "Mr. Ire Test"),
                location = "8400 Jacksonwile road, Raintown, Greenwaland",
                "email@gmail.com",
                phone = "+12345678",
                picture = Picture("", "")
            ),
            User(
                id = "unique_id_2",
                name = Name("John Test", "Mr. John Test"),
                location = "800 Perth road, Raintown, Greenwaland",
                "email@gmail.com",
                phone = "+12345678",
                picture = Picture("", "")
            )
        )
        
        composeRule.setContent {
            RandomPeopleKTheme {
                RandomPeopleListScreenRoot(
                    randomPeopleListState = RandomPeopleListState.Success(fakeUsers),
                    snackbarHostState = SnackbarHostState(),
                    isRefreshing = false,
                    pullRefreshState = PullToRefreshState(),
                    onItemClick = { },
                    onRefreshClick = { }
                )
            }
        }

        composeRule.onNodeWithText("Ire Test").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Profile image Mr. Ire Test").assertIsDisplayed()
        composeRule.onNodeWithText("8400 Jacksonwile road, Raintown, Greenwaland").assertIsDisplayed()

        composeRule.onNodeWithText("John Test").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Profile image Mr. John Test").assertIsDisplayed()
        composeRule.onNodeWithText("800 Perth road, Raintown, Greenwaland").assertIsDisplayed()
    }

//    @Test
//    fun testRandomPeopleListScreen_EmptyState_DisplaysEmptyMessage() {
//        composeRule.setContent {
//            RandomPeopleKTheme {
//                RandomPeopleListScreenRoot(
//                    randomPeopleListState = RandomPeopleListState.Success(emptyList()),
//                    snackbarHostState = SnackbarHostState(),
//                    isRefreshing = false,
//                    pullRefreshState = PullToRefreshState(),
//                    onItemClick = {  },
//                    onRefreshClick = {  }
//                )
//            }
//        }
//
//        composeRule.onNodeWithText("No profiles found").assertIsDisplayed()
//        composeRule.onNodeWithText("Refresh").assertIsDisplayed() // Check for the button
//    }
//
//    @Test
//    fun testRandomPeopleListScreen_ErrorState_DisplaysSnackbarMessage() {
//        val errorMessage = "An unexpected error occurred"
//        // Note: For error states with lists, you might still display old data.
//        // This test assumes an empty screen with an error.
//        composeRule.setContent {
//            RandomPeopleKTheme {
//                RandomPeopleListScreenRoot(
//                    randomPeopleListState = RandomPeopleListState.Error(errorMessage, null),
//                    snackbarHostState = SnackbarHostState(),
//                    isRefreshing = false,
//                    pullRefreshState = PullToRefreshState(),
//                    onItemClick = { },
//                    onRefreshClick = { }
//                )
//            }
//        }
//
//        // In a real app, the error is shown in a Snackbar.
//        // Testing Snackbars requires a bit more setup (launching a coroutine, etc.).
//        // For simplicity, if the error was displayed directly on screen, you would do:
//        // composeRule.onNodeWithText(errorMessage).assertIsDisplayed()
//    }
}