package com.paint.randompeoplek.ui.randompeoplelist

sealed interface RandomPeopleListAction {
    data class OnSearchTextChange(val newSearchText: String): RandomPeopleListAction
    data object OnSearchButtonClick: RandomPeopleListAction
    data object OnCloseSearchButtonClick: RandomPeopleListAction
    data object OnRefreshClick: RandomPeopleListAction
}