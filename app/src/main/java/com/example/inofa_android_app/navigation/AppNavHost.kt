package com.example.inofa_android_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inofa_android_app.developer_profile.DeveloperProfileScreen
import com.example.inofa_android_app.home.HomeScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onDeveloperClick = { developerId ->
                    navController.navigate(Screen.DeveloperProfile.createRoute(developerId))
                }
            )
        }
        composable(Screen.DeveloperProfile.route) { backStackEntry ->
            val developerId = backStackEntry.arguments?.getString("developerId")?.toIntOrNull() ?: 1
            DeveloperProfileScreen(
                developerId = developerId,
                onBackClick = { navController.popBackStack() }
            )
        }
        // Add other screens here (e.g., Discover, Messages, Profile placeholders)
    }
}