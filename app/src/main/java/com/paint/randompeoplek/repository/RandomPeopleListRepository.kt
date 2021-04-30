package com.paint.randompeoplek.repository

import com.paint.randompeoplek.service.RandomPeopleService

class RandomPeopleListRepository(private val randomPeopleService : RandomPeopleService) {

    suspend fun getUserList(userQuantity: String) = randomPeopleService.getUserList(userQuantity)

}