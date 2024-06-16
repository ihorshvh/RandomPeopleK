package com.paint.randompeoplek.domain

import com.paint.randompeoplek.repository.RandomPeopleListRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RandomPeopleProfileUseCaseTest {

    @Test
    fun testGetUserById() = runTest {
        val user = getUser()

        val randomPeopleListRepository = mockk<RandomPeopleListRepository>()

        coEvery { randomPeopleListRepository.getUserById("test_user_id") } answers {
            user
        }

        val randomPeopleProfileUseCase = RandomPeopleProfileUseCase(randomPeopleListRepository)

        val returnedUser = randomPeopleProfileUseCase.getUserById("test_user_id")

        TestCase.assertNotNull(returnedUser)
        TestCase.assertNotNull(returnedUser.id)
        TestCase.assertEquals("test_user_id", returnedUser.id)
    }
}