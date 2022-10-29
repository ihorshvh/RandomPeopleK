package com.paint.randompeoplek.repository

import com.paint.randompeoplek.service.RandomPeopleService
import com.paint.randompeoplek.service.model.UserResponse
import com.paint.randompeoplek.storage.dao.UserDao
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RandomPeopleListRepositoryTest {

    private lateinit var randomPeopleService: RandomPeopleService
    private lateinit var userDao: UserDao

    private lateinit var randomPeopleListRepository: RandomPeopleListRepository

    @Before
    fun setUp() {
        randomPeopleService = mockk()
        userDao = mockk()

        randomPeopleListRepository = RandomPeopleListRepository(randomPeopleService, userDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun testGetUserListSuccessWhenFreshData() = runTest {
        val user = getTestEntityUser()

        coEvery { userDao.hasUser(any()) } answers { 1 }
        coEvery { userDao.getAll() } answers { listOf(user) }

        val userResponse = randomPeopleListRepository.getUserList("1")

        assertEquals(1, userResponse.users.size)
        assertNull(userResponse.throwable)
    }

    @Test
    fun testGetUserListSuccessWhenStaleData() = runTest {
        val user = getTestEntityUser()

        coEvery { userDao.hasUser(any()) } answers { 0 }
        coEvery { randomPeopleService.getUserList(any()) } answers {
            UserResponse(listOf())
        }

        coEvery { userDao.deleteAll() } just Runs
        coEvery { userDao.insertAll(any()) } returns Unit
        coEvery { userDao.getAll() } answers { listOf(user) }

        val userResponse = randomPeopleListRepository.getUserList("1")

        assertEquals(1, userResponse.users.size)
        assertNull(userResponse.throwable)
    }

    @Test
    fun testGetUserListSuccessWhenServerError() = runTest {
        val user = getTestEntityUser()

        coEvery { userDao.hasUser(any()) } answers { 0 }
        coEvery { randomPeopleService.getUserList(any()) } answers { throw Exception() }
        coEvery { userDao.getAll() } answers { listOf(user) }

        val userResponse = randomPeopleListRepository.getUserList("1")

        assertEquals(1, userResponse.users.size)
        assertNotNull(userResponse.throwable)
    }

}