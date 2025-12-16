package com.example.inofa_android_app.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.inofa_android_app.data.Project
import com.example.inofa_android_app.utils.ImageUtils
import com.example.inofa_android_app.utils.DateUtils
import com.example.inofa_android_app.ui.viewmodel.ProjectUiState
import com.example.inofa_android_app.ui.viewmodel.ProjectViewModel
import com.example.inofa_android_app.ui.viewmodel.ProfileViewModel
import com.example.inofa_android_app.ui.viewmodel.ProfileDataState
import kotlinx.coroutines.launch
import com.example.inofa_android_app.data.repository.AuthRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientProfileViewScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onNavigateToProjectDetail: (Int) -> Unit = {},
    onLogout: () -> Unit = {},
    projectViewModel: ProjectViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val projectState by projectViewModel.uiState.collectAsStateWithLifecycle()
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()
    val photoUrl by profileViewModel.photoUrl.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        projectViewModel.loadMyProjects()
        profileViewModel.loadProfile()
    }

    val projects: List<Project> = when (val state = projectState) {
        is ProjectUiState.Success -> state.projects
        else -> emptyList()
    }

    val profile = when (val state = profileState) {
        is ProfileDataState.Success -> state.profile
        else -> null
    }

    Scaffold(
        topBar = {
            com.example.inofa_android_app.home.components.HomeTopBar()
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFF00BFA5)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = onNavigateToHome,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BFA5),
                        selectedTextColor = Color(0xFF00BFA5),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Discover") },
                    label = { Text("Discover") },
                    selected = false,
                    onClick = onNavigateToDiscover,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BFA5),
                        selectedTextColor = Color(0xFF00BFA5),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Projects") },
                    label = { Text("Projects") },
                    selected = false,
                    onClick = onNavigateToProjects,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BFA5),
                        selectedTextColor = Color(0xFF00BFA5),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BFA5),
                        selectedTextColor = Color(0xFF00BFA5),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            // Profile Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile Photo
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0E0E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (photoUrl != null) {
                                AsyncImage(
                                    model = ImageUtils.toAbsoluteUrl(photoUrl),
                                    contentDescription = "Profile photo",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp),
                                    tint = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Name
                        Text(
                            text = profile?.name ?: "User",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        // Location or bio
                        Text(
                            text = profile?.location ?: profile?.bio ?: "Belum ada informasi",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Edit Profile Button
                        Button(
                            onClick = onEditProfile,
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF10B981)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            enabled = true
                        ) {
                            Text(
                                text = "Edit Profile",
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Stats Cards
            val doneProjects = projects.filter { it.status?.lowercase() == "done" }
            val activeProjects = projects.filter { it.status?.lowercase() == "accepted" }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Active Projects
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFF00BFA5).copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Work,
                                    contentDescription = null,
                                    tint = Color(0xFF00BFA5),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${activeProjects.size}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Proyek Aktif",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    // Completed Projects
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFF00BFA5).copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF00BFA5),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${doneProjects.size}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Proyek Selesai",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // Pending Payment Banner
            val pendingProjects = projects.filter { it.status?.lowercase() == "pending" }
            if (pendingProjects.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF9E6)
                        ),
                        elevation = CardDefaults.cardElevation(0.dp)
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
                                    text = "Proyek Menunggu",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Anda memiliki ${pendingProjects.size} proyek dengan status pending",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // Projects Section Header
            item {
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
                        text = "${projects.size} proyek",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Projects List
            when (val state = projectState) {
                ProjectUiState.Loading, ProjectUiState.Idle -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF00BFA5))
                        }
                    }
                }
                is ProjectUiState.Error -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.message,
                                color = Color.Red
                            )
                        }
                    }
                }
                is ProjectUiState.Success -> {
                    if (projects.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Belum ada proyek",
                                    color = Color.Gray
                                )
                            }
                        }
                    } else {
                        items(projects) { project ->
                            ClientProjectCard(
                                project = project,
                                clientName = profile?.name,
                                onClick = { onNavigateToProjectDetail(project.id) }
                            )
                        }
                    }
                }
            }

            // Logout Button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LogoutButtonClient(onLogout = onLogout)
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ClientProjectCard(
    project: Project,
    clientName: String? = null,
    onClick: () -> Unit = {}
) {
    val initial = DateUtils.getInitial(clientName)
    val relativeTime = DateUtils.getRelativeTime(project.createdAt)
    
    // Status badge configuration
    val statusConfig = when (project.status?.lowercase()) {
        "accepted" -> Triple("Diterima", Color(0xFF10B981), Color(0xFF10B981))
        "rejected" -> Triple("Ditolak", Color(0xFFEF4444), Color(0xFFEF4444))
        "done" -> Triple("Selesai", Color(0xFF3B82F6), Color(0xFF3B82F6))
        else -> Triple("Pending", Color(0xFFFBBF24), Color(0xFFFBBF24))
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00BFA5).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initial,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00BFA5)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = clientName ?: "Client",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFF3F4F6)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(statusConfig.second)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = statusConfig.first,
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            }
                        }
                        Text(
                            text = relativeTime,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "View Details",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun LogoutButtonClient(onLogout: () -> Unit) {
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
