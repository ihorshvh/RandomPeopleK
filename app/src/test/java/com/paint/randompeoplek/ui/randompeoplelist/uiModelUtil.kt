package com.paint.randompeoplek.ui.randompeoplelist

fun getUsers(): List<com.paint.randompeoplek.usecase.model.User> {
    return listOf(getUser(), getUser())
}

fun getUser(): com.paint.randompeoplek.usecase.model.User {
    val name = com.paint.randompeoplek.usecase.model.Name("Ryan Wilson", "Mr Ryan Wilson")
    return com.paint.randompeoplek.usecase.model.User(
        name,
        "740 Lambie Drive Invercargill Bay of Plenty New Zealand 32336",
        "",
        "",
        com.paint.randompeoplek.usecase.model.Picture("", "")
    )
}