package com.example.inofa_android_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inofa_android_app.data.Project
import com.example.inofa_android_app.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface ProjectUiState {
    object Idle : ProjectUiState
    object Loading : ProjectUiState
    data class Success(val projects: List<Project>) : ProjectUiState
    data class Error(val message: String) : ProjectUiState
}

class ProjectViewModel(
    private val repository: ProjectRepository = ProjectRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProjectUiState>(ProjectUiState.Idle)
    val uiState: StateFlow<ProjectUiState> = _uiState

    fun loadMyProjects() {
        _uiState.value = ProjectUiState.Loading
        viewModelScope.launch {
            val result = repository.getMyProjects()
            _uiState.value = result.fold(
                onSuccess = { ProjectUiState.Success(it) },
                onFailure = { ProjectUiState.Error(it.message ?: "Gagal memuat proyek") }
            )
        }
    }

    fun loadAllProjects() {
        _uiState.value = ProjectUiState.Loading
        viewModelScope.launch {
            val result = repository.getAllProjects()
            _uiState.value = result.fold(
                onSuccess = { ProjectUiState.Success(it) },
                onFailure = { ProjectUiState.Error(it.message ?: "Gagal memuat semua proyek") }
            )
        }
    }
}
