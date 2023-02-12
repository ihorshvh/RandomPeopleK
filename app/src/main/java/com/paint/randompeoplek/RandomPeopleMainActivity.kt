package com.paint.randompeoplek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paint.randompeoplek.navigation.RandomPeopleList
import com.paint.randompeoplek.navigation.RandomPeopleProfile
import com.paint.randompeoplek.ui.randompeoplelist.RandomPeopleListScreen
import com.paint.randompeoplek.ui.randompeoplelist.RandomPeopleListViewModel
import com.paint.randompeoplek.ui.randompeopleprofile.UserProfileScreen
import com.paint.randompeoplek.ui.theme.RandomPeopleKTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomPeopleMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[RandomPeopleListViewModel::class.java]

        setContent {
            RandomPeopleApp(viewModel)
        }
    }
}

@Composable
fun RandomPeopleApp(viewModel: RandomPeopleListViewModel) {
    RandomPeopleKTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = RandomPeopleList.route
        ) {
            composable(route = RandomPeopleList.route) {
                RandomPeopleListScreen(viewModel) { user -> navController.navigateToProfile(user.name.fullName) }
            }
            composable(
                route = RandomPeopleProfile.routeWithArgs,
                arguments = RandomPeopleProfile.arguments
            ) { backStackEntry ->
                val userName = backStackEntry.arguments?.getString(RandomPeopleProfile.userNameArg).orEmpty()
                UserProfileScreen(viewModel, userName) { navController.navigateSingleTopTo(RandomPeopleList.route) }
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }

private fun NavHostController.navigateToProfile(userName: String) {
    this.navigateSingleTopTo("${RandomPeopleProfile.route}/$userName")
}