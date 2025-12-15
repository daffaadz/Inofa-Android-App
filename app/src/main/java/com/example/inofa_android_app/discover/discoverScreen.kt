package com.example.inofa_android_app.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.inofa_android_app.data.Developer
import com.example.inofa_android_app.data.Project
import com.example.inofa_android_app.ui.theme.Primary
import com.example.inofa_android_app.ui.theme.FontMedium
import com.example.inofa_android_app.ui.theme.BackgroundLight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inofa_android_app.ui.viewmodel.DeveloperUiState
import com.example.inofa_android_app.ui.viewmodel.DeveloperViewModel
import com.example.inofa_android_app.ui.viewmodel.ProjectViewModel
import com.example.inofa_android_app.ui.viewmodel.ProjectUiState
import androidx.compose.material3.CircularProgressIndicator
import com.example.inofa_android_app.data.UserRoleStorage
import com.example.inofa_android_app.utils.ImageUtils

@Composable
fun DiscoverScreen(
    onDeveloperClick: (Int) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToPortfolio: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    viewModel: DeveloperViewModel = viewModel(),
    projectViewModel: ProjectViewModel = viewModel()
) {
    val isDeveloper = UserRoleStorage.isDeveloper()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }

    val listState by viewModel.listState.collectAsStateWithLifecycle()
    val projectState by projectViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (isDeveloper) {
            projectViewModel.loadAllProjects()
        } else {
            viewModel.loadDevelopers()
        }
    }

    val developers: List<Developer> = when (val state = listState) {
        is DeveloperUiState.Success -> state.developers
        else -> emptyList()
    }

    val projects: List<Project> = when (val state = projectState) {
        is ProjectUiState.Success -> state.projects
        else -> emptyList()
    }

    val categories = if (isDeveloper) {
        remember(projects) {
            listOf("Semua") + projects
                .flatMap { it.skillRequirements }
                .distinct()
                .sorted()
        }
    } else {
        remember(developers) {
            listOf("Semua") + developers
                .flatMap { it.skills }
                .distinct()
                .sorted()
        }
    }

    val filteredDevelopers = remember(searchQuery, selectedCategory, developers) {
        developers.filter { developer ->
            val matchesSearch = searchQuery.isEmpty() ||
                developer.name.contains(searchQuery, ignoreCase = true)
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

    Column(modifier = Modifier.fillMaxSize()) {
        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .background(BackgroundLight)
        ) {
            // Top Bar
            DiscoverTopBar()

            // Search Bar
            DiscoverSearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )

            // Category Tabs
            CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            // Count
            if (isDeveloper) {
                Text(
                    text = "Menampilkan ${filteredProjects.size} dari ${projects.size} proyek",
                    fontSize = 14.sp,
                    color = FontMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            } else {
                Text(
                    text = "Menampilkan ${filteredDevelopers.size} dari ${developers.size} developer",
                    fontSize = 14.sp,
                    color = FontMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            if (isDeveloper) {
                when (projectState) {
                    ProjectUiState.Loading, ProjectUiState.Idle -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Primary)
                        }
                    }
                    is ProjectUiState.Error -> {
                        Text(
                            text = (projectState as ProjectUiState.Error).message,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    is ProjectUiState.Success -> {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredProjects) { project ->
                                ProjectListItem(
                                    project = project,
                                    onClick = { /* TODO: Navigate to project detail */ }
                                )
                            }
                        }
                    }
                }
            } else {
                when (listState) {
                    DeveloperUiState.Loading, DeveloperUiState.Idle -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Primary)
                        }
                    }
                    is DeveloperUiState.Error -> {
                        Text(
                            text = (listState as DeveloperUiState.Error).message,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    is DeveloperUiState.Success -> {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredDevelopers) { developer ->
                                DeveloperListItem(
                                    developer = developer,
                                    onClick = { onDeveloperClick(developer.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Navigation
        DiscoverBottomNavBar(
            onNavigateToHome = onNavigateToHome,
            onNavigateToMessages = onNavigateToMessages,
            onNavigateToProjects = onNavigateToProjects,
            onNavigateToPortfolio = onNavigateToPortfolio,
            onNavigateToProfile = onNavigateToProfile
        )
    }
}

@Composable
fun DiscoverTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Inofa",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Primary
        )
        IconButton(onClick = { /* TODO: Handle notification */ }) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun DiscoverSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("Cari developer, skill, atau proyek...", color = FontMedium) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = FontMedium
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.LightGray,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        singleLine = true
    )
}

@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Primary,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color.Black
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = if (category == selectedCategory) Primary else Color.LightGray,
                    selectedBorderColor = Primary,
                    enabled = true,
                    selected = category == selectedCategory
                )
            )
        }
    }
}

@Composable
fun ProjectListItem(
    project: Project,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Project Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = "Project",
                    tint = Primary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Project Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = project.description ?: "Tidak ada deskripsi",
                    fontSize = 14.sp,
                    color = FontMedium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Budget",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (project.budget != null) "Rp ${String.format("%,.0f", project.budget)}" else "Budget belum ditentukan",
                        fontSize = 14.sp,
                        color = FontMedium
                    )
                }
                if (project.skillRequirements.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = project.skillRequirements.joinToString(", "),
                        fontSize = 12.sp,
                        color = Primary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun DeveloperListItem(
    developer: Developer,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            val photoUrl = ImageUtils.toAbsoluteUrl(developer.photoUrl)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (photoUrl != null) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "${developer.name}'s photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Text(
                        text = developer.name.first().toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Developer Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = developer.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = if (developer.skills.isNotEmpty()) developer.skills.first() else "Developer",
                    fontSize = 14.sp,
                    color = FontMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = developer.location ?: "Unknown",
                        fontSize = 14.sp,
                        color = FontMedium
                    )
                }
            }
        }
    }
}

@Composable
fun DiscoverBottomNavBar(
    onNavigateToHome: () -> Unit,
    onNavigateToMessages: () -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToPortfolio: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val isClient = UserRoleStorage.isClient()
    NavigationBar(
        containerColor = Color.White,
        contentColor = Primary
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = onNavigateToHome,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Primary,
                selectedTextColor = Primary,
                unselectedIconColor = FontMedium,
                unselectedTextColor = FontMedium,
                indicatorColor = Color.White
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Discover") },
            label = { Text("Discover") },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Primary,
                selectedTextColor = Primary,
                unselectedIconColor = FontMedium,
                unselectedTextColor = FontMedium,
                indicatorColor = Color.White
            )
        )
        if (isClient) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Add, contentDescription = "Projects") },
                label = { Text("Projects") },
                selected = false,
                onClick = onNavigateToProjects,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    unselectedIconColor = FontMedium,
                    unselectedTextColor = FontMedium,
                    indicatorColor = Color.White
                )
            )
        }
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = onNavigateToProfile,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Primary,
                selectedTextColor = Primary,
                unselectedIconColor = FontMedium,
                unselectedTextColor = FontMedium,
                indicatorColor = Color.White
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DiscoverScreenPreview() {
    DiscoverScreen()
}