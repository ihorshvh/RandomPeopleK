package com.paint.randompeoplek.domain

import com.paint.randompeoplek.repository.RandomPeopleListRepository
import com.paint.randompeoplek.repository.model.UserResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

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
}