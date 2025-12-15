package com.example.inofa_android_app.developer_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.inofa_android_app.data.Developer
import com.example.inofa_android_app.developer_profile.components.DeveloperProfileHeader
import com.example.inofa_android_app.developer_profile.components.DeveloperProfileTopBar
import com.example.inofa_android_app.developer_profile.tabs.AboutMeTab
import com.example.inofa_android_app.developer_profile.tabs.PortfolioTab
import com.example.inofa_android_app.ui.theme.BackgroundLight
import com.example.inofa_android_app.ui.theme.Black
import com.example.inofa_android_app.ui.theme.Primary
import com.example.inofa_android_app.ui.viewmodel.DeveloperDetailState
import com.example.inofa_android_app.ui.viewmodel.DeveloperViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DeveloperProfileScreen(
    developerId: Int,
    onBackClick: () -> Unit = {},
    viewModel: DeveloperViewModel = viewModel()
) {
    val detailState by viewModel.detailState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(developerId) {
        viewModel.loadDeveloper(developerId)
    }

    when (val state = detailState) {
        DeveloperDetailState.Loading, DeveloperDetailState.Idle -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        }
        is DeveloperDetailState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
        is DeveloperDetailState.Success -> {
            val developer: Developer = state.developer
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {
                DeveloperProfileTopBar(onBackClick = onBackClick)
                DeveloperProfileHeader(
                    developer = developer,
                    actionButtonText = "Kirim Form Permintaan",
                    onActionClick = {
                        val waUrl = developer.whatsappLink
                            ?: developer.whatsapp?.let { "https://wa.me/$it" }
                        if (waUrl.isNullOrBlank()) {
                            Toast.makeText(context, "Nomor WhatsApp tidak tersedia", Toast.LENGTH_SHORT).show()
                        } else {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(waUrl))
                            context.startActivity(intent)
                        }
                    }
                )
                DeveloperProfileContent(developer = developer)
            }
        }
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