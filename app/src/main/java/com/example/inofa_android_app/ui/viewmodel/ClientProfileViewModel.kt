package com.example.inofa_android_app.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inofa_android_app.data.repository.ProfileRepository
import com.example.inofa_android_app.data.repository.UploadRepository
import com.example.inofa_android_app.network.models.ProfileCreateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface ClientProfileUiState {
    object Idle : ClientProfileUiState
    object Loading : ClientProfileUiState
    object Success : ClientProfileUiState
    object UploadingImage : ClientProfileUiState
    data class ImageUploaded(val url: String) : ClientProfileUiState
    data class Error(val message: String) : ClientProfileUiState
}

class ClientProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val uploadRepository: UploadRepository = UploadRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ClientProfileUiState>(ClientProfileUiState.Idle)
    val uiState: StateFlow<ClientProfileUiState> = _uiState

    private val _photoUrl = MutableStateFlow<String?>(null)
    val photoUrl: StateFlow<String?> = _photoUrl

    fun uploadProfilePhoto(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = ClientProfileUiState.UploadingImage
            uploadRepository.uploadImage(context, imageUri)
                .onSuccess { url ->
                    _photoUrl.value = url
                    Log.d("ClientProfileVM", "Profile photo uploaded: $url")
                    _uiState.value = ClientProfileUiState.ImageUploaded(url)
                }
                .onFailure { error ->
                    Log.e("ClientProfileVM", "Upload failed: ${error.message}", error)
                    _uiState.value = ClientProfileUiState.Error(
                        error.message ?: "Gagal mengupload foto"
                    )
                }
        }
    }

    fun submitProfile(
        name: String,
        location: String?,
        bio: String?
    ) {
        if (name.isBlank()) {
            _uiState.value = ClientProfileUiState.Error("Nama wajib diisi")
            return
        }

        _uiState.value = ClientProfileUiState.Loading
        viewModelScope.launch {
            val req = ProfileCreateRequest(
                name = name.trim(),
                photo_url = _photoUrl.value?.takeIf { it.isNotBlank() },
                location = location?.trim()?.takeIf { it.isNotEmpty() },
                bio = bio?.trim()?.takeIf { it.isNotEmpty() },
                whatsapp = null,
                skills = emptyList() // Client doesn't have skills
            )
            val result = repository.createProfile(req)
            _uiState.value = result.fold(
                onSuccess = { ClientProfileUiState.Success },
                onFailure = { ClientProfileUiState.Error(it.message ?: "Gagal menyimpan profil") }
            )
        }
    }
}
