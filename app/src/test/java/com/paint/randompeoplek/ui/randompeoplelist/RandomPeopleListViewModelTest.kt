package com.paint.randompeoplek.ui.randompeoplelist

import com.paint.randompeoplek.ViewModelCoroutineExtension
import com.paint.randompeoplek.domain.RandomPeopleListUseCase
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCaseImpl
import com.paint.randompeoplek.domain.model.UserResponse
import com.paint.randompeoplek.repository.NetworkError
import com.paint.randompeoplek.resourceprovider.ResourceProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(ViewModelCoroutineExtension::class)
class RandomPeopleListViewModelTest {

    private lateinit var resourceProvider: ResourceProvider

    @BeforeEach
    fun setUp() {
        resourceProvider = mockk<ResourceProvider>()
        every { resourceProvider.getString(any()) } returns "error"
    }

    @Test
    fun testGetUserListWhenSuccess(scheduler: TestCoroutineScheduler) {
        val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
        val users = getUsers()

        coEvery { randomPeopleListMediator.getUserList("10") } answers {
            UserResponse(users)
        }

        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, ErrorHandlerUseCaseImpl(resourceProvider))

        assertThat(viewModel.randomPeopleListScreenStateFlow.value, instanceOf(RandomPeopleListScreenState.Initial::class.java))
        assertFalse(viewModel.isRefreshing.value)

        scheduler.advanceUntilIdle()

        assertThat(viewModel.randomPeopleListScreenStateFlow.value, instanceOf(RandomPeopleListScreenState.Success::class.java))
        assertFalse(viewModel.isRefreshing.value)
        assertEquals(2, (viewModel.randomPeopleListScreenStateFlow.value as RandomPeopleListScreenState.Success).users.size)

        viewModel.getRandomPeopleList("10")
        assertTrue(viewModel.isRefreshing.value)

        scheduler.advanceUntilIdle()

        assertThat(viewModel.randomPeopleListScreenStateFlow.value, instanceOf(RandomPeopleListScreenState.Success::class.java))
        assertFalse(viewModel.isRefreshing.value)
        assertEquals(2, (viewModel.randomPeopleListScreenStateFlow.value as RandomPeopleListScreenState.Success).users.size)
        assertNull(viewModel.snackbarHostState.currentSnackbarData)
    }

    @Test
    fun testGetRandomPeopleListWhenSuccessButWithError(scheduler: TestCoroutineScheduler) {
        val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
        val users = getUsers()

        coEvery { randomPeopleListMediator.getUserList("10") } answers {
            UserResponse(users, NetworkError.NO_INTERNET)
        }

        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, ErrorHandlerUseCaseImpl(resourceProvider))

        assertThat(viewModel.randomPeopleListScreenStateFlow.value, instanceOf(RandomPeopleListScreenState.Initial::class.java))
        assertFalse(viewModel.isRefreshing.value)

        scheduler.advanceUntilIdle()

        assertThat(viewModel.randomPeopleListScreenStateFlow.value, instanceOf(RandomPeopleListScreenState.Error::class.java))
        assertFalse(viewModel.isRefreshing.value)
        assertEquals(2, (viewModel.randomPeopleListScreenStateFlow.value as com.paint.randompeoplek.ui.randompeoplelist.RandomPeopleListState.Error).users?.size)

        viewModel.getRandomPeopleList("10")
        assertTrue(viewModel.isRefreshing.value)

        scheduler.advanceUntilIdle()

        assertThat(viewModel.randomPeopleListScreenStateFlow.value, instanceOf(RandomPeopleListScreenState.Error::class.java))
        assertFalse(viewModel.isRefreshing.value)
        assertEquals(2, (viewModel.randomPeopleListScreenStateFlow.value as com.paint.randompeoplek.ui.randompeoplelist.RandomPeopleListState.Error).users?.size)
        assertNotNull(viewModel.snackbarHostState.currentSnackbarData)
        assertEquals("error", viewModel.snackbarHostState.currentSnackbarData?.visuals?.message)
    }

    @Test
    fun testGetRandomPeopleListWhenFailure(scheduler: TestCoroutineScheduler) {
        val exception = Throwable("exception")
        val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
        coEvery { randomPeopleListMediator.getUserList("10") } answers {
            throw exception
        }

        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, ErrorHandlerUseCaseImpl(resourceProvider))

        assertThat(viewModel.randomPeopleListScreenStateFlow.value, instanceOf(RandomPeopleListScreenState.Initial::class.java))
        assertFalse(viewModel.isRefreshing.value)

        scheduler.advanceUntilIdle()

        assertThat(viewModel.randomPeopleListScreenStateFlow.value, instanceOf(RandomPeopleListScreenState.Initial::class.java))

        viewModel.getRandomPeopleList("10")

        scheduler.advanceUntilIdle()

        assertFalse(viewModel.isRefreshing.value)
        assertNotNull(viewModel.snackbarHostState.currentSnackbarData)
        assertEquals("error", viewModel.snackbarHostState.currentSnackbarData?.visuals?.message)
    }
}