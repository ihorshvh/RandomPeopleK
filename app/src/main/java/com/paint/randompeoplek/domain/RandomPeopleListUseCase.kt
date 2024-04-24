package com.paint.randompeoplek.domain

import com.paint.randompeoplek.domain.model.toMediatorUserResponse
import com.paint.randompeoplek.repository.RandomPeopleListRepository
import javax.inject.Inject

class RandomPeopleListUseCase @Inject constructor(private val randomPeopleListRepository: RandomPeopleListRepository) {
    suspend fun getUserList(userQuantity: String) = randomPeopleListRepository.getUserList(userQuantity).toMediatorUserResponse()
}