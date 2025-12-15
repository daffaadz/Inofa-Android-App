package com.example.inofa_android_app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.inofa_android_app.utils.ImageUtils
import com.example.inofa_android_app.ui.viewmodel.PortfolioManageViewModel
import com.example.inofa_android_app.ui.viewmodel.PortfolioUiState
import com.example.inofa_android_app.network.models.PortfolioItemDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioManageScreen(
    onBackClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onEditClick: (Int) -> Unit = {},
    viewModel: PortfolioManageViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Reload portfolios when screen becomes visible
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadPortfolios()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadPortfolios()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Kelola Portfolio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Handle notification */ }) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color.Red,
                                    modifier = Modifier.size(8.dp)
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Blue divider line
            Divider(
                thickness = 2.dp,
                color = Color(0xFF00BFA5)
            )

            when (val state = uiState) {
                PortfolioUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is PortfolioUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(state.message)
                    }
                }
                is PortfolioUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header with My Portfolio and Add Project button
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Folder,
                                        contentDescription = "Portfolio",
                                        tint = Color(0xFF00BFA5),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "My Portfolio",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF212121)
                                    )
                                }

                                Button(
                                    onClick = onAddClick,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF00BFA5)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Add Project",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // Portfolio items
                        if (state.portfolios.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Belum ada portfolio. Tap Add Project untuk menambah.",
                                        color = Color.Gray
                                    )
                                }
                            }
                        } else {
                            items(state.portfolios) { item ->
                                PortfolioCard(
                                    item = item,
                                    onEdit = { onEditClick(item.id) },
                                    onDelete = { viewModel.deletePortfolio(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PortfolioCard(
    item: PortfolioItemDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val dashWidth = 10.dp.toPx()
                val dashGap = 10.dp.toPx()
                val pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(dashWidth, dashGap),
                    phase = 0f
                )
                drawRoundRect(
                    color = androidx.compose.ui.graphics.Color(0xFF00BFA5),
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = pathEffect
                    ),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
        ) {
            // Image section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            ) {
                val resolvedImage = ImageUtils.toAbsoluteUrl(item.image_url)
                if (resolvedImage != null) {
                    AsyncImage(
                        model = resolvedImage,
                        contentDescription = item.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Content section with white background
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )

                // Description
                if (!item.description.isNullOrBlank()) {
                    Text(
                        text = item.description,
                        fontSize = 14.sp,
                        color = Color(0xFF757575),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Technology chips (parse from description or use dummy data)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // For demo purposes, showing some technology tags
                    items(listOf("React", "Node.js", "MongoDB")) { tech ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFE0F2F1),
                            border = BorderStroke(1.dp, Color(0xFF00BFA5))
                        ) {
                            Text(
                                text = tech,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                color = Color(0xFF00BFA5),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Edit button
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White, CircleShape)
                            .clip(CircleShape)
                    ) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 2.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color(0xFF00BFA5),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Delete button
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White, CircleShape)
                            .clip(CircleShape)
                    ) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 2.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFFF5252),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PortfolioManageScreenPreview() {
    PortfolioManageScreen()
}
