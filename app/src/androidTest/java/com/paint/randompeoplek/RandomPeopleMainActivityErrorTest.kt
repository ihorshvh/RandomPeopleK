package com.paint.randompeoplek

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.paint.randompeoplek.di.RandomPeopleServiceModule
import com.paint.randompeoplek.service.RandomPeopleService
import com.paint.randompeoplek.ui.randompeoplelist.TEST_TAG_NO_USERS_IMAGE
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@HiltAndroidTest
@UninstallModules(RandomPeopleServiceModule::class)
class RandomPeopleMainActivityErrorTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<RandomPeopleMainActivity>()

    @BindValue
    @JvmField
    val randomPeopleService: RandomPeopleService = object : RandomPeopleService {
        override suspend fun getUserList(userQuantity: String): Response<com.paint.randompeoplek.service.model.UserResponse> {
            return Response.error(500, "".toResponseBody(null))
        }
    }

    @Test
    fun testSearchScenarioServerError() {
        composeRule.onNodeWithText("Push update button in order to obtain your random users list!").assertExists()
        composeRule.onNodeWithTag(TEST_TAG_NO_USERS_IMAGE).assertIsDisplayed()
        composeRule.onNodeWithText("Server error").assertIsDisplayed()
    }
}