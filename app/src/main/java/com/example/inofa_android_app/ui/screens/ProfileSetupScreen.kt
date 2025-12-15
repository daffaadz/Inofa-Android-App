package com.example.inofa_android_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inofa_android_app.ui.viewmodel.ProfileUiState
import com.example.inofa_android_app.ui.viewmodel.ProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    onDone: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is ProfileUiState.Success) onDone()
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Lengkapi Profil") })
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Lokasi (opsional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio singkat (opsional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = whatsapp,
                onValueChange = { whatsapp = it },
                label = { Text("WhatsApp (628xxxxx, opsional)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = skills,
                onValueChange = { skills = it },
                label = { Text("Skill dipisah koma, contoh: Kotlin, Android") },
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState is ProfileUiState.Error) {
                Text(
                    text = (uiState as ProfileUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    viewModel.submitProfile(
                        name = name,
                        location = location,
                        bio = bio,
                        whatsapp = whatsapp,
                        skillsCsv = skills
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is ProfileUiState.Loading
            ) {
                Text(if (uiState is ProfileUiState.Loading) "Menyimpan..." else "Simpan & Lanjut")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSetupPreview() {
    ProfileSetupScreen()
}
