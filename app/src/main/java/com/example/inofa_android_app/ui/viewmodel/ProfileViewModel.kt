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

sealed interface ProfileUiState {
    object Idle : ProfileUiState
    object Loading : ProfileUiState
    object Success : ProfileUiState
    object UploadingImage : ProfileUiState
    data class ImageUploaded(val url: String) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

sealed interface ProfileDataState {
    object Idle : ProfileDataState
    object Loading : ProfileDataState
    data class Success(val profile: ProfileCreateRequest) : ProfileDataState
    data class Error(val message: String) : ProfileDataState
}

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val uploadRepository: UploadRepository = UploadRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _profileState = MutableStateFlow<ProfileDataState>(ProfileDataState.Idle)
    val profileState: StateFlow<ProfileDataState> = _profileState

    private val _photoUrl = MutableStateFlow<String?>(null)
    val photoUrl: StateFlow<String?> = _photoUrl

    fun uploadProfilePhoto(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.UploadingImage
            uploadRepository.uploadImage(context, imageUri)
                .onSuccess { url ->
                    _photoUrl.value = url
                    Log.d("ProfileVM", "Profile photo uploaded: $url")
                    _uiState.value = ProfileUiState.ImageUploaded(url)
                }
                .onFailure { error ->
                    Log.e("ProfileVM", "Upload failed: ${error.message}", error)
                    _uiState.value = ProfileUiState.Error(
                        error.message ?: "Gagal mengupload foto"
                    )
                }
        }
    }

    fun submitProfile(
        name: String,
        location: String?,
        bio: String?,
        whatsapp: String?,
        skillsCsv: String
    ) {
        val skills = skillsCsv.split(',').mapNotNull { it.trim().takeIf { s -> s.isNotEmpty() } }
        if (skills.isEmpty()) {
            _uiState.value = ProfileUiState.Error("Isi minimal satu skill")
            return
        }
        if (name.isBlank()) {
            _uiState.value = ProfileUiState.Error("Nama wajib diisi")
            return
        }

        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            val req = ProfileCreateRequest(
                name = name.trim(),
                photo_url = _photoUrl.value?.takeIf { it.isNotBlank() },
                location = location?.trim().takeIf { !it.isNullOrEmpty() },
                bio = bio?.trim().takeIf { !it.isNullOrEmpty() },
                whatsapp = whatsapp?.trim().takeIf { !it.isNullOrEmpty() },
                skills = skills
            )
            val result = repository.createProfile(req)
            _uiState.value = result.fold(
                onSuccess = { ProfileUiState.Success },
                onFailure = { ProfileUiState.Error(it.message ?: "Gagal menyimpan profil") }
            )
        }
    }

    fun loadProfile() {
        _profileState.value = ProfileDataState.Loading
        viewModelScope.launch {
            val result = repository.getMyProfile()
            _profileState.value = result.fold(
                onSuccess = {
                    ProfileDataState.Success(
                        ProfileCreateRequest(
                            name = it.name ?: "",
                            photo_url = it.photo_url,
                            bio = it.bio,
                            location = it.location,
                            skills = it.skills ?: emptyList(),
                            whatsapp = it.whatsapp
                        )
                    )
                },
                onFailure = { ProfileDataState.Error(it.message ?: "Gagal memuat profil") }
            )
            result.getOrNull()?.photo_url?.let { url ->
                _photoUrl.value = url
            }
        }
    }

    fun updateProfile(
        name: String,
        bio: String?,
        location: String?,
        skills: List<String>,
        whatsapp: String?
    ) {
        if (name.isBlank()) {
            _uiState.value = ProfileUiState.Error("Nama wajib diisi")
            return
        }
        if (skills.isEmpty()) {
            _uiState.value = ProfileUiState.Error("Isi minimal satu skill")
            return
        }

        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            val req = ProfileCreateRequest(
                name = name.trim(),
                photo_url = _photoUrl.value?.takeIf { it.isNotBlank() },
                location = location?.trim().takeIf { !it.isNullOrEmpty() },
                bio = bio?.trim().takeIf { !it.isNullOrEmpty() },
                whatsapp = whatsapp?.trim().takeIf { !it.isNullOrEmpty() },
                skills = skills
            )
            val result = repository.updateProfile(req)
            _uiState.value = result.fold(
                onSuccess = { 
                    loadProfile() // Reload profile after update
                    ProfileUiState.Success 
                },
                onFailure = { ProfileUiState.Error(it.message ?: "Gagal mengupdate profil") }
            )
        }
    }
}
