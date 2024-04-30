package com.paint.randompeoplek.repository.model

data class Location(
    val street : Street,
    val city : String,
    val state: String,
    val country : String,
    val postCode: String
)