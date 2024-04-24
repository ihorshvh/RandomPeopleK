package com.paint.randompeoplek.domain

import com.paint.randompeoplek.domain.model.toMediatorUser
import com.paint.randompeoplek.repository.RandomPeopleListRepository
import javax.inject.Inject

class RandomPeopleProfileUseCase @Inject constructor(private val randomPeopleListRepository: RandomPeopleListRepository) {
    suspend fun getUserById(userId: String) = randomPeopleListRepository.getUserById(userId).toMediatorUser()
}