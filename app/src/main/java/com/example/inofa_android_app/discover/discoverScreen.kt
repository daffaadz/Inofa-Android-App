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
import com.example.inofa_android_app.data.Category
import com.example.inofa_android_app.data.Developer
import com.example.inofa_android_app.data.mockCategories
import com.example.inofa_android_app.data.mockFeaturedDevelopers
import com.example.inofa_android_app.ui.theme.Primary
import com.example.inofa_android_app.ui.theme.FontMedium
import com.example.inofa_android_app.ui.theme.BackgroundLight

@Composable
fun DiscoverScreen(
    onDeveloperClick: (Int) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }

    // Filter developers based on search query and category
    val filteredDevelopers = remember(searchQuery, selectedCategory) {
        mockFeaturedDevelopers.filter { developer ->
            val matchesSearch = searchQuery.isEmpty() || 
                developer.name.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "Semua" || 
                developer.skills.any { it.contains(selectedCategory, ignoreCase = true) }
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
                categories = listOf("Semua") + mockCategories.map { category -> category.name },
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            // Developer Count
            Text(
                text = "Menampilkan ${filteredDevelopers.size} dari ${mockFeaturedDevelopers.size} developer",
                fontSize = 14.sp,
                color = FontMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Developer List
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

        // Bottom Navigation
        DiscoverBottomNavBar(
            onNavigateToHome = onNavigateToHome,
            onNavigateToMessages = onNavigateToMessages,
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
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = developer.name.first().toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
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
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFCD34D),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${developer.rating}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
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
    onNavigateToProfile: () -> Unit
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