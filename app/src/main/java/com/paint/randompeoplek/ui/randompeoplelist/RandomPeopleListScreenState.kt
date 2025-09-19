package com.paint.randompeoplek.ui.randompeoplelist

import com.paint.randompeoplek.ui.model.User

data class RandomPeopleListScreenState(
    val isSearchVisible: Boolean = false,
    val searchText: String = "",
    val users: List<User>? = null,
)