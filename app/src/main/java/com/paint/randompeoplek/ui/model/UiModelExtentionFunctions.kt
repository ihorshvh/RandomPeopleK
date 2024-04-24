package com.paint.randompeoplek.ui.model

import com.paint.randompeoplek.domain.model.Name
import com.paint.randompeoplek.domain.model.Picture

private fun Name.toUiParcelableName() =
    com.paint.randompeoplek.ui.model.Name(this.shortName, this.fullName)

private fun Picture.toUiParcelablePicture() =
    com.paint.randompeoplek.ui.model.Picture(this.medium, this.thumbnail)

fun com.paint.randompeoplek.domain.model.User.toUiParcelableUser() =
    User(
        this.id,
        this.name.toUiParcelableName(),
        this.location,
        this.email,
        this.phone,
        this.picture.toUiParcelablePicture()
    )

fun List<com.paint.randompeoplek.domain.model.User>.toUiParcelableUsers() = this.map { it.toUiParcelableUser() }