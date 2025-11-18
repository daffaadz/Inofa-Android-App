package com.example.inofa_android_app.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object DeveloperProfile : Screen("developer_profile/{developerId}") {
        fun createRoute(developerId: Int) = "developer_profile/$developerId"
    }
}
