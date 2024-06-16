package com.paint.randompeoplek.ui.randompeopleprofile

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.paint.randompeoplek.ViewModelCoroutineRule
import com.paint.randompeoplek.domain.RandomPeopleProfileUseCase
import com.paint.randompeoplek.domain.errorhandler.ErrorEntity
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCaseImpl
import com.paint.randompeoplek.domain.getUser
import com.paint.randompeoplek.domain.model.toMediatorUser
import com.paint.randompeoplek.model.Response
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RandomPeopleProfileViewModelTest {

    @get:Rule
    val viewModelCoroutineRule = ViewModelCoroutineRule()

    @Test
    fun testGetUserByIdWhenSuccess() = runTest {
        val randomPeopleProfileMediator = mockk<RandomPeopleProfileUseCase>()
        val user = getUser().toMediatorUser()

        coEvery { randomPeopleProfileMediator.getUserById("test_user_id") } answers {
            user
        }

        val viewModel = RandomPeopleProfileViewModel(randomPeopleProfileMediator, ErrorHandlerUseCaseImpl())

        viewModel.userResponseFlow.test {
            viewModel.getUserById("test_user_id")

            assertThat(awaitItem(), instanceOf(Response.Initial::class.java))
            val userResponse = awaitItem()
            assertThat(userResponse, instanceOf(Response.Success::class.java))
            assertNotNull(userResponse.data)
            assertEquals("test_user_id", userResponse.data?.id)
            assertNull(userResponse.errorEntity)
        }
    }

    @Test
    fun testGetUserByIdWhenFailure() = runTest {
        val randomPeopleProfileMediator = mockk<RandomPeopleProfileUseCase>()

        val exception = Throwable("exception")
        coEvery { randomPeopleProfileMediator.getUserById("test_user_id") } answers {
            throw exception
        }

        val viewModel = RandomPeopleProfileViewModel(randomPeopleProfileMediator, ErrorHandlerUseCaseImpl())

        viewModel.userResponseFlow.test {
            viewModel.getUserById("test_user_id")

            assertThat(awaitItem(), instanceOf(Response.Initial::class.java))
            val userResponse = awaitItem()
            assertThat(userResponse, instanceOf(Response.Error::class.java))
            assertNull(userResponse.data)
            assertNotNull(userResponse.errorEntity)
            assertThat(userResponse.errorEntity, instanceOf(ErrorEntity.Unknown::class.java))
        }
    }
}