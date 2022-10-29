package com.paint.randompeoplek.ui.randompeoplelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.paint.randompeoplek.CoroutineRule
import com.paint.randompeoplek.domain.errorhandler.ErrorEntity
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCase
import com.paint.randompeoplek.domain.RandomPeopleListUseCase
import com.paint.randompeoplek.domain.model.UserResponse
import com.paint.randompeoplek.model.LiveDataResponse
import com.paint.randompeoplek.model.LoadResult
import com.paint.randompeoplek.ui.model.User
import io.mockk.*
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RandomPeopleListViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testGetRandomPeopleListWhenSuccess() = runTest {
        val errorHandlerUseCase = mockk<ErrorHandlerUseCase>()
        val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
        val observer: Observer<LoadResult<LiveDataResponse<List<User>>>> = mockkClass(Observer::class) as Observer<LoadResult<LiveDataResponse<List<User>>>>

        val users = getUsers()

        coEvery { randomPeopleListMediator.getUserList("10") } answers {
            Thread.sleep(1000)
            UserResponse(users)
        }

        every {
            observer.onChanged(any())
        } just Runs

        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, errorHandlerUseCase)

        val usersResponseLiveData = viewModel.usersResponseFlow.asLiveData()
        usersResponseLiveData.observeForever(observer)

        viewModel.getRandomPeopleList("10")

        verifySequence {
            observer.onChanged(LoadResult.Success(LiveDataResponse()))
            observer.onChanged(LoadResult.Loading(LiveDataResponse()))
            observer.onChanged(LoadResult.Success(LiveDataResponse()))
        }

        val results = mutableListOf<LoadResult<LiveDataResponse<List<User>>>>()
        verify { observer.onChanged(capture(results)) }

        assertEquals(3, results.size)

        assertThat(results[0], instanceOf(LoadResult.Success::class.java))

        assertThat(results[1], instanceOf(LoadResult.Loading::class.java))
        assertNull((results[1] as LoadResult.Loading).data)

        assertThat(results[2], instanceOf(LoadResult.Success::class.java))

        val resource = (results[2] as LoadResult.Success)
        assertNotNull(resource.data)

        val liveDataResponse = ((resource.data) as LiveDataResponse<List<User>>)
        assertNull(liveDataResponse.warningThrowable)
        assertNotNull(liveDataResponse.response)
        assertEquals(2, liveDataResponse.response?.size)

        usersResponseLiveData.removeObserver(observer)
    }

    @Test
    fun testGetRandomPeopleListWhenSuccessButWithError() = runTest {
        val errorHandlerUseCase = mockk<ErrorHandlerUseCase>()
        val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
        val observer: Observer<LoadResult<LiveDataResponse<List<User>>>> = mockkClass(Observer::class) as Observer<LoadResult<LiveDataResponse<List<User>>>>
        val oneTimeMessageObserver : Observer<ErrorEntity> = mockkClass(Observer::class) as Observer<ErrorEntity>

        val exception = Exception("exception")

        every { errorHandlerUseCase.getErrorEntity(exception) } answers {
            ErrorEntity.Unknown
        }

        val users = getUsers()

        coEvery { randomPeopleListMediator.getUserList("10") } answers {
            Thread.sleep(1000)
            UserResponse(users, exception)
        }

        every {
            observer.onChanged(any())
        } just Runs

        every {
            oneTimeMessageObserver.onChanged(any())
        } just Runs

        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, errorHandlerUseCase)

        val usersResponseLiveData = viewModel.usersResponseFlow.asLiveData()
        usersResponseLiveData.observeForever(observer)

        val oneTimeErrorLiveData = viewModel.oneTimeErrorFlow.asLiveData()
        oneTimeErrorLiveData.observeForever(oneTimeMessageObserver)

        viewModel.getRandomPeopleList("10")

        verifySequence {
            observer.onChanged(LoadResult.Error(ErrorEntity.Unknown, LiveDataResponse()))
            observer.onChanged(LoadResult.Loading(LiveDataResponse()))
            observer.onChanged(LoadResult.Error(ErrorEntity.Unknown, LiveDataResponse()))
        }

        val results = mutableListOf<LoadResult<LiveDataResponse<List<User>>>>()
        verify { observer.onChanged(capture(results)) }

        val oneTimeMessageSlot = slot<ErrorEntity>()
        verify { oneTimeMessageObserver.onChanged(capture(oneTimeMessageSlot)) }

        assertEquals(3, results.size)
        assertEquals(ErrorEntity.Unknown, oneTimeMessageSlot.captured)

        assertThat(results[0], instanceOf(LoadResult.Error::class.java))

        assertThat(results[1], instanceOf(LoadResult.Loading::class.java))
        assertNull((results[1] as LoadResult.Loading).data)

        assertThat(results[2], instanceOf(LoadResult.Error::class.java))
        val resource = (results[2] as LoadResult.Error)
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
        val errorHandlerUseCase = mockk<ErrorHandlerUseCase>()
        val randomPeopleListMediator = mockk<RandomPeopleListUseCase>()
        val observer: Observer<LoadResult<LiveDataResponse<List<User>>>> = mockkClass(Observer::class) as Observer<LoadResult<LiveDataResponse<List<User>>>>
        val oneTimeMessageObserver : Observer<ErrorEntity> = mockkClass(Observer::class) as Observer<ErrorEntity>

        val exception = Exception("exception")

        every { errorHandlerUseCase.getErrorEntity(exception) } answers {
            ErrorEntity.Unknown
        }

        coEvery { randomPeopleListMediator.getUserList("10") } answers {
            Thread.sleep(1000)
            throw exception
        }

        every {
            observer.onChanged(any())
        } just Runs

        every {
            oneTimeMessageObserver.onChanged(any())
        } just Runs

        val viewModel = RandomPeopleListViewModel(randomPeopleListMediator, errorHandlerUseCase)

        val usersResponseLiveData = viewModel.usersResponseFlow.asLiveData()
        usersResponseLiveData.observeForever(observer)

        val oneTimeErrorLiveData = viewModel.oneTimeErrorFlow.asLiveData()
        oneTimeErrorLiveData.observeForever(oneTimeMessageObserver)

        viewModel.getRandomPeopleList("10")

        verifySequence {
            observer.onChanged(LoadResult.Error(ErrorEntity.Unknown, LiveDataResponse()))
            observer.onChanged(LoadResult.Loading(LiveDataResponse()))
            observer.onChanged(LoadResult.Error(ErrorEntity.Unknown, LiveDataResponse()))
        }

        val results = mutableListOf<LoadResult<LiveDataResponse<List<User>>>>()
        verify { observer.onChanged(capture(results)) }

        val oneTimeMessageSlot = slot<ErrorEntity>()
        verify { oneTimeMessageObserver.onChanged(capture(oneTimeMessageSlot)) }

        assertEquals(ErrorEntity.Unknown, oneTimeMessageSlot.captured)

        assertThat(results[0], instanceOf(LoadResult.Error::class.java))

        assertThat(results[1], instanceOf(LoadResult.Loading::class.java))
        assertNull((results[1] as LoadResult.Loading).data)

        assertThat(results[2], instanceOf(LoadResult.Error::class.java))

        val resource = (results[2] as LoadResult.Error)
        assertNull(resource.data)
        assertNotNull(resource.errorEntity)

        usersResponseLiveData.removeObserver(observer)
        oneTimeErrorLiveData.removeObserver(oneTimeMessageObserver)
    }

}