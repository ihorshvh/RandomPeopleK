package com.paint.randompeoplek.domain

fun getUsers(): List<com.paint.randompeoplek.repository.model.User> {
    return listOf(getUser(), getUser())
}

fun getUser(): com.paint.randompeoplek.repository.model.User {
    val name = com.paint.randompeoplek.repository.model.Name("Mr", "Ryan Wilson", "Mr Ryan Wilson")
    return com.paint.randompeoplek.repository.model.User(
        "test_user_id",
        name,
        com.paint.randompeoplek.repository.model.Location(com.paint.randompeoplek.repository.model.Street(0, ""), "", "", "", ""),
        "",
        "",
        com.paint.randompeoplek.repository.model.Picture("", "", "")
    )
}