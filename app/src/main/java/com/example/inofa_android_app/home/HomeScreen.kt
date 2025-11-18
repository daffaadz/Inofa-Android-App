package com.example.inofa_android_app.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.inofa_android_app.data.mockCategories
import com.example.inofa_android_app.data.mockFeaturedDevelopers
import com.example.inofa_android_app.home.components.HomeBottomNavBar
import com.example.inofa_android_app.home.components.HomeCategoriesSection
import com.example.inofa_android_app.home.components.HomeDiscoverSection
import com.example.inofa_android_app.home.components.HomeFeaturedDevelopersSection
import com.example.inofa_android_app.home.components.HomeSearchBar
import com.example.inofa_android_app.home.components.HomeTopBar

@Composable
fun HomeScreen(onDeveloperClick: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Content area with scrolling
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Top Bar (App Name and Notification Icon)
            HomeTopBar()

            // 2. Discover Section (Welcome Message)
            HomeDiscoverSection()

            // 3. Search Bar
            HomeSearchBar()

            // 4. Categories Section
            HomeCategoriesSection(categories = mockCategories)

            // 5. Featured Developers Section
            HomeFeaturedDevelopersSection(
                developers = mockFeaturedDevelopers,
                onDeveloperClick = onDeveloperClick
            )
        }

        // 6. Bottom Navigation Bar
        HomeBottomNavBar()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onDeveloperClick = {})
}