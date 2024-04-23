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
    const val userIdArg = "user_id"
    val routeWithArgs = "${route}/{${userIdArg}}"
    val arguments = listOf(navArgument(userIdArg) { type = NavType.StringType })
}