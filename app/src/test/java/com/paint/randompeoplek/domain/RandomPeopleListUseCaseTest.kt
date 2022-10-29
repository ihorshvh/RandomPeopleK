package com.paint.randompeoplek.domain

import com.paint.randompeoplek.repository.RandomPeopleListRepository
import com.paint.randompeoplek.repository.model.UserResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RandomPeopleListUseCaseTest {

    @Test
    fun testGetUserListWhenSuccess() = runTest {
        val users = getUsers()

        val randomPeopleListRepository = mockk<RandomPeopleListRepository>()

        coEvery { randomPeopleListRepository.getUserList("10") } answers {
            UserResponse(users)
        }

        val randomPeopleListUseCase = RandomPeopleListUseCase(randomPeopleListRepository)

        val userResponse = randomPeopleListUseCase.getUserList("10")

        assertNotNull(userResponse)
        assertNotNull(userResponse.users)
        assertEquals(2, userResponse.users.size)
    }

    @Test(expected = Exception::class)
    fun testGetUserListWhenFailure() = runTest {
        val randomPeopleListRepository = mockk<RandomPeopleListRepository>()

        coEvery { randomPeopleListRepository.getUserList("10") } answers {
            throw Exception()
        }

        val randomPeopleListUseCase = RandomPeopleListUseCase(randomPeopleListRepository)
        randomPeopleListUseCase.getUserList("10")

        fail()
    }
}