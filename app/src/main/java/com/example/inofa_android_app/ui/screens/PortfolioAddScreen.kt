package com.example.inofa_android_app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.inofa_android_app.BuildConfig
import com.example.inofa_android_app.utils.ImageUtils
import com.example.inofa_android_app.ui.viewmodel.PortfolioAddViewModel
import com.example.inofa_android_app.ui.viewmodel.PortfolioAddUiState
import com.example.inofa_android_app.ui.components.CustomSnackbarHost
import com.example.inofa_android_app.ui.components.ToastType
import com.example.inofa_android_app.ui.components.showCustomToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioAddScreen(
    portfolioId: Int? = null,
    onBackClick: () -> Unit = {},
    onSuccess: () -> Unit = {},
    viewModel: PortfolioAddViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var technologies by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var imageUrlInput by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isEditMode = portfolioId != null
    val snackbarHostState = remember { SnackbarHostState() }

    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            viewModel.uploadImage(context, it)
        }
    }

    // Load portfolio data if in edit mode
    LaunchedEffect(portfolioId) {
        if (portfolioId != null) {
            viewModel.loadPortfolio(portfolioId)
        }
    }

    // Update fields when data is loaded
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is PortfolioAddUiState.Success -> {
                snackbarHostState.showCustomToast(
                    "Portfolio berhasil disimpan!",
                    ToastType.SUCCESS
                )
                onSuccess()
            }
            is PortfolioAddUiState.Loaded -> {
                title = state.portfolio.title
                description = state.portfolio.description ?: ""
                link = state.portfolio.link ?: ""
                imageUrl = state.portfolio.image_url ?: ""
                imageUrlInput = imageUrl
            }
            is PortfolioAddUiState.ImageUploaded -> {
                imageUrl = state.imageUrl
                snackbarHostState.showCustomToast(
                    "Gambar berhasil diupload!",
                    ToastType.SUCCESS
                )
            }
            is PortfolioAddUiState.Error -> {
                snackbarHostState.showCustomToast(
                    state.message,
                    ToastType.ERROR
                )
            }
            PortfolioAddUiState.Idle,
            PortfolioAddUiState.Loading,
            PortfolioAddUiState.UploadingImage -> {}
        }
    }

    Scaffold(
        snackbarHost = { 
            CustomSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditMode) "Edit Portfolio" else "Tambah Portfolio Baru",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .padding(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Image Section Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Gambar Proyek",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF212121)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color(0xFFFAFAFA), RoundedCornerShape(8.dp))
                                .drawBehind {
                                    val dashWidth = 10.dp.toPx()
                                    val dashGap = 10.dp.toPx()
                                    val pathEffect = PathEffect.dashPathEffect(
                                        intervals = floatArrayOf(dashWidth, dashGap),
                                        phase = 0f
                                    )
                                    drawRoundRect(
                                        color = androidx.compose.ui.graphics.Color(0xFFBDBDBD),
                                        style = Stroke(
                                            width = 2.dp.toPx(),
                                            pathEffect = pathEffect
                                        ),
                                        cornerRadius = CornerRadius(8.dp.toPx())
                                    )
                                }
                                .clickable { launcher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = ImageUtils.toAbsoluteUrl(imageUrl),
                                    contentDescription = "Selected image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add image",
                                        tint = Color(0xFF9E9E9E),
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Text(
                                        text = "Klik untuk upload gambar",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF424242)
                                    )
                                    Text(
                                        text = "PNG, JPG hingga 5MB",
                                        fontSize = 12.sp,
                                        color = Color(0xFF9E9E9E)
                                    )
                                }
                            }

                            if (uiState is PortfolioAddUiState.UploadingImage) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Color.White)
                                }
                            }
                        }

                        Text(
                            text = "Atau masukkan URL gambar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF424242)
                        )

                        OutlinedTextField(
                            value = imageUrlInput,
                            onValueChange = {
                                imageUrlInput = it
                                if (it.isNotBlank()) {
                                    imageUrl = it
                                }
                            },
                            placeholder = {
                                Text(
                                    "https://example.com/image.jpg",
                                    fontSize = 14.sp,
                                    color = Color(0xFF9E9E9E)
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFFAFAFA),
                                focusedContainerColor = Color(0xFFFAFAFA),
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedBorderColor = Color(0xFF00BFA5)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                // Detail Proyek Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Detail Proyek",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF212121)
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Judul Proyek",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF424242)
                            )
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                placeholder = {
                                    Text(
                                        "Contoh: E-Commerce Platform",
                                        fontSize = 14.sp,
                                        color = Color(0xFF9E9E9E)
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color(0xFFFAFAFA),
                                    focusedContainerColor = Color(0xFFFAFAFA),
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedBorderColor = Color(0xFF00BFA5)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Deskripsi",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF424242)
                            )
                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                placeholder = {
                                    Text(
                                        "Jelaskan tentang proyek ini, fitur utama, dan teknologi yang digunakan",
                                        fontSize = 14.sp,
                                        color = Color(0xFF9E9E9E)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color(0xFFFAFAFA),
                                    focusedContainerColor = Color(0xFFFAFAFA),
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedBorderColor = Color(0xFF00BFA5)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                maxLines = 5
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Teknologi yang Digunakan",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF424242)
                            )
                            OutlinedTextField(
                                value = technologies,
                                onValueChange = { technologies = it },
                                placeholder = {
                                    Text(
                                        "Ketik teknologi (misal: React) dan tekan Enter",
                                        fontSize = 14.sp,
                                        color = Color(0xFF9E9E9E)
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color(0xFFFAFAFA),
                                    focusedContainerColor = Color(0xFFFAFAFA),
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedBorderColor = Color(0xFF00BFA5)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Link Proyek (Opsional)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF424242)
                            )
                            OutlinedTextField(
                                value = link,
                                onValueChange = { link = it },
                                placeholder = {
                                    Text(
                                        "https://project-demo.com",
                                        fontSize = 14.sp,
                                        color = Color(0xFF9E9E9E)
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color(0xFFFAFAFA),
                                    focusedContainerColor = Color(0xFFFAFAFA),
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedBorderColor = Color(0xFF00BFA5)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Buttons
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                if (isEditMode) {
                                    viewModel.updatePortfolio(
                                        title = title,
                                        description = description.ifBlank { null },
                                        link = link.ifBlank { null },
                                        imageUrl = imageUrl.ifBlank { null }
                                    )
                                } else {
                                    viewModel.createPortfolio(
                                        title = title,
                                        description = description.ifBlank { null },
                                        link = link.ifBlank { null },
                                        imageUrl = imageUrl.ifBlank { null }
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00BFA5),
                            disabledContainerColor = Color(0xFF00BFA5).copy(alpha = 0.5f)
                        ),
                        enabled = title.isNotBlank() && uiState !is PortfolioAddUiState.Loading && uiState !is PortfolioAddUiState.UploadingImage,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (uiState is PortfolioAddUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                if (isEditMode) "Simpan Perubahan" else "Simpan Portfolio",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF00BFA5)
                        ),
                        border = BorderStroke(1.dp, Color(0xFF00BFA5)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Batal",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PortfolioAddScreenPreview() {
    PortfolioAddScreen()
}
