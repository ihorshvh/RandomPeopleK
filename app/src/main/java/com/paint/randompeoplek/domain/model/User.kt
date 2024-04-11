package com.paint.randompeoplek.domain.model

data class User(
    val id: String,
    val name: Name,
    val location: String,
    val email: String,
    val phone: String,
    val picture: Picture
)