package com.example.inofa_android_app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.inofa_android_app.ui.viewmodel.ProjectViewModel
import com.example.inofa_android_app.ui.viewmodel.ProjectUiState
import com.example.inofa_android_app.ui.viewmodel.ProfileViewModel
import com.example.inofa_android_app.ui.viewmodel.ProfileDataState
import com.example.inofa_android_app.ui.theme.Primary
import com.example.inofa_android_app.ui.theme.FontMedium
import com.example.inofa_android_app.utils.ImageUtils
import com.example.inofa_android_app.data.Project

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectViewScreen(
    projectId: Int,
    onBackClick: () -> Unit = {},
    projectViewModel: ProjectViewModel = viewModel()
) {
    val context = LocalContext.current
    val projectDetailState by projectViewModel.detailState.collectAsStateWithLifecycle()

    // Load project data with creator details
    LaunchedEffect(projectId) {
        projectViewModel.loadProjectById(projectId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Detail Proyek",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        when (val state = projectDetailState) {
            com.example.inofa_android_app.ui.viewmodel.ProjectDetailState.Loading, 
            com.example.inofa_android_app.ui.viewmodel.ProjectDetailState.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }
            is com.example.inofa_android_app.ui.viewmodel.ProjectDetailState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = state.message,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { projectViewModel.loadProjectById(projectId) },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("Coba Lagi")
                        }
                    }
                }
            }
            is com.example.inofa_android_app.ui.viewmodel.ProjectDetailState.Success -> {
                val projectDto = state.project
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFFF5F5F5)),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Project Details Card
                    item {
                        ProjectDetailsCardFromDto(projectDto = projectDto)
                    }

                    // Creator Information Card
                    item {
                        CreatorInfoCardFromDto(
                            projectDto = projectDto,
                            onWhatsAppClick = { whatsappLink ->
                                // Open WhatsApp with the link
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(whatsappLink)
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectDetailsCardFromDto(projectDto: com.example.inofa_android_app.network.models.ProjectDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = projectDto.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Divider(color = Color.LightGray)

            // Description
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Deskripsi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = projectDto.description ?: "Tidak ada deskripsi",
                    fontSize = 14.sp,
                    color = FontMedium,
                    lineHeight = 20.sp
                )
            }

            Divider(color = Color.LightGray)

            // Budget
            if (projectDto.budget != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Budget",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Budget",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Text(
                        text = "Rp ${String.format("%,.0f", projectDto.budget)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            // Skills Required
            if (projectDto.skillRequirements?.isNotEmpty() == true) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Keahlian yang Dibutuhkan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    androidx.compose.foundation.layout.FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        projectDto.skillRequirements.forEach { skill ->
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Primary.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = skill,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 14.sp,
                                    color = Primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Constraints
            if (!projectDto.constraints.isNullOrEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Batasan & Persyaratan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = projectDto.constraints,
                        fontSize = 14.sp,
                        color = FontMedium,
                        lineHeight = 20.sp
                    )
                }
            }

            // Status
            if (projectDto.status != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = when (projectDto.status.lowercase()) {
                            "pending" -> Color(0xFFFFF3CD)
                            "accepted" -> Color(0xFFD1F2EB)
                            "rejected" -> Color(0xFFF8D7DA)
                            "done" -> Color(0xFFD4EDDA)
                            else -> Color.LightGray
                        }
                    ) {
                        Text(
                            text = when (projectDto.status.lowercase()) {
                                "pending" -> "Menunggu"
                                "accepted" -> "Diterima"
                                "rejected" -> "Ditolak"
                                "done" -> "Selesai"
                                else -> projectDto.status
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 14.sp,
                            color = when (projectDto.status.lowercase()) {
                                "pending" -> Color(0xFF856404)
                                "accepted" -> Color(0xFF0C5460)
                                "rejected" -> Color(0xFF721C24)
                                "done" -> Color(0xFF155724)
                                else -> Color.Black
                            },
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Timestamps
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Dibuat: ${projectDto.createdAt ?: ""}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Diperbarui: ${projectDto.updatedAt ?: ""}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun CreatorInfoCardFromDto(
    projectDto: com.example.inofa_android_app.network.models.ProjectDto,
    onWhatsAppClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Pembuat Proyek",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Divider(color = Color.LightGray)

            // Creator info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Avatar
                val photoUrl = ImageUtils.toAbsoluteUrl(projectDto.creatorPhotoUrl)
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (photoUrl != null) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "${projectDto.creatorName}'s photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User",
                            tint = Primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = projectDto.creatorName ?: "Client #${projectDto.userId}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if (projectDto.creatorBio != null) {
                        Text(
                            text = projectDto.creatorBio,
                            fontSize = 14.sp,
                            color = FontMedium,
                            maxLines = 2
                        )
                    }
                    if (projectDto.creatorLocation != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = projectDto.creatorLocation,
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // WhatsApp Button
            if (projectDto.creatorWhatsappLink != null) {
                Button(
                    onClick = { onWhatsAppClick(projectDto.creatorWhatsappLink) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF25D366) // WhatsApp green
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "WhatsApp",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Hubungi via WhatsApp",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Show message if WhatsApp not available
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFF3CD)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = Color(0xFF856404),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Nomor WhatsApp pembuat proyek belum tersedia.",
                            fontSize = 12.sp,
                            color = Color(0xFF856404),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}
