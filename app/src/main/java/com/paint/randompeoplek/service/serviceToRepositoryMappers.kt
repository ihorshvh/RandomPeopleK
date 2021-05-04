package com.paint.randompeoplek.service

import com.paint.randompeoplek.service.model.*

fun Location.toRepositoryLocation() =
    com.paint.randompeoplek.repository.model.Location(this.street.toRepositoryStreet(),
        this.city,
        this.state,
        this.country,
        this.postCode)

fun Name.toRepositoryName() =
    com.paint.randompeoplek.repository.model.Name(this.title, this.firstName, this.lastName)

fun Picture.toRepositoryPicture() =
    com.paint.randompeoplek.repository.model.Picture(this.large, this.medium, this.thumbnail)

fun Street.toRepositoryStreet() =
    com.paint.randompeoplek.repository.model.Street(this.number, this.name)

fun User.toRepositoryUser() =
    com.paint.randompeoplek.repository.model.User(this.name.toRepositoryName(),
        this.location.toRepositoryLocation(),
        this.email,
        this.phone,
        this.picture.toRepositoryPicture())

fun List<User>.toRepositoryUsers() = this.map { it.toRepositoryUser() }

fun UserResponse.toRepositoryUserResponse() =
    com.paint.randompeoplek.repository.model.UserResponse(this.users.toRepositoryUsers())