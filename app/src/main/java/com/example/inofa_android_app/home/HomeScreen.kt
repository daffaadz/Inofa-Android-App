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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.example.inofa_android_app.home.components.HomeProjectCard
import com.example.inofa_android_app.home.components.HomeCategoryTabs
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
import androidx.compose.material3.Scaffold
import androidx.compose.ui.text.font.FontWeight
import com.example.inofa_android_app.ui.viewmodel.ProfileViewModel
import com.example.inofa_android_app.ui.viewmodel.ProfileDataState

@Composable
fun HomeScreen(
    onDeveloperClick: (Int) -> Unit = {},
    onProjectClick: (Int) -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToPortfolio: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    developerViewModel: DeveloperViewModel = viewModel(),
    projectViewModel: ProjectViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val isDeveloper = UserRoleStorage.isDeveloper()
    val developerListState by developerViewModel.listState.collectAsStateWithLifecycle()
    val projectListState by projectViewModel.uiState.collectAsStateWithLifecycle()
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
        if (isDeveloper) {
            projectViewModel.loadAllProjects()
        } else {
            developerViewModel.loadDevelopers()
        }
    }

    val userName = when (val state = profileState) {
        is ProfileDataState.Success -> state.profile.name
        else -> "User"
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

    val categories: List<String> = remember(developers, projects, isDeveloper) {
        if (isDeveloper) {
            listOf("Semua") + projects
                .flatMap { it.skillRequirements }
                .distinct()
                .sorted()
        } else {
            listOf("Semua") + developers
                .flatMap { it.skills }
                .distinct()
                .sorted()
        }
    }

    val filteredDevelopers = remember(searchQuery, selectedCategory, developers) {
        developers.filter { developer ->
            val matchesSearch = searchQuery.isEmpty() ||
                developer.name.contains(searchQuery, ignoreCase = true) ||
                developer.skills.any { it.contains(searchQuery, ignoreCase = true) }
            val matchesCategory = selectedCategory == "Semua" ||
                developer.skills.any { it.contains(selectedCategory, ignoreCase = true) }
            matchesSearch && matchesCategory
        }
    }

    val filteredProjects = remember(searchQuery, selectedCategory, projects) {
        projects.filter { project ->
            val matchesSearch = searchQuery.isEmpty() ||
                project.title.contains(searchQuery, ignoreCase = true) ||
                (project.description?.contains(searchQuery, ignoreCase = true) == true)
            val matchesCategory = selectedCategory == "Semua" ||
                project.skillRequirements.any { it.contains(selectedCategory, ignoreCase = true) }
            matchesSearch && matchesCategory
        }
    }

    Scaffold(
        topBar = {
            HomeTopBar()
        },
        bottomBar = {
            HomeBottomNavBar(
                onNavigateToDiscover = onNavigateToDiscover,
                onNavigateToMessages = onNavigateToMessages,
                onNavigateToProjects = if (isDeveloper) onNavigateToPortfolio else onNavigateToProjects,
                onNavigateToProfile = onNavigateToProfile,
                selectedTab = "Home"
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Discover Section (Welcome Message)
            HomeDiscoverSection(userName = userName)

            // 2. Search Bar
            HomeSearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )

            // 3. Category Filter (khusus developer)
            if (isDeveloper) {
                HomeCategoryTabs(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            if (isDeveloper) {
                // Show filtered projects for developer
                Text(
                    text = "Menampilkan ${filteredProjects.size} dari ${projects.size} proyek",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                filteredProjects.forEach { project ->
                    HomeProjectCard(
                        project = project,
                        onClick = { onProjectClick(project.id) }
                    )
                }
            } else {
                // Show categories for client
                val categoryList = developers
                    .flatMap { it.skills }
                    .groupingBy { it }
                    .eachCount()
                    .entries
                    .mapIndexed { index, entry ->
                        Category(
                            id = index + 1,
                            name = entry.key,
                            iconResName = "",
                            devCount = entry.value
                        )
                    }
                if (categoryList.isNotEmpty()) {
                    HomeCategoriesSection(categories = categoryList)
                }

                // Featured Developers Section with filtered results
                Text(
                    text = "Menampilkan ${filteredDevelopers.size} dari ${developers.size} developer",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                HomeFeaturedDevelopersSection(
                    developers = filteredDevelopers,
                    onDeveloperClick = onDeveloperClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}