package com.paint.randompeoplek.ui.randompeoplelist

fun getUsers(): List<com.paint.randompeoplek.domain.model.User> {
    return listOf(getUser("test_id_1"), getUser("test_id_2"))
}

fun getUser(id: String): com.paint.randompeoplek.domain.model.User {
    val name = com.paint.randompeoplek.domain.model.Name("Ryan Wilson", "Mr Ryan Wilson")
    return com.paint.randompeoplek.domain.model.User(
        id,
        name,
        "740 Lambie Drive Invercargill Bay of Plenty New Zealand 32336",
        "",
        "",
        com.paint.randompeoplek.domain.model.Picture("", "")
    )
}