package com.paint.randompeoplek.mediator

import com.paint.randompeoplek.mediator.model.*


fun Name.toUiParcelableName() =
    com.paint.randompeoplek.ui.model.Name(this.shortName, this.fullName)

fun Picture.toUiParcelablePicture() =
    com.paint.randompeoplek.ui.model.Picture(this.medium, this.thumbnail)

fun User.toUiParcelableUser() =
    com.paint.randompeoplek.ui.model.User(this.name.toUiParcelableName(),
        this.location,
        this.email,
        this.phone,
        this.picture.toUiParcelablePicture())

fun List<User>.toUiParcelableUsers() = this.map { it.toUiParcelableUser() }