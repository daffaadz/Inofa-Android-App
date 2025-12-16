package com.example.inofa_android_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inofa_android_app.auth.SignInScreen
import com.example.inofa_android_app.auth.SignUpScreen
import com.example.inofa_android_app.ui.screens.ProfileSetupScreen
import com.example.inofa_android_app.ui.screens.ClientProfileSetupScreen
import com.example.inofa_android_app.ui.screens.ProjectCreateScreen
import com.example.inofa_android_app.ui.screens.PortfolioManageScreen
import com.example.inofa_android_app.ui.screens.PortfolioAddScreen
import com.example.inofa_android_app.ui.screens.DeveloperProfileViewScreen
import com.example.inofa_android_app.ui.screens.DeveloperProfileEditScreen
import com.example.inofa_android_app.ui.screens.ClientProfileViewScreen
import com.example.inofa_android_app.ui.screens.ClientProfileEditScreen
import com.example.inofa_android_app.ui.screens.ProjectDetailScreen
import com.example.inofa_android_app.ui.screens.ProjectEditScreen
import com.example.inofa_android_app.ui.screens.ProjectViewScreen
import com.example.inofa_android_app.developer_profile.DeveloperProfileScreen
import com.example.inofa_android_app.discover.DiscoverScreen
import com.example.inofa_android_app.home.HomeScreen
import com.example.inofa_android_app.profile.ProfileScreen
import com.example.inofa_android_app.ui.viewmodel.NextStep

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
                onNavigateHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateProfileSetup = {
                    val isDeveloper = com.example.inofa_android_app.data.UserRoleStorage.isDeveloper()
                    val route = if (isDeveloper) Screen.ProfileSetup.route else Screen.ClientProfileSetup.route
                    navController.navigate(route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateRole = {
                    navController.navigate(Screen.SignUp.route) {
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
                onNavigateHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateProfileSetup = {
                    val isDeveloper = com.example.inofa_android_app.data.UserRoleStorage.isDeveloper()
                    val route = if (isDeveloper) Screen.ProfileSetup.route else Screen.ClientProfileSetup.route
                    navController.navigate(route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateRole = {
                    // stay on SignUp if role missing
                },
                onSignInClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                onDone = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ClientProfileSetup.route) {
            ClientProfileSetupScreen(
                onDone = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.ClientProfileSetup.route) { inclusive = true }
                    }
                }
            )
        }

        // Main Screens
        composable(Screen.Home.route) {
            HomeScreen(
                onDeveloperClick = { developerId ->
                    navController.navigate(Screen.DeveloperProfile.createRoute(developerId))
                },
                onProjectClick = { projectId ->
                    navController.navigate(Screen.ProjectView.createRoute(projectId))
                },
                onNavigateToDiscover = {
                    navController.navigate(Screen.Discover.route)
                },
                onNavigateToMessages = {
                    navController.navigate(Screen.Messages.route)
                },
                onNavigateToProjects = {
                    navController.navigate(Screen.ProjectCreate.route)
                },
                onNavigateToPortfolio = {
                    navController.navigate(Screen.PortfolioManage.route)
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
                onProjectClick = { projectId ->
                    navController.navigate(Screen.ProjectView.createRoute(projectId))
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToMessages = {
                    navController.navigate(Screen.Messages.route)
                },
                onNavigateToProjects = {
                    navController.navigate(Screen.ProjectCreate.route)
                },
                onNavigateToPortfolio = {
                    navController.navigate(Screen.PortfolioManage.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.Profile.route) {
            val isDeveloper = com.example.inofa_android_app.data.UserRoleStorage.isDeveloper()
            
            if (isDeveloper) {
                DeveloperProfileViewScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onNavigateToDiscover = {
                        navController.navigate(Screen.Discover.route)
                    },
                    onNavigateToPortfolio = {
                        navController.navigate(Screen.PortfolioManage.route)
                    },
                    onEditProfile = {
                        navController.navigate(Screen.DeveloperProfileEdit.route)
                    },
                    onManagePortfolio = {
                        navController.navigate(Screen.PortfolioManage.route)
                    },
                    onLogout = {
                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            } else {
                ClientProfileViewScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onNavigateToDiscover = {
                        navController.navigate(Screen.Discover.route)
                    },
                    onNavigateToProjects = {
                        navController.navigate(Screen.ProjectCreate.route)
                    },
                    onEditProfile = {
                        navController.navigate(Screen.ClientProfileEdit.route)
                    },
                    onNavigateToProjectDetail = { projectId ->
                        navController.navigate(Screen.ProjectDetail.createRoute(projectId))
                    },
                    onLogout = {
                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.DeveloperProfile.route) { backStackEntry ->
            val developerId = backStackEntry.arguments?.getString("developerId")?.toIntOrNull() ?: 1
            DeveloperProfileScreen(
                developerId = developerId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.ProjectCreate.route) {
            ProjectCreateScreen(
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(Screen.PortfolioManage.route) {
            PortfolioManageScreen(
                onBackClick = { navController.popBackStack() },
                onAddClick = { navController.navigate(Screen.PortfolioAdd.route) },
                onEditClick = { portfolioId ->
                    navController.navigate(Screen.PortfolioEdit.createRoute(portfolioId))
                }
            )
        }

        composable(Screen.PortfolioAdd.route) {
            PortfolioAddScreen(
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(Screen.PortfolioEdit.route) { backStackEntry ->
            val portfolioId = backStackEntry.arguments?.getString("portfolioId")?.toIntOrNull()
            if (portfolioId != null) {
                PortfolioAddScreen(
                    portfolioId = portfolioId,
                    onBackClick = { navController.popBackStack() },
                    onSuccess = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.DeveloperProfileEdit.route) {
            DeveloperProfileEditScreen(
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ClientProfileEdit.route) {
            ClientProfileEditScreen(
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ProjectDetail.route) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull() ?: 0
            ProjectDetailScreen(
                projectId = projectId,
                onBackClick = { navController.popBackStack() },
                onEditProject = { id ->
                    navController.navigate(Screen.ProjectEdit.createRoute(id))
                }
            )
        }

        composable(Screen.ProjectEdit.route) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull() ?: 0
            ProjectEditScreen(
                projectId = projectId,
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(Screen.ProjectView.route) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull() ?: 0
            ProjectViewScreen(
                projectId = projectId,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Placeholder for Messages
        composable(Screen.Messages.route) {
            // TODO: Implement Messages Screen
        }
    }
}