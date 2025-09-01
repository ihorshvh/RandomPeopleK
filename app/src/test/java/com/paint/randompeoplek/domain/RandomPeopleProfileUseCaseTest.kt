package com.paint.randompeoplek.domain

import com.paint.randompeoplek.repository.RandomPeopleListRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

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

        assertNotNull(returnedUser)
        assertNotNull(returnedUser.id)
        assertEquals("test_user_id", returnedUser.id)
    }
}