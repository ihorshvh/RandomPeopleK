package com.paint.randompeoplek.ui.randompeoplelist

import app.cash.turbine.test
import com.paint.randompeoplek.domain.RandomPeopleListUseCase
import com.paint.randompeoplek.domain.errorhandler.ErrorEntity
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCase
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCaseImpl
import com.paint.randompeoplek.domain.model.UserResponse
import com.paint.randompeoplek.model.Response
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.model.toUiParcelableUsers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertNull
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class RandomPeopleListViewModelTest {

//    @get:Rule
//    val coroutineRule = CoroutineRule()
//
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testGetUserListWhenSuccess() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
            val users = getUsers()

            coEvery { randomPeopleListMediator.getUserList("10") } answers {
                UserResponse(users)
            }

            val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, ErrorHandlerUseCaseImpl())

            val usersResponseStates = mutableListOf<Response<List<User>>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.usersResponseFlow.toList(usersResponseStates)
            }

            val isRefreshingStates = mutableListOf<Boolean>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.isRefreshing.toList(isRefreshingStates)
            }

            val onTimeErrorStates = mutableListOf<ErrorEntity>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.oneTimeErrorFlow.toList(onTimeErrorStates)
            }

            assertEquals(1, usersResponseStates.size)
            val state = usersResponseStates[0]
            assertThat(state, instanceOf(Response.Success::class.java))
            assertEquals(2, state.data?.size)
            assertNull(state.errorEntity)

            assertEquals(1, isRefreshingStates.size)
            assertEquals(isRefreshingStates[0], false)

            assertEquals(0, onTimeErrorStates.size)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun testGetRandomPeopleListWhenSuccessButWithError() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val exception = Throwable("exception")

            val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
            val users = getUsers()
            coEvery { randomPeopleListMediator.getUserList("10") } answers {
                UserResponse(users, exception)
            }

            val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, ErrorHandlerUseCaseImpl())

            val usersResponseStates = mutableListOf<Response<List<User>>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.usersResponseFlow.toList(usersResponseStates)
            }

            assertEquals(1, usersResponseStates.size)
            val state = usersResponseStates[0]
            assertThat(state, instanceOf(Response.Error::class.java))
            assertEquals(2, state.data?.size)
            assertNotNull(state.errorEntity)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.oneTimeErrorFlow.test {
                    val errorEntity = awaitItem()
                    assertEquals(ErrorEntity.Unknown, errorEntity)
                }
            }

            viewModel.getRandomPeopleList("10")
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun testGetRandomPeopleListWhenFailure() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val exception = Throwable("exception")
            val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
            coEvery { randomPeopleListMediator.getUserList("10") } answers {
                throw exception
            }

            val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, ErrorHandlerUseCaseImpl())
            val usersResponseStates = mutableListOf<Response<List<User>>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.usersResponseFlow.toList(usersResponseStates)
            }

            viewModel.getRandomPeopleList("10")

            assertEquals(1, usersResponseStates.size)
            assertThat(usersResponseStates[0], instanceOf(Response.Initial::class.java))

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.oneTimeErrorFlow.test {
                    val errorEntity = awaitItem()
                    assertEquals(ErrorEntity.Unknown, errorEntity)
                }
            }

            viewModel.getRandomPeopleList("10")
        } finally {
            Dispatchers.resetMain()
        }
    }

}