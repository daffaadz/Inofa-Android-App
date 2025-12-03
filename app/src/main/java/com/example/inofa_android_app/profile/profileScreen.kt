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
import com.example.inofa_android_app.data.mockProjects

data class ProjectDisplay(
    val id: Int,
    val title: String,
    val developerName: String,
    val status: String, // "Pending", "In Progress", "Selesai"
    val timeAgo: String
)

// Convert backend Project to display format
val mockProjectsDisplay = listOf(
    ProjectDisplay(1, "Website Company Profile", "Sarah Putri", "Pending", "2 jam lalu"),
    ProjectDisplay(2, "Aplikasi Mobile E-Commerce", "Ahmad Nur Hidayat", "In Progress", "5 jam lalu"),
    ProjectDisplay(3, "Dashboard Analytics", "Budi Santoso", "Selesai", "1 hari lalu"),
    ProjectDisplay(4, "REST API Backend", "Dewi Lestari", "Selesai", "3 hari lalu"),
)

@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(BackgroundLight)
        ) {
            // Top Bar
            item {
                ProfileTopBar()
            }

            // Welcome Section
            item {
                WelcomeSection()
            }

            // Stats Cards
            item {
                StatsSection()
            }

            // Pending Payment Banner (if any)
            item {
                PendingPaymentBanner()
            }

            // Projects Section Header
            item {
                ProjectsSectionHeader()
            }

            // Project List
            items(mockProjectsDisplay) { project ->
                ProjectCard(project = project)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Bottom Navigation
        ProfileBottomNavBar(
            onNavigateToHome = onNavigateToHome,
            onNavigateToDiscover = onNavigateToDiscover,
            onNavigateToMessages = onNavigateToMessages
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
fun WelcomeSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Selamat Siang, Citra!",
            fontSize = 24.sp,
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
fun StatsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Active Projects Card
        StatsCard(
            icon = Icons.Default.Star,
            count = "2",
            label = "Proyek Aktif",
            iconColor = Primary,
            modifier = Modifier.weight(1f)
        )

        // Completed Projects Card
        StatsCard(
            icon = Icons.Default.Check,
            count = "3",
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
fun ProjectsSectionHeader() {
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
            text = "6 proyek",
            fontSize = 14.sp,
            color = FontMedium
        )
    }
}

@Composable
fun ProjectCard(project: ProjectDisplay) {
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
                    text = project.developerName.first().toString(),
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
                    text = project.developerName,
                    fontSize = 12.sp,
                    color = FontMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusBadge(status = project.status)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = project.timeAgo,
                        fontSize = 11.sp,
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
fun StatusBadge(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "Pending" -> Color(0xFFFEF3C7) to Color(0xFFF59E0B)
        "In Progress" -> Color(0xFFDCFCE7) to Color(0xFF10B981)
        "Selesai" -> Color(0xFFE0E7FF) to Color(0xFF6366F1)
        else -> Color.LightGray to Color.Black
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = backgroundColor
    ) {
        Text(
            text = status,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ProfileBottomNavBar(
    onNavigateToHome: () -> Unit,
    onNavigateToDiscover: () -> Unit,
    onNavigateToMessages: () -> Unit
) {
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
        NavigationBarItem(
            icon = { Icon(Icons.Default.Email, contentDescription = "Messages") },
            label = { Text("Messages") },
            selected = false,
            onClick = onNavigateToMessages,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Primary,
                selectedTextColor = Primary,
                unselectedIconColor = FontMedium,
                unselectedTextColor = FontMedium,
                indicatorColor = Color.White
            )
        )
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