package com.paint.randompeoplek.ui.randompeoplelist

import app.cash.turbine.turbineScope
import com.paint.randompeoplek.ViewModelCoroutineRule
import com.paint.randompeoplek.domain.RandomPeopleListUseCase
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCaseImpl
import com.paint.randompeoplek.domain.model.UserResponse
import com.paint.randompeoplek.model.Response
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RandomPeopleListViewModelTest {
// TODO fix tests
//
//    @get:Rule
//    val viewModelCoroutineRule = ViewModelCoroutineRule()
//
//    @Test
//    fun testGetUserListWhenSuccess() = runTest {
//        val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
//        val users = getUsers()
//
//        coEvery { randomPeopleListMediator.getUserList("10") } answers {
//            UserResponse(users)
//        }
//
//        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, ErrorHandlerUseCaseImpl())
//
//        turbineScope {
//            val usersResponseFlows = viewModel.usersResponseFlow.testIn(backgroundScope)
//            val isRefreshingFlows = viewModel.isRefreshing.testIn(backgroundScope)
//            val oneTimeErrorFlows = viewModel.oneTimeErrorFlow.testIn(backgroundScope)
//
//            assertThat(usersResponseFlows.awaitItem(), instanceOf(Response.Initial::class.java))
//
//            val userResponse = usersResponseFlows.awaitItem()
//            assertThat(userResponse, instanceOf(Response.Success::class.java))
//            assertEquals(2, userResponse.data?.size)
//            assertNull(userResponse.errorEntity)
//            assertFalse(isRefreshingFlows.awaitItem())
//
//            viewModel.getRandomPeopleList("10")
//
//            assertTrue(isRefreshingFlows.awaitItem())
//
//            val userResponseRefreshed = usersResponseFlows.awaitItem()
//            assertThat(userResponseRefreshed, instanceOf(Response.Success::class.java))
//            assertEquals(2, userResponseRefreshed.data?.size)
//            assertNull(userResponseRefreshed.errorEntity)
//
//            assertFalse(isRefreshingFlows.awaitItem())
//            oneTimeErrorFlows.expectNoEvents()
//        }
//    }
//
//    @Test
//    fun testGetRandomPeopleListWhenSuccessButWithError() = runTest {
//        val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
//        val users = getUsers()
//
//        val exception = Throwable("exception")
//        coEvery { randomPeopleListMediator.getUserList("10") } answers {
//            UserResponse(users, exception)
//        }
//
//        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, ErrorHandlerUseCaseImpl())
//
//        turbineScope {
//            val usersResponseFlows = viewModel.usersResponseFlow.testIn(backgroundScope)
//            val isRefreshingFlows = viewModel.isRefreshing.testIn(backgroundScope)
//            val oneTimeErrorFlows = viewModel.oneTimeErrorFlow.testIn(backgroundScope)
//
//            assertThat(usersResponseFlows.awaitItem(), instanceOf(Response.Initial::class.java))
//
//            val userResponse = usersResponseFlows.awaitItem()
//            assertThat(userResponse, instanceOf(Response.Error::class.java))
//            assertEquals(2, userResponse.data?.size)
//            assertNotNull(userResponse.errorEntity)
//            assertFalse(isRefreshingFlows.awaitItem())
//            assertThat(oneTimeErrorFlows.awaitItem(), instanceOf(ErrorEntity.Unknown::class.java))
//
//            viewModel.getRandomPeopleList("10")
//
//            assertTrue(isRefreshingFlows.awaitItem())
//
//            val userResponseRefreshed = usersResponseFlows.awaitItem()
//            assertThat(userResponseRefreshed, instanceOf(Response.Error::class.java))
//            assertEquals(2, userResponseRefreshed.data?.size)
//            assertNotNull(userResponseRefreshed.errorEntity)
//            assertFalse(isRefreshingFlows.awaitItem())
//            assertThat(oneTimeErrorFlows.awaitItem(), instanceOf(ErrorEntity.Unknown::class.java))
//        }
//    }
//
//    @Test
//    fun testGetRandomPeopleListWhenFailure() = runTest {
//        val exception = Throwable("exception")
//        val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
//        coEvery { randomPeopleListMediator.getUserList("10") } answers {
//            throw exception
//        }
//
//        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, ErrorHandlerUseCaseImpl())
//
//        turbineScope {
//            val usersResponseFlows = viewModel.usersResponseFlow.testIn(backgroundScope)
//            val isRefreshingFlows = viewModel.isRefreshing.testIn(backgroundScope)
//            val oneTimeErrorFlows = viewModel.oneTimeErrorFlow.testIn(backgroundScope)
//
//            assertThat(usersResponseFlows.awaitItem(), instanceOf(Response.Initial::class.java))
//            assertThat(oneTimeErrorFlows.awaitItem(), instanceOf(ErrorEntity.Unknown::class.java))
//
//            viewModel.getRandomPeopleList("10")
//            assertThat(oneTimeErrorFlows.awaitItem(), instanceOf(ErrorEntity.Unknown::class.java))
//            assertFalse(isRefreshingFlows.awaitItem())
//        }
//    }
}