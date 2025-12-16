package com.example.inofa_android_app.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.inofa_android_app.ui.theme.Primary
import com.example.inofa_android_app.ui.theme.FontMedium
import com.example.inofa_android_app.ui.theme.Secondary
import com.example.inofa_android_app.ui.theme.BackgroundLight
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inofa_android_app.data.Project
import com.example.inofa_android_app.ui.viewmodel.ProjectUiState
import com.example.inofa_android_app.ui.viewmodel.ProjectViewModel
import com.example.inofa_android_app.ui.viewmodel.ProfileViewModel
import com.example.inofa_android_app.ui.viewmodel.ProfileDataState
import com.example.inofa_android_app.data.UserRoleStorage
import com.example.inofa_android_app.data.repository.AuthRepository
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToPortfolio: () -> Unit = {},
    onLogout: () -> Unit = {},
    projectViewModel: ProjectViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val isDeveloper = UserRoleStorage.isDeveloper()
    val projectState by projectViewModel.uiState.collectAsStateWithLifecycle()
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (!isDeveloper) {
            projectViewModel.loadMyProjects()
        }
        profileViewModel.loadProfile()
    }

    val projects: List<Project> = if (!isDeveloper) {
        when (val state = projectState) {
            is ProjectUiState.Success -> state.projects
            else -> emptyList()
        }
    } else emptyList()

    Scaffold(
        topBar = {
            com.example.inofa_android_app.home.components.HomeTopBar()
        },
        bottomBar = {
            com.example.inofa_android_app.home.components.HomeBottomNavBar(
                onNavigateToDiscover = onNavigateToDiscover,
                onNavigateToMessages = onNavigateToMessages,
                onNavigateToProjects = if (isDeveloper) onNavigateToPortfolio else onNavigateToProjects,
                onNavigateToProfile = { },
                selectedTab = "Profile"
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight)
        ) {

            // Welcome Section
            item {
                val userName = when (val state = profileState) {
                    is ProfileDataState.Success -> state.profile.name
                    else -> null
                }
                WelcomeSection(userName)
            }

            if (isDeveloper) {
                // Show profile edit form for developer
                item {
                    ProfileEditSection(profileViewModel = profileViewModel)
                }
            } else {
                // Show stats and projects for client
                // Stats Cards
                item {
                    StatsSection(activeProjects = projects.size, completedProjects = 0)
                }

                // Pending Payment Banner (if any)
                item {
                    PendingPaymentBanner()
                }

                // Projects Section Header
                item {
                    ProjectsSectionHeader(projectCount = projects.size)
                }

                when (val state = projectState) {
                    ProjectUiState.Loading, ProjectUiState.Idle -> {
                        item {
                            LoadingProjects()
                        }
                    }
                    is ProjectUiState.Error -> {
                        item {
                            ErrorProjects(message = state.message)
                        }
                    }
                    is ProjectUiState.Success -> {
                        items(state.projects) { project ->
                            ProjectCard(project = project)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Logout Button
            item {
                LogoutButton(onLogout = onLogout)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Bottom Navigation
        ProfileBottomNavBar(
            onNavigateToHome = onNavigateToHome,
            onNavigateToDiscover = onNavigateToDiscover,
            onNavigateToMessages = onNavigateToMessages,
            onNavigateToProjects = onNavigateToProjects,
            onNavigateToPortfolio = onNavigateToPortfolio
        )
    }
}

@Composable
fun ProfileTopBar() {
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
fun WelcomeSection(name: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Selamat datang, ${name?.takeIf { it.isNotBlank() } ?: "Pengguna"}!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "Kelola dan pantau proyek Anda dengan mudah",
            fontSize = 14.sp,
            color = FontMedium
        )
    }
}

@Composable
fun StatsSection(activeProjects: Int, completedProjects: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Active Projects Card
        StatsCard(
            icon = Icons.Default.Star,
            count = activeProjects.toString(),
            label = "Proyek Aktif",
            iconColor = Primary,
            modifier = Modifier.weight(1f)
        )

        // Completed Projects Card
        StatsCard(
            icon = Icons.Default.Check,
            count = completedProjects.toString(),
            label = "Proyek Selesai",
            iconColor = Primary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatsCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: String,
    label: String,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
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
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = iconColor.copy(alpha = 0.1f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = count,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = FontMedium
                )
            }
        }
    }
}

@Composable
fun PendingPaymentBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Secondary.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = Color(0xFFF59E0B),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Pembayaran Tertunda",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = "Anda memiliki 1 proyek yang menunggu pembayaran",
                    fontSize = 12.sp,
                    color = FontMedium
                )
            }
        }
    }
}

@Composable
fun ProjectsSectionHeader(projectCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Semua Proyek",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "$projectCount proyek",
            fontSize = 14.sp,
            color = FontMedium
        )
    }
}

@Composable
fun ProjectCard(project: Project) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { /* TODO: Navigate to project detail */ },
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
            // Developer Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = project.title.firstOrNull()?.toString() ?: "?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Project Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = project.description ?: "",
                    fontSize = 12.sp,
                    color = FontMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (project.budget != null) "Budget: Rp ${project.budget}" else "Budget tidak tersedia",
                    fontSize = 12.sp,
                    color = FontMedium
                )
                if (project.skillRequirements.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Skill: ${project.skillRequirements.joinToString(", ")}",
                        fontSize = 12.sp,
                        color = FontMedium
                    )
                }
            }

            // Arrow Icon
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "View Details",
                tint = FontMedium
            )
        }
    }
}

@Composable
fun ProfileBottomNavBar(
    onNavigateToHome: () -> Unit,
    onNavigateToDiscover: () -> Unit,
    onNavigateToMessages: () -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToPortfolio: () -> Unit
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
            selected = false,
            onClick = onNavigateToDiscover,
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
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}

@Composable
fun LoadingProjects() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(color = Primary)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = "Memuat proyek...", color = FontMedium)
    }
}

@Composable
fun ErrorProjects(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LogoutButton(onLogout: () -> Unit) {
    val authRepository = remember { AuthRepository() }
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Keluar Akun") },
            text = { Text("Apakah Anda yakin ingin keluar dari akun?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            authRepository.logout()
                            onLogout()
                        }
                        showDialog = false
                    }
                ) {
                    Text("Keluar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Keluar Akun",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun ProfileEditSection(profileViewModel: ProfileViewModel) {
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()
    
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var skillsText by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }

    LaunchedEffect(profileState) {
        if (profileState is ProfileDataState.Success) {
            val profile = (profileState as ProfileDataState.Success).profile
            name = profile.name
            bio = profile.bio ?: ""
            location = profile.location ?: ""
            skillsText = profile.skills.joinToString(", ")
            whatsapp = profile.whatsapp ?: ""
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Edit Profil",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") },
                minLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Lokasi") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = skillsText,
                onValueChange = { skillsText = it },
                label = { Text("Skills (pisahkan dengan koma)") },
                placeholder = { Text("Java, Kotlin, Android") },
                minLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = whatsapp,
                onValueChange = { whatsapp = it },
                label = { Text("WhatsApp") },
                placeholder = { Text("6281234567890") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    val skillsList = skillsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    profileViewModel.updateProfile(
                        name = name,
                        bio = bio.ifBlank { null },
                        location = location.ifBlank { null },
                        skills = skillsList,
                        whatsapp = whatsapp.ifBlank { null }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("Simpan Perubahan")
            }
        }
    }
}
