package com.paint.randompeoplek.repository

import com.paint.randompeoplek.service.RandomPeopleService
import com.paint.randompeoplek.service.toRepositoryUserResponse
import javax.inject.Inject

class RandomPeopleListRepository @Inject constructor (private val randomPeopleService : RandomPeopleService) {

    suspend fun getUserList(userQuantity: String) =
        randomPeopleService.getUserList(userQuantity).toRepositoryUserResponse()

}