package com.paint.randompeoplek.ui.randompeoplelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.paint.randompeoplek.CoroutineRule
import com.paint.randompeoplek.errorhandler.ErrorEntity
import com.paint.randompeoplek.errorhandler.ErrorHandlerUseCase
import com.paint.randompeoplek.domain.RandomPeopleListUseCase
import com.paint.randompeoplek.domain.model.UserResponse
import com.paint.randompeoplek.model.LiveDataResponse
import com.paint.randompeoplek.model.LoadResult
import com.paint.randompeoplek.ui.model.User
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runTest
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
    fun testGetRandomPeopleListWhenSuccess() = runTest {
        val errorHandlerUseCase = mock(ErrorHandlerUseCase::class.java)
        val randomPeopleListMediator = mock(RandomPeopleListUseCase::class.java)
        val observer: Observer<LoadResult<LiveDataResponse<List<User>>>> = mock(Observer::class.java) as Observer<LoadResult<LiveDataResponse<List<User>>>>

        val users = getUsers()

        `when`(randomPeopleListMediator.getUserList("10")).thenAnswer {
            Thread.sleep(1000)
            UserResponse(users)
        }

        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, errorHandlerUseCase)

        val usersResponseLiveData = viewModel.usersResponseFlow.asLiveData()

        usersResponseLiveData.observeForever(observer)

        viewModel.getRandomPeopleList("10")

        verify(observer, times(1)).onChanged(LoadResult.Loading(LiveDataResponse()))
        verify(observer, times(2)).onChanged(LoadResult.Success(LiveDataResponse()))

        verify(observer, times(3)).onChanged(resourceCaptor.capture())
        assertEquals(3, resourceCaptor.allValues.size)

        assertThat(resourceCaptor.allValues[0], instanceOf(LoadResult.Success::class.java))

        assertThat(resourceCaptor.allValues[1], instanceOf(LoadResult.Loading::class.java))
        assertNull((resourceCaptor.allValues[1] as LoadResult.Loading).data)

        assertThat(resourceCaptor.allValues[2], instanceOf(LoadResult.Success::class.java))
        val resource = (resourceCaptor.allValues[2] as LoadResult.Success)
        assertNotNull(resource.data)

        val liveDataResponse = ((resource.data) as LiveDataResponse<List<User>>)
        assertNull(liveDataResponse.warningThrowable)
        assertNotNull(liveDataResponse.response)
        assertEquals(2, liveDataResponse.response?.size)

        usersResponseLiveData.removeObserver(observer)
    }

    @Test
    fun testGetRandomPeopleListWhenSuccessButWithError() = runTest {
        val errorHandlerUseCase = mock(ErrorHandlerUseCase::class.java)

        val randomPeopleListMediator = mock(RandomPeopleListUseCase::class.java)
        val observer: Observer<LoadResult<LiveDataResponse<List<User>>>> = mock(Observer::class.java) as Observer<LoadResult<LiveDataResponse<List<User>>>>
        val oneTimeMessageObserver : Observer<ErrorEntity> = mock(Observer::class.java) as Observer<ErrorEntity>

        val exception = Exception("exception")
        `when`(errorHandlerUseCase.getErrorEntity(exception)).thenReturn(ErrorEntity.Unknown)

        val users = getUsers()

        `when`(randomPeopleListMediator.getUserList("10")).thenAnswer {
            Thread.sleep(1000)
            UserResponse(
                users,
                exception
            )
        }

        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, errorHandlerUseCase)

        val usersResponseLiveData = viewModel.usersResponseFlow.asLiveData()
        usersResponseLiveData.observeForever(observer)

        val oneTimeErrorLiveData = viewModel.oneTimeErrorFlow.asLiveData()
        oneTimeErrorLiveData.observeForever(oneTimeMessageObserver)

        viewModel.getRandomPeopleList("10")

        verify(oneTimeMessageObserver, times(1)).onChanged(ErrorEntity.Unknown)

        verify(observer, times(1)).onChanged(LoadResult.Loading(LiveDataResponse()))
        verify(observer, times(2)).onChanged(LoadResult.Error(ErrorEntity.Unknown, LiveDataResponse()))

        verify(observer, times(3)).onChanged(resourceCaptor.capture())
        assertEquals(3, resourceCaptor.allValues.size)

        assertThat(resourceCaptor.allValues[0], instanceOf(LoadResult.Error::class.java))

        assertThat(resourceCaptor.allValues[1], instanceOf(LoadResult.Loading::class.java))
        assertNull((resourceCaptor.allValues[1] as LoadResult.Loading).data)

        assertThat(resourceCaptor.allValues[2], instanceOf(LoadResult.Error::class.java))
        val resource = (resourceCaptor.allValues[2] as LoadResult.Error)
        assertNotNull(resource.data)
        assertNotNull(resource.errorEntity)

        val liveDataResponse = ((resource.data) as LiveDataResponse<List<User>>)
        assertNull(liveDataResponse.warningThrowable)
        assertNotNull(liveDataResponse.response)
        assertEquals(2, liveDataResponse.response?.size)

        usersResponseLiveData.removeObserver(observer)
        oneTimeErrorLiveData.removeObserver(oneTimeMessageObserver)
    }

    @Test
    fun testGetRandomPeopleListWhenFailure() = runTest {
        val errorHandlerUseCase = mock(ErrorHandlerUseCase::class.java)
        val randomPeopleListMediator = mock(RandomPeopleListUseCase::class.java)
        val observer: Observer<LoadResult<LiveDataResponse<List<User>>>> = mock(Observer::class.java) as Observer<LoadResult<LiveDataResponse<List<User>>>>
        val oneTimeMessageObserver : Observer<ErrorEntity> = mock(Observer::class.java) as Observer<ErrorEntity>

        val exception = Exception("exception")
        `when`(errorHandlerUseCase.getErrorEntity(exception)).thenReturn(ErrorEntity.Unknown)

        doAnswer {
            Thread.sleep(1000)
            throw exception
        }.`when`(randomPeopleListMediator).getUserList("10")

        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, errorHandlerUseCase)

        val usersResponseLiveData = viewModel.usersResponseFlow.asLiveData()
        usersResponseLiveData.observeForever(observer)

        val oneTimeErrorLiveData = viewModel.oneTimeErrorFlow.asLiveData()
        oneTimeErrorLiveData.observeForever(oneTimeMessageObserver)

        viewModel.getRandomPeopleList("10")

        verify(oneTimeMessageObserver, times(1)).onChanged(ErrorEntity.Unknown)

        verify(observer, times(1)).onChanged(LoadResult.Loading(LiveDataResponse()))
        verify(observer, times(2)).onChanged(LoadResult.Error(ErrorEntity.Unknown))

        verify(observer, times(3)).onChanged(resourceCaptor.capture())
        assertEquals(3, resourceCaptor.allValues.size)

        assertThat(resourceCaptor.allValues[0], instanceOf(LoadResult.Error::class.java))

        assertThat(resourceCaptor.allValues[1], instanceOf(LoadResult.Loading::class.java))
        assertNull((resourceCaptor.allValues[1] as LoadResult.Loading).data)

        assertThat(resourceCaptor.allValues[2], instanceOf(LoadResult.Error::class.java))

        val resource = (resourceCaptor.allValues[2] as LoadResult.Error)
        assertNull(resource.data)
        assertNotNull(resource.errorEntity)

        usersResponseLiveData.removeObserver(observer)
        oneTimeErrorLiveData.removeObserver(oneTimeMessageObserver)
    }

}