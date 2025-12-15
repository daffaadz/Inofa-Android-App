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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.inofa_android_app.BuildConfig
import com.example.inofa_android_app.utils.ImageUtils
import com.example.inofa_android_app.ui.viewmodel.PortfolioAddViewModel
import com.example.inofa_android_app.ui.viewmodel.PortfolioAddUiState

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
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isEditMode = portfolioId != null
    val snackbarHostState = remember { SnackbarHostState() }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            // Upload image when selected
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
                snackbarHostState.showSnackbar("Portfolio berhasil disimpan!")
                onSuccess()
            }
            is PortfolioAddUiState.Loaded -> {
                title = state.portfolio.title
                description = state.portfolio.description ?: ""
                link = state.portfolio.link ?: ""
                imageUrl = state.portfolio.image_url ?: ""
            }
            is PortfolioAddUiState.ImageUploaded -> {
                imageUrl = state.imageUrl
                android.util.Log.d("PortfolioAddScreen", "ImageUploaded! Setting imageUrl to: ${state.imageUrl}")
                android.util.Log.d("PortfolioAddScreen", "Current imageUrl variable: $imageUrl")
                snackbarHostState.showSnackbar("Gambar berhasil diupload!")
            }
            is PortfolioAddUiState.Error -> {
                snackbarHostState.showSnackbar("Error: ${state.message}")
            }
            PortfolioAddUiState.Idle,
            PortfolioAddUiState.Loading,
            PortfolioAddUiState.UploadingImage -> {
                // no-op
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Portfolio" else "Tambah Portfolio Baru") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Image Upload Section
                Text(
                    text = "Gambar Proyek",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    val resolvedImageUrl = ImageUtils.toAbsoluteUrl(imageUrl)
                    val painterSource = selectedImageUri ?: resolvedImageUrl

                    if (painterSource != null) {
                        // Show selected or existing image
                        Image(
                            painter = rememberAsyncImagePainter(painterSource),
                            contentDescription = "Selected image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Remove button
                        IconButton(
                            onClick = {
                                selectedImageUri = null
                                imageUrl = ""
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(32.dp)
                                .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove image",
                                tint = Color.Black
                            )
                        }
                    } else {
                        // Show upload placeholder
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Upload",
                                tint = Color.Gray,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Klik untuk upload gambar",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "PNG, JPG hingga 5MB",
                                fontSize = 10.sp,
                                color = Color.LightGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    
                    // Show upload progress
                    if (uiState is PortfolioAddUiState.UploadingImage) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }
                }

                Text(
                    text = "Atau masukkan URL gambar",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    placeholder = { Text("https://example.com/image.jpg", fontSize = 14.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                // Detail Proyek Section
                Text(
                    text = "Detail Proyek",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Text(
                    text = "Judul Proyek",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Contoh: E-Commerce Platform", fontSize = 14.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Text(
                    text = "Deskripsi",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { 
                        Text(
                            "Jelaskan tentang proyek ini, fitur utama, dan teknologi yang digunakan",
                            fontSize = 14.sp
                        ) 
                    },
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Text(
                    text = "Teknologi yang Digunakan",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = technologies,
                    onValueChange = { technologies = it },
                    placeholder = { Text("Ketik teknologi (misal: React) dan tekan Enter", fontSize = 14.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Text(
                    text = "Link Proyek (Opsional)",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = link,
                    onValueChange = { link = it },
                    placeholder = { Text("https://project-demo.com", fontSize = 14.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bottom Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF00BFA5)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF00BFA5))
                ) {
                    Text("Batal")
                }

                Button(
                    onClick = {
                        android.util.Log.d("PortfolioAddScreen", "Button clicked! title: $title, isEditMode: $isEditMode")
                        android.util.Log.d("PortfolioAddScreen", "Current uiState: $uiState")
                        
                        if (title.isNotBlank()) {
                            if (isEditMode) {
                                android.util.Log.d("PortfolioAddScreen", "Calling updatePortfolio")
                                viewModel.updatePortfolio(
                                    title = title,
                                    description = description.ifBlank { null },
                                    link = link.ifBlank { null },
                                    imageUrl = imageUrl.ifBlank { null }
                                )
                            } else {
                                android.util.Log.d("PortfolioAddScreen", "Calling createPortfolio")
                                viewModel.createPortfolio(
                                    title = title,
                                    description = description.ifBlank { null },
                                    link = link.ifBlank { null },
                                    imageUrl = imageUrl.ifBlank { null }
                                )
                            }
                        } else {
                            android.util.Log.w("PortfolioAddScreen", "Title is blank!")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BFA5)
                    ),
                    enabled = title.isNotBlank() && uiState !is PortfolioAddUiState.Loading && uiState !is PortfolioAddUiState.UploadingImage
                ) {
                    if (uiState is PortfolioAddUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(if (isEditMode) "Simpan Perubahan" else "Simpan Portfolio")
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
