package com.example.inofa_android_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inofa_android_app.auth.SignInScreen
import com.example.inofa_android_app.auth.SignUpScreen
import com.example.inofa_android_app.developer_profile.DeveloperProfileScreen
import com.example.inofa_android_app.discover.DiscoverScreen
import com.example.inofa_android_app.home.HomeScreen
import com.example.inofa_android_app.profile.ProfileScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.SignIn.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth Screens
        composable(Screen.SignIn.route) {
            SignInScreen(
                onSignInClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onSignInClick = {
                    navController.popBackStack()
                }
            )
        }

        // Main Screens
        composable(Screen.Home.route) {
            HomeScreen(
                onDeveloperClick = { developerId ->
                    navController.navigate(Screen.DeveloperProfile.createRoute(developerId))
                },
                onNavigateToDiscover = {
                    navController.navigate(Screen.Discover.route)
                },
                onNavigateToMessages = {
                    navController.navigate(Screen.Messages.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.Discover.route) {
            DiscoverScreen(
                onDeveloperClick = { developerId ->
                    navController.navigate(Screen.DeveloperProfile.createRoute(developerId))
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToMessages = {
                    navController.navigate(Screen.Messages.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToDiscover = {
                    navController.navigate(Screen.Discover.route)
                },
                onNavigateToMessages = {
                    navController.navigate(Screen.Messages.route)
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

        // Placeholder for Messages
        composable(Screen.Messages.route) {
            // TODO: Implement Messages Screen
        }
    }
}