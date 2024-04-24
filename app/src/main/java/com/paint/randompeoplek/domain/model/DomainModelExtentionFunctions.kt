package com.paint.randompeoplek.domain.model

import com.paint.randompeoplek.repository.model.Location
import com.paint.randompeoplek.repository.model.Name
import com.paint.randompeoplek.repository.model.Picture
import com.paint.randompeoplek.repository.model.User
import com.paint.randompeoplek.repository.model.UserResponse

private fun Name.toMediatorName() =
    com.paint.randompeoplek.domain.model.Name(
        this.firstName + " " + this.lastName,
        this.title + " " + this.firstName + " " + this.lastName
    )

private fun Picture.toMediatorPicture() =
    com.paint.randompeoplek.domain.model.Picture(this.medium, this.thumbnail)

fun User.toMediatorUser() = com.paint.randompeoplek.domain.model.User(
    this.id,
    this.name.toMediatorName(),
    this.location.getAddress(),
    this.email,
    this.phone,
    this.picture.toMediatorPicture()
)

private fun List<User>.toRepositoryUsers() = this.map { it.toMediatorUser() }

fun UserResponse.toMediatorUserResponse(): com.paint.randompeoplek.domain.model.UserResponse {
    return if (this.throwable != null) {
        com.paint.randompeoplek.domain.model.UserResponse(this.users.toRepositoryUsers(), this.throwable)
    } else {
        com.paint.randompeoplek.domain.model.UserResponse(this.users.toRepositoryUsers())
    }
}

private fun Location.getAddress(): String {
    val sb: StringBuilder = StringBuilder()

    return sb.append(this.street.number).append(" ")
        .append(this.street.name).append(", ")
        .append(this.city).append(", ")
        .append(this.state).append(", ")
        .append(this.country).append(", ")
        .append(this.postCode).toString()
}