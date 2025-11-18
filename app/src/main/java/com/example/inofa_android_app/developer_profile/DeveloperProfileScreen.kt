package com.example.inofa_android_app.developer_profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inofa_android_app.developer_profile.components.DeveloperProfileHeader
import com.example.inofa_android_app.developer_profile.components.DeveloperProfileTopBar
import com.example.inofa_android_app.developer_profile.tabs.AboutMeTab
import com.example.inofa_android_app.developer_profile.tabs.PortfolioTab
import com.example.inofa_android_app.developer_profile.tabs.ReviewsTab
import androidx.compose.ui.tooling.preview.Preview
import com.example.inofa_android_app.data.mockDeveloper

@Composable
fun DeveloperProfileScreen(developerId: Int, onBackClick: () -> Unit = {}) {
    // In a real app, you would fetch data based on developerId
    val developer = mockDeveloper // Using mock data for now

    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Top Bar (App Bar)
        DeveloperProfileTopBar(onBackClick = onBackClick)

        // 2. Developer Header (Avatar, Name, Title, Location, Stats)
        // The action button text changes based on the tab, but for the main screen, we'll use the "Tentang Saya" button text
        DeveloperProfileHeader(
            developer = developer,
            actionButtonText = "Kirim Form Permintaan" // Based on TentangSaya.png
        )

        // 3. Tabbed Content (Portfolio, About, Reviews)
        DeveloperProfileContent(developer = developer)
    }
}



@Composable
fun DeveloperProfileContent(developer: com.example.inofa_android_app.data.Developer) {
    val tabs = listOf("Portofolio", "Tentang", "Ulasan")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (selectedTabIndex) {
            0 -> PortfolioTab(portfolio = developer.portfolio)
            1 -> AboutMeTab(developer = developer)
            2 -> ReviewsTab(reviews = developer.reviews)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeveloperProfileScreenPreview() {
    DeveloperProfileScreen(developerId = 1, onBackClick = {})
}