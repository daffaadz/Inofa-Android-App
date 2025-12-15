package com.example.inofa_android_app.data.repository

import com.example.inofa_android_app.network.NetworkModule
import com.example.inofa_android_app.network.models.ProfileCreateRequest
import com.example.inofa_android_app.network.models.ProfileResponse
import com.example.inofa_android_app.network.models.ProfileDto

class ProfileRepository {
    private val api = NetworkModule.apiService

    suspend fun createProfile(request: ProfileCreateRequest): Result<ProfileResponse> {
        return try {
            val response = api.createProfile(request)
            if (response.success) Result.success(response)
            else Result.failure(IllegalStateException(response.message ?: "Failed to create profile"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyProfile(): Result<ProfileDto> {
        return try {
            val response = api.getMyProfile()
            val data = response.data
            if (response.success && data != null) Result.success(data)
            else Result.failure(IllegalStateException(response.message ?: "Profile not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(request: ProfileCreateRequest): Result<ProfileResponse> {
        return try {
            val response = api.updateProfile(request)
            if (response.success) Result.success(response)
            else Result.failure(IllegalStateException(response.message ?: "Failed to update profile"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
