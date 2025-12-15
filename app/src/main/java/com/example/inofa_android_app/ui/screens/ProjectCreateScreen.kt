package com.example.inofa_android_app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inofa_android_app.ui.viewmodel.ProjectCreateUiState
import com.example.inofa_android_app.ui.viewmodel.ProjectCreateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCreateScreen(
    onBackClick: () -> Unit = {},
    onSuccess: () -> Unit = {},
    viewModel: ProjectCreateViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var budgetText by remember { mutableStateOf("") }
    var skillsCsv by remember { mutableStateOf("") }
    var constraints by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is ProjectCreateUiState.Success) onSuccess()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Buat Proyek Baru",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .padding(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
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
                                        "Contoh: Website Company Profile",
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
                                        "Jelaskan detail kebutuhan proyek Anda",
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
                                text = "Budget (Opsional)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF424242)
                            )
                            OutlinedTextField(
                                value = budgetText,
                                onValueChange = { budgetText = it },
                                placeholder = {
                                    Text(
                                        "Contoh: 15000000",
                                        fontSize = 14.sp,
                                        color = Color(0xFF9E9E9E)
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                                text = "Skill yang Dibutuhkan",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF424242)
                            )
                            OutlinedTextField(
                                value = skillsCsv,
                                onValueChange = { skillsCsv = it },
                                placeholder = {
                                    Text(
                                        "Contoh: React, Node.js, PostgreSQL",
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
                            Text(
                                text = "Pisahkan dengan koma",
                                fontSize = 12.sp,
                                color = Color(0xFF9E9E9E)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Batasan/Catatan (Opsional)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF424242)
                            )
                            OutlinedTextField(
                                value = constraints,
                                onValueChange = { constraints = it },
                                placeholder = {
                                    Text(
                                        "Contoh: Deadline 1 bulan, harus SEO-friendly",
                                        fontSize = 14.sp,
                                        color = Color(0xFF9E9E9E)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color(0xFFFAFAFA),
                                    focusedContainerColor = Color(0xFFFAFAFA),
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedBorderColor = Color(0xFF00BFA5)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                maxLines = 4
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (uiState is ProjectCreateUiState.Error) {
                        Text(
                            text = (uiState as ProjectCreateUiState.Error).message,
                            color = Color(0xFFFF5252),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                val budget = budgetText.toDoubleOrNull()
                                viewModel.createProject(
                                    title = title,
                                    description = description.takeIf { it.isNotBlank() },
                                    budget = budget,
                                    skillsCsv = skillsCsv,
                                    constraints = constraints.takeIf { it.isNotBlank() }
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00BFA5),
                                disabledContainerColor = Color(0xFF00BFA5).copy(alpha = 0.5f)
                            ),
                            enabled = title.isNotBlank() && skillsCsv.isNotBlank() && uiState !is ProjectCreateUiState.Loading,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (uiState is ProjectCreateUiState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Buat Proyek",
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
}

@Preview(showBackground = true)
@Composable
private fun ProjectCreateScreenPreview() {
    ProjectCreateScreen()
}
