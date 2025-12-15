package com.example.inofa_android_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inofa_android_app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val token: String? = null,
    val nextStep: NextStep? = null
)

enum class NextStep {
    HOME,
    COMPLETE_PROFILE,
    SET_ROLE
}

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    private suspend fun resolveNextStep(): NextStep {
        return try {
            val status = repository.status()
            when (status.status) {
                "ready" -> NextStep.HOME
                "profile_missing" -> NextStep.COMPLETE_PROFILE
                "role_missing" -> NextStep.SET_ROLE
                else -> if (status.profile_missing == true) NextStep.COMPLETE_PROFILE else NextStep.HOME
            }
        } catch (_: Exception) {
            NextStep.HOME
        }
    }

    fun login(email: String, password: String) {
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                if (response.success && response.data?.token != null) {
                    val next = resolveNextStep()
                    _uiState.value = AuthUiState(isLoading = false, token = response.data.token, nextStep = next)
                } else {
                    _uiState.value = AuthUiState(
                        isLoading = false,
                        error = response.message ?: "Login failed"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun register(email: String, password: String, role: String) {
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val response = repository.register(email, password)
                if (response.success && response.data?.token != null) {
                    // Set role immediately after register
                    repository.setRole(role)
                    val next = resolveNextStep()
                    _uiState.value = AuthUiState(isLoading = false, token = response.data.token, nextStep = next)
                } else {
                    _uiState.value = AuthUiState(
                        isLoading = false,
                        error = response.message ?: "Register failed"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
