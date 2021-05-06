package com.paint.randompeoplek.repository

import com.paint.randompeoplek.service.RandomPeopleService
import com.paint.randompeoplek.repository.model.UserResponse
import com.paint.randompeoplek.service.toStorageUsers
import com.paint.randompeoplek.storage.dao.UserDao
import com.paint.randompeoplek.storage.toRepositoryUsers
import java.util.Date
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RandomPeopleListRepository @Inject constructor(
    private val randomPeopleService: RandomPeopleService,
    private val userDao: UserDao
) {

    suspend fun getUserList(userQuantity: String) : UserResponse {

        val freshUsersNumber = userDao.hasUser(getTimeout())

        if (freshUsersNumber <= 0) {
            try {
                val userResponse = randomPeopleService.getUserList(userQuantity)

                userDao.deleteAll()
                userDao.insertAll(userResponse.users.toStorageUsers())
            } catch (e : Exception) {
                return UserResponse(userDao.getAll().toRepositoryUsers(), e)
            }
        }

        return UserResponse(userDao.getAll().toRepositoryUsers())
    }

    companion object {

        private val FRESH_TIMEOUT = TimeUnit.SECONDS.toMillis(15)

        fun getTimeout() = Date(Calendar.getInstance().time.time - FRESH_TIMEOUT)

    }

}