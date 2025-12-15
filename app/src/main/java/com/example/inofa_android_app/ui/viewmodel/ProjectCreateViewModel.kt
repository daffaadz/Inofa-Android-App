package com.example.inofa_android_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inofa_android_app.data.repository.ProjectRepository
import com.example.inofa_android_app.network.models.ProjectCreateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface ProjectCreateUiState {
    object Idle : ProjectCreateUiState
    object Loading : ProjectCreateUiState
    object Success : ProjectCreateUiState
    data class Error(val message: String) : ProjectCreateUiState
}

class ProjectCreateViewModel(
    private val repository: ProjectRepository = ProjectRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProjectCreateUiState>(ProjectCreateUiState.Idle)
    val uiState: StateFlow<ProjectCreateUiState> = _uiState

    fun createProject(
        title: String,
        description: String?,
        budget: Double?,
        skillsCsv: String,
        constraints: String?
    ) {
        if (title.isBlank()) {
            _uiState.value = ProjectCreateUiState.Error("Judul proyek wajib diisi")
            return
        }

        val skills = skillsCsv.split(',').mapNotNull { it.trim().takeIf { s -> s.isNotEmpty() } }
        if (skills.isEmpty()) {
            _uiState.value = ProjectCreateUiState.Error("Skill yang dibutuhkan wajib diisi")
            return
        }

        _uiState.value = ProjectCreateUiState.Loading
        viewModelScope.launch {
            val request = ProjectCreateRequest(
                title = title.trim(),
                description = description?.trim(),
                budget = budget,
                skillRequirements = skills,
                constraints = constraints?.trim()
            )
            val result = repository.createProject(request)
            _uiState.value = result.fold(
                onSuccess = { ProjectCreateUiState.Success },
                onFailure = { ProjectCreateUiState.Error(it.message ?: "Gagal membuat proyek") }
            )
        }
    }
}
