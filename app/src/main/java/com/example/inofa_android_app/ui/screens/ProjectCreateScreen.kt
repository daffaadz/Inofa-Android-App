package com.example.inofa_android_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                title = { Text("Buat Proyek Baru") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Proyek *") },
                placeholder = { Text("Contoh: Website Company Profile") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi (opsional)") },
                placeholder = { Text("Jelaskan detail kebutuhan proyek Anda") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = budgetText,
                onValueChange = { budgetText = it },
                label = { Text("Budget (Rp, opsional)") },
                placeholder = { Text("Contoh: 15000000") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = skillsCsv,
                onValueChange = { skillsCsv = it },
                label = { Text("Skill yang Dibutuhkan *") },
                placeholder = { Text("Contoh: React, Node.js, PostgreSQL") },
                supportingText = { Text("Pisahkan dengan koma") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = constraints,
                onValueChange = { constraints = it },
                label = { Text("Batasan/Catatan (opsional)") },
                placeholder = { Text("Contoh: Deadline 1 bulan, harus SEO-friendly") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState is ProjectCreateUiState.Error) {
                Text(
                    text = (uiState as ProjectCreateUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

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
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is ProjectCreateUiState.Loading
            ) {
                Text(if (uiState is ProjectCreateUiState.Loading) "Membuat..." else "Buat Proyek")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectCreateScreenPreview() {
    ProjectCreateScreen()
}
