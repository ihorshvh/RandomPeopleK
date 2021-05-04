package com.paint.randompeoplek.repository

import com.paint.randompeoplek.repository.model.*

fun Name.toMediatorName() =
    com.paint.randompeoplek.mediator.model.Name(this.firstName + " " + this.lastName,
        this.title + " " + this.firstName + " " + this.lastName)

fun Picture.toMediatorPicture() =
    com.paint.randompeoplek.mediator.model.Picture(this.medium, this.thumbnail)

fun User.toMediatorUser() = com.paint.randompeoplek.mediator.model.User(this.name.toMediatorName(),
    this.location.getAddress(), this.email, this.phone, this.picture.toMediatorPicture())

fun List<User>.toRepositoryUsers() = this.map { it.toMediatorUser() }

fun UserResponse.toMediatorUserResponse() =
    com.paint.randompeoplek.mediator.model.UserResponse(this.users.toRepositoryUsers())

fun Location.getAddress() : String {
    val sb : StringBuilder = StringBuilder()

    return sb.append(this.street.number).append(" ")
        .append(this.street.name).append(", ")
        .append(this.city).append(", ")
        .append(this.state).append(", ")
        .append(this.country).append(", ")
        .append(this.postCode).toString()
}