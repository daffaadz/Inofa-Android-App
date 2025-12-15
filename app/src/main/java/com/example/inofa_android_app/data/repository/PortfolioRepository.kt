package com.example.inofa_android_app.data.repository

import android.util.Log
import com.example.inofa_android_app.network.NetworkModule
import com.example.inofa_android_app.network.models.PortfolioCreateRequest
import com.example.inofa_android_app.network.models.PortfolioItemDto

class PortfolioRepository {
    private val apiService = NetworkModule.apiService

    suspend fun createPortfolio(
        title: String,
        description: String?,
        link: String?,
        imageUrl: String?
    ): Result<PortfolioItemDto> {
        return try {
            Log.d("PortfolioRepository", "createPortfolio called")
            Log.d("PortfolioRepository", "  title: $title")
            Log.d("PortfolioRepository", "  description: $description")
            Log.d("PortfolioRepository", "  link: $link")
            Log.d("PortfolioRepository", "  imageUrl: $imageUrl")
            
            val request = PortfolioCreateRequest(
                title = title,
                description = description,
                link = link,
                image_url = imageUrl
            )
            
            Log.d("PortfolioRepository", "Request object: $request")
            
            val response = apiService.createPortfolio(request)
            
            Log.d("PortfolioRepository", "Response: success=${response.success}, message=${response.message}")
            
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to create portfolio"))
            }
        } catch (e: Exception) {
            Log.e("PortfolioRepository", "Exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getMyPortfolio(): Result<List<PortfolioItemDto>> {
        return try {
            val response = apiService.getMyPortfolio()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to load portfolio"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPortfolioById(id: Int): Result<PortfolioItemDto> {
        return try {
            val response = apiService.getPortfolioById(id)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to load portfolio"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePortfolio(
        id: Int,
        title: String,
        description: String?,
        link: String?,
        imageUrl: String?
    ): Result<PortfolioItemDto> {
        return try {
            val request = PortfolioCreateRequest(
                title = title,
                description = description,
                link = link,
                image_url = imageUrl
            )
            val response = apiService.updatePortfolio(id, request)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to update portfolio"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePortfolio(id: Int): Result<Unit> {
        return try {
            val response = apiService.deletePortfolio(id)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Failed to delete portfolio"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
