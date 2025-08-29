package com.paint.randompeoplek

import androidx.activity.ComponentActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paint.randompeoplek.navigation.RandomPeopleListScreen
import com.paint.randompeoplek.navigation.RandomPeopleProfile
import com.paint.randompeoplek.ui.randompeoplelist.RandomPeopleListScreen
import com.paint.randompeoplek.ui.randompeopleprofile.UserProfileScreen
import com.paint.randompeoplek.ui.theme.RandomPeopleKTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomPeopleMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        setContent {
            RandomPeopleApp()
        }
    }
}

@Composable
fun RandomPeopleApp() {
    RandomPeopleKTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = RandomPeopleListScreen.route
        ) {
            composable(route = RandomPeopleListScreen.route) {
                RandomPeopleListScreen {
                    user -> navController.navigateToProfile(user.id)
                }
            }
            composable(
                route = RandomPeopleProfile.routeWithArgs,
                arguments = RandomPeopleProfile.arguments
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString(RandomPeopleProfile.userIdArg).orEmpty()
                UserProfileScreen(userId = userId) { navController.navigateSingleTopTo(RandomPeopleListScreen.route) }
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