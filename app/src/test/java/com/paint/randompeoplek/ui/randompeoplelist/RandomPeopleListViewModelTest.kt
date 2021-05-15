package com.paint.randompeoplek.ui.randompeoplelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.paint.randompeoplek.CoroutineRule
import com.paint.randompeoplek.mediator.RandomPeopleListMediator
import com.paint.randompeoplek.mediator.model.UserResponse
import com.paint.randompeoplek.model.LiveDataResponse
import com.paint.randompeoplek.model.LoadResult
import com.paint.randompeoplek.ui.model.User
import junit.framework.Assert.assertNull
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RandomPeopleListViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Captor
    private lateinit var resourceCaptor: ArgumentCaptor<LoadResult<LiveDataResponse<List<User>>>>

    @Test
    fun testGetRandomPeopleListWhenSuccess() {
        val randomPeopleListMediator = mock(RandomPeopleListMediator::class.java)
        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator)
        val observer: Observer<LoadResult<LiveDataResponse<List<User>>>> = mock(Observer::class.java) as Observer<LoadResult<LiveDataResponse<List<User>>>>

        runBlockingTest {
            val users = getUsers()

            `when`(randomPeopleListMediator.getUserList("2")).thenReturn(UserResponse(users))
            viewModel.usersResponse.observeForever(observer)
            viewModel.getRandomPeopleList("2")

            verify(observer, times(1)).onChanged(LoadResult.Loading(LiveDataResponse()))
            verify(observer, times(1)).onChanged(LoadResult.Success(LiveDataResponse()))

            verify(observer, times(2)).onChanged(resourceCaptor.capture())
            assertEquals(2, resourceCaptor.allValues.size)

            assertThat(resourceCaptor.allValues[0], instanceOf(LoadResult.Loading::class.java))
            assertNull((resourceCaptor.allValues[0] as LoadResult.Loading).data)

            assertThat(resourceCaptor.allValues[1], instanceOf(LoadResult.Success::class.java))

            val resource = (resourceCaptor.allValues[1] as LoadResult.Success)
            assertNotNull(resource.data)

            val liveDataResponse =
                (((resourceCaptor.allValues[1] as LoadResult.Success).data) as LiveDataResponse<List<User>>)
            assertNull(liveDataResponse.warningThrowable)
            assertNotNull(liveDataResponse.response)
            assertEquals(2, liveDataResponse.response?.size)

            viewModel.usersResponse.removeObserver(observer)
        }
    }

    @Test
    fun testGetRandomPeopleListWhenSuccessWhenReloading() {
        val randomPeopleListMediator = mock(RandomPeopleListMediator::class.java)
        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator)
        val observer: Observer<LoadResult<LiveDataResponse<List<User>>>> = mock(Observer::class.java) as Observer<LoadResult<LiveDataResponse<List<User>>>>

        runBlockingTest {
            val users = getUsers()

            `when`(randomPeopleListMediator.getUserList("2")).thenReturn(UserResponse(users))
            viewModel.usersResponse.observeForever(observer)
            viewModel.getRandomPeopleList("2")
            viewModel.getRandomPeopleList("2")

            verify(observer, times(2)).onChanged(LoadResult.Loading(LiveDataResponse()))
            verify(observer, times(2)).onChanged(LoadResult.Success(LiveDataResponse()))

            verify(observer, times(4)).onChanged(resourceCaptor.capture())

            assertThat(resourceCaptor.allValues[2], instanceOf(LoadResult.Loading::class.java))
            assertNotNull((resourceCaptor.allValues[2] as LoadResult.Loading).data)

            val liveDataResponse =
                (((resourceCaptor.allValues[2] as LoadResult.Loading).data) as LiveDataResponse<List<User>>)
            assertNotNull(liveDataResponse.response)
            assertEquals(2, liveDataResponse.response?.size)

            viewModel.usersResponse.removeObserver(observer)
        }
    }

    @Test
    fun testGetRandomPeopleListWhenSuccessButWithError() {
        val randomPeopleListMediator = mock(RandomPeopleListMediator::class.java)
        val observer: Observer<LoadResult<LiveDataResponse<List<User>>>> = mock(Observer::class.java) as Observer<LoadResult<LiveDataResponse<List<User>>>>
        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator)

        runBlockingTest {

            val users = getUsers()

            `when`(randomPeopleListMediator.getUserList("2")).thenReturn(
                UserResponse(
                    users,
                    Exception("exception")
                )
            )
            viewModel.usersResponse.observeForever(observer)
            viewModel.getRandomPeopleList("2")

            verify(observer, times(1)).onChanged(LoadResult.Loading(LiveDataResponse()))
            verify(observer, times(1)).onChanged(LoadResult.Error("exception", LiveDataResponse()))

            verify(observer, times(2)).onChanged(resourceCaptor.capture())
            assertEquals(2, resourceCaptor.allValues.size)

            assertThat(resourceCaptor.allValues[0], instanceOf(LoadResult.Loading::class.java))
            assertNull((resourceCaptor.allValues[0] as LoadResult.Loading).data)

            assertThat(resourceCaptor.allValues[1], instanceOf(LoadResult.Error::class.java))

            val resource = (resourceCaptor.allValues[1] as LoadResult.Error)
            assertNotNull(resource.data)
            assertNotNull(resource.message)

            val liveDataResponse =
                (((resourceCaptor.allValues[1] as LoadResult.Error).data) as LiveDataResponse<List<User>>)
            assertNull(liveDataResponse.warningThrowable)
            assertNotNull(liveDataResponse.response)
            assertEquals(2, liveDataResponse.response?.size)

            viewModel.usersResponse.removeObserver(observer)
        }
    }

    @Test
    fun testGetRandomPeopleListWhenFailure() {
        val randomPeopleListMediator = mock(RandomPeopleListMediator::class.java)
        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator)
        val observer: Observer<LoadResult<LiveDataResponse<List<User>>>> = mock(Observer::class.java) as Observer<LoadResult<LiveDataResponse<List<User>>>>

        runBlockingTest {
            doAnswer {
                throw Exception("exception")
            }.`when`(randomPeopleListMediator).getUserList("2")
            viewModel.usersResponse.observeForever(observer)
            viewModel.getRandomPeopleList("2")

            verify(observer, times(1)).onChanged(LoadResult.Loading(LiveDataResponse()))
            verify(observer, times(1)).onChanged(LoadResult.Error("exception"))

            verify(observer, times(2)).onChanged(resourceCaptor.capture())
            assertEquals(2, resourceCaptor.allValues.size)

            assertThat(resourceCaptor.allValues[0], instanceOf(LoadResult.Loading::class.java))
            assertNull((resourceCaptor.allValues[0] as LoadResult.Loading).data)

            assertThat(resourceCaptor.allValues[1], instanceOf(LoadResult.Error::class.java))

            val resource = (resourceCaptor.allValues[1] as LoadResult.Error)
            assertNull(resource.data)
            assertNotNull(resource.message)

            viewModel.usersResponse.removeObserver(observer)
        }
    }

}