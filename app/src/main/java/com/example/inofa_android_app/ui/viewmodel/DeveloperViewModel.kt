package com.example.inofa_android_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inofa_android_app.data.Developer
import com.example.inofa_android_app.data.repository.DeveloperRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface DeveloperUiState {
    object Idle : DeveloperUiState
    object Loading : DeveloperUiState
    data class Success(val developers: List<Developer>) : DeveloperUiState
    data class Error(val message: String) : DeveloperUiState
}

sealed interface DeveloperDetailState {
    object Idle : DeveloperDetailState
    object Loading : DeveloperDetailState
    data class Success(val developer: Developer) : DeveloperDetailState
    data class Error(val message: String) : DeveloperDetailState
}

class DeveloperViewModel(
    private val repository: DeveloperRepository = DeveloperRepository()
) : ViewModel() {

    private val _listState = MutableStateFlow<DeveloperUiState>(DeveloperUiState.Idle)
    val listState: StateFlow<DeveloperUiState> = _listState

    private val _detailState = MutableStateFlow<DeveloperDetailState>(DeveloperDetailState.Idle)
    val detailState: StateFlow<DeveloperDetailState> = _detailState

    fun loadDevelopers(skill: String? = null) {
        _listState.value = DeveloperUiState.Loading
        viewModelScope.launch {
            val result = repository.getDevelopers(skill)
            _listState.value = result.fold(
                onSuccess = { DeveloperUiState.Success(it) },
                onFailure = { DeveloperUiState.Error(it.message ?: "Failed to load developers") }
            )
        }
    }

    fun loadDeveloper(id: Int) {
        _detailState.value = DeveloperDetailState.Loading
        viewModelScope.launch {
            val result = repository.getDeveloper(id)
            _detailState.value = result.fold(
                onSuccess = { DeveloperDetailState.Success(it) },
                onFailure = { DeveloperDetailState.Error(it.message ?: "Failed to load developer") }
            )
        }
    }
}
