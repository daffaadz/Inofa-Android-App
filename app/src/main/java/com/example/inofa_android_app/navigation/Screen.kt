package com.example.inofa_android_app.navigation

sealed class Screen(val route: String) {
    data object SignIn : Screen("sign_in")
    data object SignUp : Screen("sign_up")
    data object Home : Screen("home")
    data object Discover : Screen("discover")
    data object Messages : Screen("messages")
    data object Profile : Screen("profile")
    data object DeveloperProfile : Screen("developer_profile/{developerId}") {
        fun createRoute(developerId: Int) = "developer_profile/$developerId"
    }
}
