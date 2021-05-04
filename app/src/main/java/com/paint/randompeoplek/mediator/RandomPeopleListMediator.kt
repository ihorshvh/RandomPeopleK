package com.paint.randompeoplek.mediator

import com.paint.randompeoplek.repository.RandomPeopleListRepository
import com.paint.randompeoplek.repository.toMediatorUserResponse
import javax.inject.Inject

class RandomPeopleListMediator @Inject constructor(private val randomPeopleListRepository : RandomPeopleListRepository) {

    suspend fun getUserList(userQuantity: String) =
        randomPeopleListRepository.getUserList(userQuantity).toMediatorUserResponse()

}