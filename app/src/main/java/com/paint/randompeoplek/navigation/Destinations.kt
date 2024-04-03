package com.paint.randompeoplek.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument


interface Destination {
    val route: String
}

object RandomPeopleListScreen : Destination {
    override val route = "random_people_list"
}

object RandomPeopleProfile : Destination {
    override val route = "random_people_profile"
    const val userNameArg = "user_name"
    val routeWithArgs = "${route}/{${userNameArg}}"
    val arguments = listOf(navArgument(userNameArg) { type = NavType.StringType })
}