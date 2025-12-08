package com.example.inofa_android_app.developer_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.inofa_android_app.data.mockDeveloper
import com.example.inofa_android_app.data.mockFeaturedDevelopers
import com.example.inofa_android_app.ui.theme.Primary
import com.example.inofa_android_app.ui.theme.BackgroundLight
import com.example.inofa_android_app.ui.theme.Black

@Composable
fun DeveloperProfileScreen(developerId: Int, onBackClick: () -> Unit = {}) {
    // Fetch data based on developerId from mock data
    val developer = mockFeaturedDevelopers.find { it.id == developerId } ?: mockDeveloper

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        // 1. Top Bar (App Bar)
        DeveloperProfileTopBar(onBackClick = onBackClick)

        // 2. Developer Header (Avatar, Name, Title, Location, Stats)
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
    val tabs = listOf("Portofolio", "Tentang")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = BackgroundLight,
            contentColor = Black
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) },
                    selectedContentColor = Primary,
                    unselectedContentColor = Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (selectedTabIndex) {
            0 -> PortfolioTab(portfolio = developer.portfolio)
            1 -> AboutMeTab(developer = developer)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeveloperProfileScreenPreview() {
    DeveloperProfileScreen(developerId = 1, onBackClick = {})
}