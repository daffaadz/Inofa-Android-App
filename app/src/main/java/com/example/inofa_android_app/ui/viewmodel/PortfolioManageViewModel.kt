package com.example.inofa_android_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inofa_android_app.data.repository.PortfolioRepository
import com.example.inofa_android_app.network.models.PortfolioItemDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PortfolioUiState {
    data object Loading : PortfolioUiState
    data class Success(val portfolios: List<PortfolioItemDto>) : PortfolioUiState
    data class Error(val message: String) : PortfolioUiState
}

class PortfolioManageViewModel(
    private val repository: PortfolioRepository = PortfolioRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<PortfolioUiState>(PortfolioUiState.Loading)
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()

    fun loadPortfolios() {
        viewModelScope.launch {
            _uiState.value = PortfolioUiState.Loading
            repository.getMyPortfolio()
                .onSuccess { portfolios ->
                    _uiState.value = PortfolioUiState.Success(portfolios)
                }
                .onFailure { error ->
                    _uiState.value = PortfolioUiState.Error(
                        error.message ?: "Failed to load portfolios"
                    )
                }
        }
    }

    fun createPortfolio(
        title: String,
        description: String?,
        link: String?,
        imageUrl: String?
    ) {
        viewModelScope.launch {
            repository.createPortfolio(title, description, link, imageUrl)
                .onSuccess {
                    loadPortfolios()
                }
                .onFailure { error ->
                    _uiState.value = PortfolioUiState.Error(
                        error.message ?: "Failed to create portfolio"
                    )
                }
        }
    }

    fun deletePortfolio(id: Int) {
        viewModelScope.launch {
            repository.deletePortfolio(id)
                .onSuccess {
                    loadPortfolios()
                }
                .onFailure { error ->
                    _uiState.value = PortfolioUiState.Error(
                        error.message ?: "Failed to delete portfolio"
                    )
                }
        }
    }
}
