package com.paint.randompeoplek.mediator

import com.paint.randompeoplek.repository.RandomPeopleListRepository
import com.paint.randompeoplek.repository.model.*
import javax.inject.Inject

class RandomPeopleListMediator @Inject constructor(private val randomPeopleListRepository : RandomPeopleListRepository) {

    suspend fun getUserList(userQuantity: String) =
        randomPeopleListRepository.getUserList(userQuantity).toMediatorUserResponse()

}

private fun Name.toMediatorName() =
    com.paint.randompeoplek.mediator.model.Name(this.firstName + " " + this.lastName,
        this.title + " " + this.firstName + " " + this.lastName)

private fun Picture.toMediatorPicture() =
    com.paint.randompeoplek.mediator.model.Picture(this.medium, this.thumbnail)

private fun User.toMediatorUser() = com.paint.randompeoplek.mediator.model.User(this.name.toMediatorName(),
    this.location.getAddress(), this.email, this.phone, this.picture.toMediatorPicture())

private fun List<User>.toRepositoryUsers() = this.map { it.toMediatorUser() }

private fun UserResponse.toMediatorUserResponse() : com.paint.randompeoplek.mediator.model.UserResponse {
    return if(this.throwable != null) {
        com.paint.randompeoplek.mediator.model.UserResponse(this.users.toRepositoryUsers(), this.throwable!!)
    } else {
        com.paint.randompeoplek.mediator.model.UserResponse(this.users.toRepositoryUsers())
    }
}

private fun Location.getAddress() : String {
    val sb : StringBuilder = StringBuilder()

    return sb.append(this.street.number).append(" ")
        .append(this.street.name).append(", ")
        .append(this.city).append(", ")
        .append(this.state).append(", ")
        .append(this.country).append(", ")
        .append(this.postCode).toString()
}