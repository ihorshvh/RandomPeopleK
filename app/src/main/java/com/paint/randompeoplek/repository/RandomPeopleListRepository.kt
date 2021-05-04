package com.paint.randompeoplek.repository

import com.paint.randompeoplek.service.RandomPeopleService
import com.paint.randompeoplek.service.toRepositoryUserResponse
import com.paint.randompeoplek.storage.dao.UserDao
import javax.inject.Inject

class RandomPeopleListRepository @Inject constructor (
    private val randomPeopleService : RandomPeopleService,
    private val userDao: UserDao) {

    suspend fun getUserList(userQuantity: String) =
        randomPeopleService.getUserList(userQuantity).toRepositoryUserResponse()

}