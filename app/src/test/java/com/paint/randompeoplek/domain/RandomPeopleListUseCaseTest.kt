package com.paint.randompeoplek.domain

import com.paint.randompeoplek.repository.RandomPeopleListRepository
import com.paint.randompeoplek.repository.model.UserResponse
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RandomPeopleListUseCaseTest {

    @Test
    fun testGetUserListWhenSuccess() {
        val users = getUsers()

        val randomPeopleListRepository = mock(RandomPeopleListRepository::class.java)
        val randomPeopleListUseCase = RandomPeopleListUseCase(randomPeopleListRepository)
        runBlockingTest {
            `when`(randomPeopleListRepository.getUserList("10")).thenReturn(UserResponse(users))

            val userResponse = randomPeopleListUseCase.getUserList("10")

            assertNotNull(userResponse)
            assertNotNull(userResponse.users)
            assertEquals(2, userResponse.users.size)
        }
    }

    @Test(expected = Exception::class)
    fun testGetUserListWhenFailure() {
        val randomPeopleListRepository = mock(RandomPeopleListRepository::class.java)
        val randomPeopleListUseCase = RandomPeopleListUseCase(randomPeopleListRepository)
        
        runBlockingTest {
            Mockito.doAnswer {
                throw Exception()
            }.`when`(randomPeopleListRepository).getUserList("10")

            randomPeopleListUseCase.getUserList("10")

            fail()
        }
    }
}