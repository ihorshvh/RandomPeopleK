package com.paint.randompeoplek.repository

import com.paint.randompeoplek.service.RandomPeopleService
import com.paint.randompeoplek.service.model.UserResponse
import com.paint.randompeoplek.storage.dao.UserDao
import io.mockk.*
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import retrofit2.Response

class RandomPeopleListRepositoryTest {

    private lateinit var randomPeopleService: RandomPeopleService
    private lateinit var userDao: UserDao

    private lateinit var randomPeopleListRepository: RandomPeopleListRepository

    @BeforeEach
    fun setUp() {
        randomPeopleService = mockk()
        userDao = mockk()

        randomPeopleListRepository = RandomPeopleListRepository(randomPeopleService, userDao)
    }

    @AfterEach
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
        assertNull(userResponse.networkError)
    }

    @Test
    fun testGetUserListSuccessWhenStaleData() = runTest {
        val user = getTestEntityUser()

        coEvery { userDao.hasUser(any()) } answers { 0 }
        coEvery { randomPeopleService.getUserList(any()) } answers {
            Response.success(200, UserResponse(listOf()))
        }

        coEvery { userDao.deleteAll() } just Runs
        coEvery { userDao.insertAll(any()) } returns Unit
        coEvery { userDao.getAll() } answers { listOf(user) }

        val userResponse = randomPeopleListRepository.getUserList("1")

        assertEquals(1, userResponse.users.size)
        assertNull(userResponse.networkError)
    }

    @Test
    fun testGetUserListSuccessWhenNoData() = runTest {
        val user = getTestEntityUser()

        coEvery { userDao.hasUser(any()) } answers { 0 }
        coEvery { randomPeopleService.getUserList(any()) } answers {
            val response: UserResponse? = null
            Response.success(200, response)
        }
        coEvery { userDao.getAll() } answers { listOf(user) }

        val userResponse = randomPeopleListRepository.getUserList("1")

        assertEquals(1, userResponse.users.size)
        assertEquals(NetworkError.UNKNOWN, userResponse.networkError)
    }

    @ParameterizedTest
    @CsvSource(
        "401, UNAUTHORIZED",
        "408, REQUEST_TIMEOUT",
        "500, SERVER_ERROR",
        "601, UNKNOWN"
    )
    fun testGetUserListSuccessWhenErrorCode(statusCode: Int, networkError: NetworkError) = runTest {
        val user = getTestEntityUser()

        coEvery { userDao.hasUser(any()) } answers { 0 }
        coEvery { randomPeopleService.getUserList(any()) } answers {
            Response.error(statusCode, "".toResponseBody(null))
        }
        coEvery { userDao.getAll() } answers { listOf(user) }

        val userResponse = randomPeopleListRepository.getUserList("1")

        assertEquals(1, userResponse.users.size)
        assertEquals(networkError, userResponse.networkError)
    }

    @Test
    fun testGetUserListSuccessWhenServerError() = runTest {
        val user = getTestEntityUser()

        coEvery { userDao.hasUser(any()) } answers { 0 }
        coEvery { randomPeopleService.getUserList(any()) } answers { throw Exception() }
        coEvery { userDao.getAll() } answers { listOf(user) }

        val userResponse = randomPeopleListRepository.getUserList("1")

        assertEquals(1, userResponse.users.size)
        assertNotNull(userResponse.networkError)
        assertEquals(NetworkError.NO_INTERNET, userResponse.networkError)
    }

    @Test
    fun testGetUserById() = runTest {
        val user = getTestEntityUser()

        coEvery { userDao.getUserById("test_user_id") } answers { user }

        val returnedUser = randomPeopleListRepository.getUserById("test_user_id")

        assertNotNull(returnedUser)
        assertNotNull(returnedUser.id)
        assertEquals("test_user_id", returnedUser.id)
    }
}