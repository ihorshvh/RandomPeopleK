package com.paint.randompeoplek.service

import com.paint.randompeoplek.service.model.*
import java.util.*

fun Location.toStorageLocation() =
    com.paint.randompeoplek.storage.entity.Location(this.street.toStorageStreet(),
        this.city,
        this.state,
        this.country,
        this.postCode)

fun Name.toStorageName() =
    com.paint.randompeoplek.storage.entity.Name(this.title, this.firstName, this.lastName)

fun Picture.toStoragePicture() =
    com.paint.randompeoplek.storage.entity.Picture(this.large, this.medium, this.thumbnail)

fun Street.toStorageStreet() =
    com.paint.randompeoplek.storage.entity.Street(this.number, this.name)

fun User.toStorageUser() =
    com.paint.randompeoplek.storage.entity.User(this.name.toStorageName(),
        this.location.toStorageLocation(),
        this.email,
        this.phone,
        this.picture.toStoragePicture(),
        Calendar.getInstance().time)

fun List<User>.toStorageUsers() = this.map { it.toStorageUser() }