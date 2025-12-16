package com.example.inofa_android_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inofa_android_app.data.Project
import com.example.inofa_android_app.data.repository.ProjectRepository
import com.example.inofa_android_app.network.models.ProjectCreateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface ProjectUiState {
    object Idle : ProjectUiState
    object Loading : ProjectUiState
    data class Success(val projects: List<Project>) : ProjectUiState
    data class Error(val message: String) : ProjectUiState
}

sealed interface ProjectDetailState {
    object Idle : ProjectDetailState
    object Loading : ProjectDetailState
    data class Success(val project: com.example.inofa_android_app.network.models.ProjectDto) : ProjectDetailState
    data class Error(val message: String) : ProjectDetailState
}

sealed interface ProjectActionState {
    object Idle : ProjectActionState
    object Loading : ProjectActionState
    object Success : ProjectActionState
    data class Error(val message: String) : ProjectActionState
}

class ProjectViewModel(
    private val repository: ProjectRepository = ProjectRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProjectUiState>(ProjectUiState.Idle)
    val uiState: StateFlow<ProjectUiState> = _uiState

    private val _detailState = MutableStateFlow<ProjectDetailState>(ProjectDetailState.Idle)
    val detailState: StateFlow<ProjectDetailState> = _detailState

    private val _actionState = MutableStateFlow<ProjectActionState>(ProjectActionState.Idle)
    val actionState: StateFlow<ProjectActionState> = _actionState

    fun loadProjectById(id: Int) {
        _detailState.value = ProjectDetailState.Loading
        viewModelScope.launch {
            val result = repository.getProjectById(id)
            _detailState.value = result.fold(
                onSuccess = { ProjectDetailState.Success(it) },
                onFailure = { ProjectDetailState.Error(it.message ?: "Gagal memuat detail proyek") }
            )
        }
    }

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

    fun updateProject(
        id: Int,
        title: String,
        description: String?,
        budget: Double?,
        skillsCsv: String,
        constraints: String?
    ) {
        _actionState.value = ProjectActionState.Loading
        viewModelScope.launch {
            val skillsList = skillsCsv.split(",").map { it.trim() }.filter { it.isNotBlank() }
            val request = ProjectCreateRequest(
                title = title,
                description = description,
                budget = budget,
                skillRequirements = skillsList,
                constraints = constraints
            )
            
            val result = repository.updateProject(id, request)
            _actionState.value = result.fold(
                onSuccess = { 
                    loadMyProjects() // Reload projects after update
                    ProjectActionState.Success 
                },
                onFailure = { ProjectActionState.Error(it.message ?: "Gagal mengupdate proyek") }
            )
        }
    }

    fun deleteProject(id: Int) {
        _actionState.value = ProjectActionState.Loading
        viewModelScope.launch {
            val result = repository.deleteProject(id)
            _actionState.value = result.fold(
                onSuccess = { 
                    loadMyProjects() // Reload projects after delete
                    ProjectActionState.Success 
                },
                onFailure = { ProjectActionState.Error(it.message ?: "Gagal menghapus proyek") }
            )
        }
    }

    fun updateProjectStatus(id: Int, status: String) {
        _actionState.value = ProjectActionState.Loading
        viewModelScope.launch {
            val result = repository.updateProjectStatus(id, status)
            _actionState.value = result.fold(
                onSuccess = { 
                    loadMyProjects() // Reload projects after status update
                    ProjectActionState.Success 
                },
                onFailure = { ProjectActionState.Error(it.message ?: "Gagal mengubah status proyek") }
            )
        }
    }

    fun resetActionState() {
        _actionState.value = ProjectActionState.Idle
    }
}
