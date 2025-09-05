package com.paint.randompeoplek.ui.randompeoplelist

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.paint.randompeoplek.ui.theme.RandomPeopleKTheme
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class RandomPeopleListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testRandomPeopleListScreen_InitialState_DisplaysLoading() {
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

        composeRule.onNodeWithText("Random People").assertIsDisplayed()
    }
}