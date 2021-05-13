package com.paint.randompeoplek.repository

import com.paint.randompeoplek.anyObject
import com.paint.randompeoplek.service.RandomPeopleService
import com.paint.randompeoplek.service.model.UserResponse
import com.paint.randompeoplek.storage.dao.UserDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import java.util.*


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RandomPeopleListRepositoryTest {

    private lateinit var randomPeopleService: RandomPeopleService
    private lateinit var userDao: UserDao

    private lateinit var randomPeopleListRepository: RandomPeopleListRepository

    @Before
    fun setUp() {
        randomPeopleService = mock(RandomPeopleService::class.java)
        userDao = mock(UserDao::class.java)

        randomPeopleListRepository = RandomPeopleListRepository(randomPeopleService, userDao)
    }

    @Test
    fun testGetUserListSuccessWhenFreshData() = runBlockingTest {
        val user = getTestEntityUser()

        `when`(userDao.hasUser(anyObject(Date::class.java))).thenReturn(1)
        `when`(userDao.getAll()).thenReturn(listOf(user))

        val userResponse = randomPeopleListRepository.getUserList("1")

        assertEquals(1, userResponse.users.size)
        assertNull(userResponse.throwable)
    }

    @Test
    fun testGetUserListSuccessWhenStaleData() = runBlockingTest {
        val user = getTestEntityUser()

        `when`(userDao.hasUser(anyObject(Date::class.java))).thenReturn(0)
        `when`(randomPeopleService.getUserList(anyObject(String::class.java))).thenReturn(UserResponse(listOf()))
        `when`(userDao.deleteAll()).thenReturn(Unit)
        `when`(userDao.insertAll(listOf(user))).thenReturn(Unit)
        `when`(userDao.getAll()).thenReturn(listOf(user))

        val userResponse = randomPeopleListRepository.getUserList("1")

        assertEquals(1, userResponse.users.size)
        assertNull(userResponse.throwable)
    }

    @Test
    fun testGetUserListSuccessWhenServerError() = runBlockingTest {
        val user = getTestEntityUser()

        `when`(userDao.hasUser(anyObject(Date::class.java))).thenReturn(0)
        doAnswer {
            throw Exception()
        }.`when`(randomPeopleService).getUserList(anyObject(String::class.java))
        `when`(userDao.getAll()).thenReturn(listOf(user))

        val userResponse = randomPeopleListRepository.getUserList("1")

        assertEquals(1, userResponse.users.size)
        assertNotNull(userResponse.throwable)
    }

}