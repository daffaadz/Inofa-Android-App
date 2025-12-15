package com.example.inofa_android_app.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.inofa_android_app.data.Category
import com.example.inofa_android_app.data.Developer
import com.example.inofa_android_app.data.Project
import com.example.inofa_android_app.data.mockCategories
import com.example.inofa_android_app.data.mockFeaturedDevelopers
import com.example.inofa_android_app.developer_profile.components.RatingBar
import com.example.inofa_android_app.developer_profile.components.PrimaryGreen
import com.example.inofa_android_app.developer_profile.components.LightGreenBackground
import com.example.inofa_android_app.data.UserRoleStorage
import com.example.inofa_android_app.utils.ImageUtils

// --- Top Bar ---
@Composable
fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Inofa",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )
        IconButton(onClick = { /* TODO: Handle notification click */ }) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.Black
            )
        }
    }
}

// --- Discover Section ---
@Composable
fun HomeDiscoverSection(userName: String = "User") {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightGreenBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Discover",
                    tint = PrimaryGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryGreen
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Selamat Datang, $userName!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            val isDeveloper = UserRoleStorage.isDeveloper()
            Text(
                text = if (isDeveloper) "Temukan proyek yang sesuai\ndengan keahlian Anda." else "Temukan developer profesional\nuntuk proyek Anda.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}

// --- Search Bar ---
@Composable
fun HomeSearchBar(
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = { Text("Cari developer, skill, atau proyek...", color = Color.Gray) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Gray.copy(alpha = 0.5f),
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        singleLine = true
    )
}

// --- Categories Section ---
@Composable
fun HomeCategoriesSection(categories: List<Category>) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text(
            text = "Kategori",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = "Jelajahi berdasarkan keahlian",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryItem(category = category)
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable { /* TODO: Handle category click */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Icon in Circle Background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(LightGreenBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (category.name) {
                        "Full-stack" -> Icons.Default.Star
                        "UI/UX" -> Icons.Default.Star
                        "Mobile" -> Icons.Default.Star
                        else -> Icons.Default.Star
                    },
                    contentDescription = category.name,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = "${category.devCount} devs",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

// --- Featured Developers Section ---
@Composable
fun HomeFeaturedDevelopersSection(
    developers: List<Developer>,
    onDeveloperClick: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Developer Unggulan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Rekomendasi terbaik untuk Anda",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Text(
                text = "Lihat Semua >",
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryGreen,
                modifier = Modifier.clickable { /* TODO: Handle See All click */ }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (developers.isEmpty()) {
            Text(
                text = "Belum ada data developer",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(developers) { developer ->
                    FeaturedDeveloperItem(
                        developer = developer,
                        onClick = { onDeveloperClick(developer.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedDeveloperItem(developer: Developer, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Avatar with photo
            if (!developer.photoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageUtils.toAbsoluteUrl(developer.photoUrl),
                    contentDescription = "Photo of ${developer.name}",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(LightGreenBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = developer.name.first().toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = developer.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = if (developer.skills.isNotEmpty()) developer.skills.first() else "Developer",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
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
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

// --- Bottom Navigation Bar ---
@Composable
fun HomeBottomNavBar(
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToPortfolio: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val isClient = UserRoleStorage.isClient()
    val selectedItem = "Home"

    NavigationBar(
        containerColor = Color.White,
        contentColor = PrimaryGreen
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryGreen,
                selectedTextColor = PrimaryGreen,
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
                selectedIconColor = PrimaryGreen,
                selectedTextColor = PrimaryGreen,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
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
                    selectedIconColor = PrimaryGreen,
                    selectedTextColor = PrimaryGreen,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
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
                selectedIconColor = PrimaryGreen,
                selectedTextColor = PrimaryGreen,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.White
            )
        )
    }
}

// --- Category Tabs for Developer Filter ---
@Composable
fun HomeCategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories.size) { index ->
            val category = categories[index]
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryGreen,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color.Black
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = if (category == selectedCategory) PrimaryGreen else Color.LightGray,
                    selectedBorderColor = PrimaryGreen,
                    enabled = true,
                    selected = category == selectedCategory
                )
            )
        }
    }
}

// --- Project Card for Developer Homepage ---
@Composable
fun HomeProjectCard(project: Project) {
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
            // Project Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(PrimaryGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = "Project",
                    tint = PrimaryGreen,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Project Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = project.description ?: "Tidak ada deskripsi",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
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
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                if (project.skillRequirements.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = project.skillRequirements.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryGreen,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeComponentsPreview() {
    Column {
        HomeTopBar()
        HomeDiscoverSection()
        HomeSearchBar()
        HomeCategoriesSection(mockCategories)
        HomeFeaturedDevelopersSection(mockFeaturedDevelopers, onDeveloperClick = {})
        HomeBottomNavBar(
            onNavigateToDiscover = {},
            onNavigateToMessages = {},
            onNavigateToProfile = {}
        )
    }
}
