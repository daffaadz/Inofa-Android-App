package com.example.inofa_android_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inofa_android_app.ui.viewmodel.ProjectViewModel
import com.example.inofa_android_app.ui.viewmodel.ProjectUiState
import com.example.inofa_android_app.ui.viewmodel.ProjectActionState
import com.example.inofa_android_app.ui.theme.Primary
import com.example.inofa_android_app.ui.components.CustomSnackbarHost
import com.example.inofa_android_app.ui.components.ToastType
import com.example.inofa_android_app.ui.components.showCustomToast
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: Int,
    onBackClick: () -> Unit = {},
    onEditProject: (Int) -> Unit = {},
    projectViewModel: ProjectViewModel = viewModel()
) {
    val projectState by projectViewModel.uiState.collectAsStateWithLifecycle()
    val actionState by projectViewModel.actionState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showStatusMenu by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(projectId) {
        projectViewModel.loadMyProjects()
    }

    // Track which action is being performed
    var lastAction by remember { mutableStateOf<String?>(null) }

    // Handle action success or error
    LaunchedEffect(actionState) {
        when (actionState) {
            is ProjectActionState.Success -> {
                val message = when (lastAction) {
                    "delete" -> "Proyek berhasil dihapus"
                    "status" -> "Status proyek berhasil diubah"
                    else -> "Aksi berhasil dilakukan"
                }
                snackbarHostState.showCustomToast(
                    message = message,
                    type = ToastType.SUCCESS
                )
                delay(500)
                projectViewModel.resetActionState()
                
                // Only navigate back on delete
                if (lastAction == "delete") {
                    onBackClick()
                }
                lastAction = null
            }
            is ProjectActionState.Error -> {
                snackbarHostState.showCustomToast(
                    message = (actionState as ProjectActionState.Error).message,
                    type = ToastType.ERROR
                )
                lastAction = null
            }
            else -> {}
        }
    }

    val project = when (val state = projectState) {
        is ProjectUiState.Success -> state.projects.find { it.id == projectId }
        else -> null
    }

    Scaffold(
        topBar = {
            com.example.inofa_android_app.home.components.HomeTopBar()
        },
        snackbarHost = { 
            CustomSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { padding ->
        // Delete Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { 
                    Text(
                        "Hapus Proyek",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                text = { 
                    Text("Apakah Anda yakin ingin menghapus proyek ini? Tindakan ini tidak dapat dibatalkan.") 
                },
                confirmButton = {
                    Button(
                        onClick = {
                            lastAction = "delete"
                            projectViewModel.deleteProject(projectId)
                            showDeleteDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("Hapus")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Batal", color = Color.Gray)
                    }
                }
            )
        }

        when (val state = projectState) {
            ProjectUiState.Loading, ProjectUiState.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }
            is ProjectUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
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
                        Button(onClick = { projectViewModel.loadMyProjects() }) {
                            Text("Coba Lagi")
                        }
                    }
                }
            }
            is ProjectUiState.Success -> {
                if (project == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Proyek tidak ditemukan",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .background(Color(0xFFF5F5F5)),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Project Title Card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                ) {
                                    Text(
                                        text = project.title,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    // Status configuration
                                    val statusConfig = when (project.status?.lowercase()) {
                                        "accepted" -> Triple("Diterima", Color(0xFF10B981), Color(0xFFD1FAE5))
                                        "rejected" -> Triple("Ditolak", Color(0xFFEF4444), Color(0xFFFEE2E2))
                                        "done" -> Triple("Selesai", Color(0xFF3B82F6), Color(0xFFDBEAFE))
                                        else -> Triple("Pending", Color(0xFFFBBF24), Color(0xFFFEF3C7))
                                    }
                                    
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = statusConfig.third,
                                            modifier = Modifier.clickable { showStatusMenu = true }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .background(statusConfig.second, RoundedCornerShape(50))
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = statusConfig.first,
                                                    fontSize = 12.sp,
                                                    color = statusConfig.second,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Icon(
                                                    imageVector = Icons.Default.ArrowDropDown,
                                                    contentDescription = "Change status",
                                                    tint = statusConfig.second,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        
                                        // Status Dropdown Menu
                                        DropdownMenu(
                                            expanded = showStatusMenu,
                                            onDismissRequest = { showStatusMenu = false }
                                        ) {
                                            DropdownMenuItem(
                                                text = { 
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(8.dp)
                                                                .background(Color(0xFFFBBF24), RoundedCornerShape(50))
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text("Pending") 
                                                    }
                                                },
                                                onClick = {
                                                    lastAction = "status"
                                                    projectViewModel.updateProjectStatus(projectId, "pending")
                                                    showStatusMenu = false
                                                }
                                            )
                                            DropdownMenuItem(
                                                text = { 
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(8.dp)
                                                                .background(Color(0xFF10B981), RoundedCornerShape(50))
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text("Diterima") 
                                                    }
                                                },
                                                onClick = {
                                                    lastAction = "status"
                                                    projectViewModel.updateProjectStatus(projectId, "accepted")
                                                    showStatusMenu = false
                                                }
                                            )
                                            DropdownMenuItem(
                                                text = { 
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(8.dp)
                                                                .background(Color(0xFFEF4444), RoundedCornerShape(50))
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text("Ditolak") 
                                                    }
                                                },
                                                onClick = {
                                                    lastAction = "status"
                                                    projectViewModel.updateProjectStatus(projectId, "rejected")
                                                    showStatusMenu = false
                                                }
                                            )
                                            DropdownMenuItem(
                                                text = { 
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(8.dp)
                                                                .background(Color(0xFF3B82F6), RoundedCornerShape(50))
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text("Selesai") 
                                                    }
                                                },
                                                onClick = {
                                                    lastAction = "status"
                                                    projectViewModel.updateProjectStatus(projectId, "done")
                                                    showStatusMenu = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Description Card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Description,
                                            contentDescription = null,
                                            tint = Primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "Deskripsi Proyek",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = project.description ?: "Tidak ada deskripsi",
                                        fontSize = 14.sp,
                                        color = Color.DarkGray,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }

                        // Budget Card
                        if (project.budget != null) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AttachMoney,
                                                contentDescription = null,
                                                tint = Primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = "Budget",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = "Rp ${String.format("%,.0f", project.budget)}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Primary
                                        )
                                    }
                                }
                            }
                        }

                        // Skills Required Card
                        if (project.skillRequirements.isNotEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Settings,
                                                contentDescription = null,
                                                tint = Primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = "Keahlian yang Dibutuhkan",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        FlowRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            project.skillRequirements.forEach { skill ->
                                                AssistChip(
                                                    onClick = { },
                                                    label = { Text(skill, fontSize = 12.sp) },
                                                    colors = AssistChipDefaults.assistChipColors(
                                                        containerColor = Primary.copy(alpha = 0.1f),
                                                        labelColor = Primary
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Constraints Card
                        if (!project.constraints.isNullOrBlank()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = null,
                                                tint = Primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = "Batasan & Catatan",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = project.constraints,
                                            fontSize = 14.sp,
                                            color = Color.DarkGray,
                                            lineHeight = 20.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Project Info Card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = Primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "Informasi Proyek",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    InfoRow(label = "Dibuat", value = project.createdAt)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    InfoRow(label = "Diperbarui", value = project.updatedAt)
                                }
                            }
                        }

                        // Action Buttons
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { onEditProject(project.id) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Primary
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Edit Proyek", fontSize = 16.sp)
                                }

                                OutlinedButton(
                                    onClick = { showDeleteDialog = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.Red
                                    ),
                                    enabled = actionState !is ProjectActionState.Loading,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    if (actionState is ProjectActionState.Loading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = Color.Red,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Hapus Proyek", fontSize = 16.sp)
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable androidx.compose.foundation.layout.FlowRowScope.() -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = content
    )
}
