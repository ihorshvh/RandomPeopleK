package com.paint.randompeoplek.ui.randompeoplelist

import com.paint.randompeoplek.ui.model.User

data class RandomPeopleListScreenState(
    val isSearchVisible: Boolean = false,
    val searchText: String = "",
    val randomPeopleListState: RandomPeopleListState
)

sealed class RandomPeopleListState {
    object Initial : RandomPeopleListState()
    class Success(val users: List<User>) : RandomPeopleListState()
    class Error(val users: List<User>? = null) : RandomPeopleListState()
}