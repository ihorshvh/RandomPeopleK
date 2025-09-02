package com.paint.randompeoplek.ui.randompeopleprofile

import com.paint.randompeoplek.ViewModelCoroutineExtension
import com.paint.randompeoplek.domain.RandomPeopleProfileUseCase
import com.paint.randompeoplek.domain.getUser
import com.paint.randompeoplek.domain.model.toMediatorUser
import com.paint.randompeoplek.resourceprovider.ResourceProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(ViewModelCoroutineExtension::class)
class RandomPeopleProfileViewModelTest {

    private lateinit var resourceProvider: ResourceProvider

    @BeforeEach
    fun setUp() {
        resourceProvider = mockk<ResourceProvider>()
        every { resourceProvider.getString(any()) } returns "error"
    }

    @Test
    fun testGetUserByIdWhenSuccess(scheduler: TestCoroutineScheduler) {
        val randomPeopleProfileMediator = mockk<RandomPeopleProfileUseCase>()
        val user = getUser().toMediatorUser()

        coEvery { randomPeopleProfileMediator.getUserById("test_user_id") } answers {
            user
        }

        val viewModel = RandomPeopleProfileViewModel(randomPeopleProfileMediator)

        assertThat(viewModel.randomPeopleProfileStateFlow.value, instanceOf(RandomPeopleProfileState.Initial::class.java))
        viewModel.getUserById("test_user_id")

        scheduler.advanceUntilIdle()

        val userResponse = viewModel.randomPeopleProfileStateFlow.value
        assertThat(userResponse, instanceOf(RandomPeopleProfileState.Success::class.java))
        val userSuccess = (userResponse as RandomPeopleProfileState.Success)
        assertNotNull(userSuccess)
        assertEquals("test_user_id", userSuccess.user.id)
    }

    @Test
    fun testGetUserByIdWhenFailure(scheduler: TestCoroutineScheduler) {
        val randomPeopleProfileMediator = mockk<RandomPeopleProfileUseCase>()

        val exception = Throwable("exception")
        coEvery { randomPeopleProfileMediator.getUserById("test_user_id") } answers {
            throw exception
        }

        val viewModel = RandomPeopleProfileViewModel(randomPeopleProfileMediator)

        assertThat(viewModel.randomPeopleProfileStateFlow.value, instanceOf(RandomPeopleProfileState.Initial::class.java))
        viewModel.getUserById("test_user_id")

        scheduler.advanceUntilIdle()

        val userResponse = viewModel.randomPeopleProfileStateFlow.value
        assertThat(userResponse, instanceOf(RandomPeopleProfileState.Error::class.java))
        val userError = (userResponse as RandomPeopleProfileState.Error)
        assertNull(userError.user)
    }
}