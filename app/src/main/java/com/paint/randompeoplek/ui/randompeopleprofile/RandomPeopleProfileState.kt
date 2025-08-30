package com.paint.randompeoplek.ui.randompeopleprofile

import com.paint.randompeoplek.ui.model.User

sealed class RandomPeopleProfileState {
    object Initial : RandomPeopleProfileState()
    class Success(val user: User) : RandomPeopleProfileState()
    class Error(val user: User? = null) : RandomPeopleProfileState()
}