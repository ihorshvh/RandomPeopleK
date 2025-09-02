package com.paint.randompeoplek.ui.randompeoplelist

import com.paint.randompeoplek.ViewModelCoroutineRule
import com.paint.randompeoplek.domain.RandomPeopleListUseCase
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCaseImpl
import com.paint.randompeoplek.domain.model.UserResponse
import com.paint.randompeoplek.repository.NetworkError
import com.paint.randompeoplek.resourceprovider.ResourceProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RandomPeopleListViewModelTest {

//    @get:Rule
//    val viewModelCoroutineRule = ViewModelCoroutineRule()

    private lateinit var scheduler: TestCoroutineScheduler

    @BeforeEach
    fun setUp() {
        scheduler = TestCoroutineScheduler()
        Dispatchers.setMain(StandardTestDispatcher(scheduler))
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetUserListWhenSuccess() {
        val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
        val users = getUsers()

        coEvery { randomPeopleListMediator.getUserList("10") } answers {
            UserResponse(users)
        }

        val resourceProvider = mockk<ResourceProvider>()
        every { resourceProvider.getString(any()) } returns "error"
        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, ErrorHandlerUseCaseImpl(resourceProvider))

        assertThat(viewModel.randomPeopleListStateFlow.value, instanceOf(RandomPeopleListState.Initial::class.java))
        assertFalse(viewModel.isRefreshing.value)

        scheduler.advanceUntilIdle()

        assertThat(viewModel.randomPeopleListStateFlow.value, instanceOf(RandomPeopleListState.Success::class.java))
        assertFalse(viewModel.isRefreshing.value)
        assertEquals(2, (viewModel.randomPeopleListStateFlow.value as RandomPeopleListState.Success).users.size)

        viewModel.getRandomPeopleList("10")
        assertTrue(viewModel.isRefreshing.value)

        scheduler.advanceUntilIdle()

        assertThat(viewModel.randomPeopleListStateFlow.value, instanceOf(RandomPeopleListState.Success::class.java))
        assertFalse(viewModel.isRefreshing.value)
        assertEquals(2, (viewModel.randomPeopleListStateFlow.value as RandomPeopleListState.Success).users.size)
        assertNull(viewModel.snackbarHostState.currentSnackbarData)
    }

    @Test
    fun testGetRandomPeopleListWhenSuccessButWithError() {
        val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
        val users = getUsers()

        coEvery { randomPeopleListMediator.getUserList("10") } answers {
            UserResponse(users, NetworkError.NO_INTERNET)
        }

        val resourceProvider = mockk<ResourceProvider>()
        every { resourceProvider.getString(any()) } returns "error"
        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, ErrorHandlerUseCaseImpl(resourceProvider))

        assertThat(viewModel.randomPeopleListStateFlow.value, instanceOf(RandomPeopleListState.Initial::class.java))
        assertFalse(viewModel.isRefreshing.value)

        scheduler.advanceUntilIdle()

        assertThat(viewModel.randomPeopleListStateFlow.value, instanceOf(RandomPeopleListState.Error::class.java))
        assertFalse(viewModel.isRefreshing.value)
        assertEquals(2, (viewModel.randomPeopleListStateFlow.value as RandomPeopleListState.Error).users?.size)

        viewModel.getRandomPeopleList("10")
        assertTrue(viewModel.isRefreshing.value)

        scheduler.advanceUntilIdle()

        assertThat(viewModel.randomPeopleListStateFlow.value, instanceOf(RandomPeopleListState.Error::class.java))
        assertFalse(viewModel.isRefreshing.value)
        assertEquals(2, (viewModel.randomPeopleListStateFlow.value as RandomPeopleListState.Error).users?.size)
        assertNotNull(viewModel.snackbarHostState.currentSnackbarData)
        assertEquals("error", viewModel.snackbarHostState.currentSnackbarData?.message)
    }

    @Test
    fun testGetRandomPeopleListWhenFailure() {
        val exception = Throwable("exception")
        val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
        coEvery { randomPeopleListMediator.getUserList("10") } answers {
            throw exception
        }

        val resourceProvider = mockk<ResourceProvider>()
        every { resourceProvider.getString(any()) } returns "error"
        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, ErrorHandlerUseCaseImpl(resourceProvider))

        assertThat(viewModel.randomPeopleListStateFlow.value, instanceOf(RandomPeopleListState.Initial::class.java))
        assertFalse(viewModel.isRefreshing.value)

        scheduler.advanceUntilIdle()

        assertThat(viewModel.randomPeopleListStateFlow.value, instanceOf(RandomPeopleListState.Error::class.java))

        viewModel.getRandomPeopleList("10")
        assertTrue(viewModel.isRefreshing.value)

        scheduler.advanceUntilIdle()

        assertFalse(viewModel.isRefreshing.value)
        assertNotNull(viewModel.snackbarHostState.currentSnackbarData)
        assertEquals("error", viewModel.snackbarHostState.currentSnackbarData?.message)
    }
}