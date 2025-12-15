package com.example.inofa_android_app.navigation

sealed class Screen(val route: String) {
    data object SignIn : Screen("sign_in")
    data object SignUp : Screen("sign_up")
    data object Home : Screen("home")
    data object Discover : Screen("discover")
    data object Messages : Screen("messages")
    data object Profile : Screen("profile")
    data object ProfileSetup : Screen("profile_setup")
    data object ProjectCreate : Screen("project_create")
    data object PortfolioManage : Screen("portfolio_manage")
    data object PortfolioAdd : Screen("portfolio_add")
    data object PortfolioEdit : Screen("portfolio_edit/{portfolioId}") {
        fun createRoute(portfolioId: Int) = "portfolio_edit/$portfolioId"
    }
    data object DeveloperProfileEdit : Screen("developer_profile_edit")
    data object ClientProfileEdit : Screen("client_profile_edit")
    data object DeveloperProfile : Screen("developer_profile/{developerId}") {
        fun createRoute(developerId: Int) = "developer_profile/$developerId"
    }
}
