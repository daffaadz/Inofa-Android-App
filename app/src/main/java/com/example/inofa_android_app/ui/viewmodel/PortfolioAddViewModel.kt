package com.example.inofa_android_app.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inofa_android_app.data.repository.PortfolioRepository
import com.example.inofa_android_app.data.repository.UploadRepository
import com.example.inofa_android_app.network.models.PortfolioItemDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PortfolioAddUiState {
    data object Idle : PortfolioAddUiState
    data object Loading : PortfolioAddUiState
    data object Success : PortfolioAddUiState
    data class Loaded(val portfolio: PortfolioItemDto) : PortfolioAddUiState
    data object UploadingImage : PortfolioAddUiState
    data class ImageUploaded(val imageUrl: String) : PortfolioAddUiState
    data class Error(val message: String) : PortfolioAddUiState
}

class PortfolioAddViewModel(
    private val repository: PortfolioRepository = PortfolioRepository(),
    private val uploadRepository: UploadRepository = UploadRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<PortfolioAddUiState>(PortfolioAddUiState.Idle)
    val uiState: StateFlow<PortfolioAddUiState> = _uiState.asStateFlow()

    private var portfolioId: Int? = null
    private var uploadedImageUrl: String? = null

    fun uploadImage(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = PortfolioAddUiState.UploadingImage
            uploadRepository.uploadImage(context, imageUri)
                .onSuccess { imageUrl ->
                    uploadedImageUrl = imageUrl
                    Log.d("PortfolioAddVM", "Image uploaded! URL saved: $uploadedImageUrl")
                    _uiState.value = PortfolioAddUiState.ImageUploaded(imageUrl)
                }
                .onFailure { error ->
                    _uiState.value = PortfolioAddUiState.Error(
                        error.message ?: "Gagal mengupload gambar"
                    )
                }
        }
    }

    fun loadPortfolio(id: Int) {
        portfolioId = id
        viewModelScope.launch {
            _uiState.value = PortfolioAddUiState.Loading
            repository.getPortfolioById(id)
                .onSuccess { portfolio ->
                    _uiState.value = PortfolioAddUiState.Loaded(portfolio)
                }
                .onFailure { error ->
                    _uiState.value = PortfolioAddUiState.Error(
                        error.message ?: "Gagal memuat portfolio"
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
        // Use uploaded image URL if no imageUrl provided
        val finalImageUrl = imageUrl?.takeIf { it.isNotBlank() } ?: uploadedImageUrl
        
        Log.d("PortfolioAddVM", "createPortfolio called")
        Log.d("PortfolioAddVM", "  title: $title")
        Log.d("PortfolioAddVM", "  description: $description")
        Log.d("PortfolioAddVM", "  link: $link")
        Log.d("PortfolioAddVM", "  imageUrl param: $imageUrl")
        Log.d("PortfolioAddVM", "  uploadedImageUrl: $uploadedImageUrl")
        Log.d("PortfolioAddVM", "  finalImageUrl: $finalImageUrl")
        
        viewModelScope.launch {
            try {
                _uiState.value = PortfolioAddUiState.Loading
                Log.d("PortfolioAddVM", "State set to Loading")
                
                val result = repository.createPortfolio(title, description, link, finalImageUrl)
                Log.d("PortfolioAddVM", "Repository result: ${result.isSuccess}")
                
                result.onSuccess {
                    Log.d("PortfolioAddVM", "Success! Setting state to Success")
                    _uiState.value = PortfolioAddUiState.Success
                }
                .onFailure { error ->
                    Log.e("PortfolioAddVM", "Failed: ${error.message}", error)
                    _uiState.value = PortfolioAddUiState.Error(
                        error.message ?: "Gagal menambahkan portfolio"
                    )
                }
            } catch (e: Exception) {
                Log.e("PortfolioAddVM", "Exception in createPortfolio: ${e.message}", e)
                _uiState.value = PortfolioAddUiState.Error(
                    e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }

    fun updatePortfolio(
        title: String,
        description: String?,
        link: String?,
        imageUrl: String?
    ) {
        val id = portfolioId ?: run {
            Log.e("PortfolioAddVM", "portfolioId is null!")
            _uiState.value = PortfolioAddUiState.Error("ID portfolio tidak ditemukan")
            return
        }
        
        // Use uploaded image URL if no imageUrl provided
        val finalImageUrl = imageUrl?.takeIf { it.isNotBlank() } ?: uploadedImageUrl
        
        Log.d("PortfolioAddVM", "updatePortfolio called")
        Log.d("PortfolioAddVM", "  id: $id, title: $title")
        Log.d("PortfolioAddVM", "  imageUrl param: $imageUrl")
        Log.d("PortfolioAddVM", "  uploadedImageUrl: $uploadedImageUrl")
        Log.d("PortfolioAddVM", "  finalImageUrl: $finalImageUrl")
        
        viewModelScope.launch {
            try {
                _uiState.value = PortfolioAddUiState.Loading
                Log.d("PortfolioAddVM", "State set to Loading")
                
                val result = repository.updatePortfolio(id, title, description, link, finalImageUrl)
                Log.d("PortfolioAddVM", "Repository result: ${result.isSuccess}")
                
                result.onSuccess {
                    Log.d("PortfolioAddVM", "Success! Setting state to Success")
                    _uiState.value = PortfolioAddUiState.Success
                }
                .onFailure { error ->
                    Log.e("PortfolioAddVM", "Failed: ${error.message}", error)
                    _uiState.value = PortfolioAddUiState.Error(
                        error.message ?: "Gagal mengupdate portfolio"
                    )
                }
            } catch (e: Exception) {
                Log.e("PortfolioAddVM", "Exception in updatePortfolio: ${e.message}", e)
                _uiState.value = PortfolioAddUiState.Error(
                    e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }
}
