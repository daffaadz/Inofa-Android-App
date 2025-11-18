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
import com.example.inofa_android_app.data.Category
import com.example.inofa_android_app.data.Developer
import com.example.inofa_android_app.data.mockCategories
import com.example.inofa_android_app.data.mockFeaturedDevelopers
import com.example.inofa_android_app.developer_profile.components.RatingBar
import com.example.inofa_android_app.developer_profile.components.PrimaryGreen
import com.example.inofa_android_app.developer_profile.components.LightGreenBackground

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
                contentDescription = "Notifications"
            )
        }
    }
}

// --- Discover Section ---
@Composable
fun HomeDiscoverSection() {
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
                    imageVector = Icons.Default.Star, // Placeholder for Discover icon
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
                text = "Selamat Malam!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Temukan developer profesional untuk proyek Anda.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// --- Search Bar ---
@Composable
fun HomeSearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = { /* TODO: Handle search input */ },
        placeholder = { Text("Cari developer, skill, atau proyek...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
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
        )
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
            .size(100.dp)
            .clickable { /* TODO: Handle category click */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon Placeholder
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
            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${category.devCount} devs",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
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
                    fontWeight = FontWeight.Bold
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
            // Avatar
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = developer.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = developer.title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RatingBar(rating = developer.rating)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${developer.rating}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// --- Bottom Navigation Bar ---
@Composable
fun HomeBottomNavBar() {
    val items = listOf("Home", "Discover", "Messages", "Profile")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.Default.Email,
        Icons.Default.Person
    )
    val selectedItem = "Home" // Mock selected item

    NavigationBar(
        containerColor = Color.White,
        contentColor = PrimaryGreen
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        icons[index],
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = item == selectedItem,
                onClick = { /* TODO: Handle navigation click */ },
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
        HomeBottomNavBar()
    }
}
