package com.example.inofa_android_app.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.inofa_android_app.network.NetworkModule
import com.example.inofa_android_app.utils.ImageUtils

class UploadRepository {
    private val apiService = NetworkModule.apiService

    suspend fun uploadImage(context: Context, imageUri: Uri): Result<String> {
        return try {
            Log.d("UploadRepository", "uploadImage called with URI: $imageUri")
            
            val multipartBody = ImageUtils.uriToMultipartBodyPart(context, imageUri)
                ?: return Result.failure(Exception("Failed to prepare image for upload"))

            Log.d("UploadRepository", "Uploading image to server...")
            val response = apiService.uploadImage(multipartBody)
            
            Log.d("UploadRepository", "Upload response: success=${response.success}, url=${response.data?.url}")
            
            if (response.success && response.data != null) {
                Log.d("UploadRepository", "Upload SUCCESS! URL: ${response.data.url}")
                Result.success(response.data.url)
            } else {
                Log.e("UploadRepository", "Upload FAILED: ${response.message}")
                Result.failure(Exception(response.message ?: "Upload failed"))
            }
        } catch (e: Exception) {
            Log.e("UploadRepository", "Upload EXCEPTION: ${e.message}", e)
            Result.failure(e)
        }
    }
}
