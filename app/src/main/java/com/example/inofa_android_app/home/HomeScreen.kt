package com.example.inofa_android_app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inofa_android_app.data.Category
import com.example.inofa_android_app.data.Project
import com.example.inofa_android_app.home.components.HomeBottomNavBar
import com.example.inofa_android_app.home.components.HomeCategoriesSection
import com.example.inofa_android_app.home.components.HomeDiscoverSection
import com.example.inofa_android_app.home.components.HomeFeaturedDevelopersSection
import com.example.inofa_android_app.home.components.HomeSearchBar
import com.example.inofa_android_app.home.components.HomeTopBar
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inofa_android_app.ui.viewmodel.DeveloperUiState
import com.example.inofa_android_app.ui.viewmodel.DeveloperViewModel
import com.example.inofa_android_app.ui.viewmodel.ProjectViewModel
import com.example.inofa_android_app.ui.viewmodel.ProjectUiState
import com.example.inofa_android_app.data.UserRoleStorage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HomeScreen(
    onDeveloperClick: (Int) -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToPortfolio: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    developerViewModel: DeveloperViewModel = viewModel(),
    projectViewModel: ProjectViewModel = viewModel()
) {
    val isDeveloper = UserRoleStorage.isDeveloper()
    val developerListState by developerViewModel.listState.collectAsStateWithLifecycle()
    val projectListState by projectViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (isDeveloper) {
            projectViewModel.loadAllProjects()
        } else {
            developerViewModel.loadDevelopers()
        }
    }

    val developers = if (!isDeveloper) {
        when (val state = developerListState) {
            is DeveloperUiState.Success -> state.developers
            else -> emptyList()
        }
    } else emptyList()

    val projects = if (isDeveloper) {
        when (val state = projectListState) {
            is ProjectUiState.Success -> state.projects
            else -> emptyList()
        }
    } else emptyList()

    val categories: List<Category> = remember(developers) {
        if (!isDeveloper) {
            developers
                .flatMap { it.skills }
                .groupingBy { it }
                .eachCount()
                .entries
                .mapIndexed { index, entry ->
                    Category(
                        id = index + 1,
                        name = entry.key,
                        iconResName = "", // icon not provided by backend
                        devCount = entry.value
                    )
                }
        } else emptyList()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
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

            if (isDeveloper) {
                // Show projects for developer
                Text(
                    text = "Daftar Proyek",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                projects.forEach { project ->
                    HomeProjectCard(project = project)
                }
            } else {
                // Show categories and developers for client
                if (categories.isNotEmpty()) {
                    HomeCategoriesSection(categories = categories)
                }

                // Featured Developers Section
                HomeFeaturedDevelopersSection(
                    developers = developers,
                    onDeveloperClick = onDeveloperClick
                )
            }
        }

        // 6. Bottom Navigation Bar
        HomeBottomNavBar(
            onNavigateToDiscover = onNavigateToDiscover,
            onNavigateToMessages = onNavigateToMessages,
            onNavigateToProjects = onNavigateToProjects,
            onNavigateToPortfolio = onNavigateToPortfolio,
            onNavigateToProfile = onNavigateToProfile
        )
    }
}

@Composable
fun HomeProjectCard(project: Project) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = project.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = project.description ?: "Deskripsi belum tersedia",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Budget: Rp ${project.budget}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}