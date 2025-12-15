package com.example.inofa_android_app.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.inofa_android_app.BuildConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    
    /**
     * Convert relative image path to absolute URL using API_BASE_URL
     * Examples:
     * - "/uploads/img-123.jpg" -> "http://192.168.18.59:4000/uploads/img-123.jpg"
     * - "http://example.com/img.jpg" -> "http://example.com/img.jpg" (already absolute)
     * - null or empty -> null
     */
    fun toAbsoluteUrl(relativePath: String?): String? {
        if (relativePath.isNullOrBlank()) return null
        
        // Already an absolute URL
        if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) {
            return relativePath
        }
        
        // Combine with base URL
        val baseUrl = BuildConfig.API_BASE_URL.trimEnd('/')
        val path = if (relativePath.startsWith("/")) relativePath else "/$relativePath"
        return "$baseUrl$path"
    }
    
    /**
     * Convert URI to MultipartBody.Part for upload
     */
    fun uriToMultipartBodyPart(context: Context, uri: Uri, partName: String = "image"): MultipartBody.Part? {
        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            
            // Get file name from URI
            val fileName = getFileName(context, uri) ?: "image_${System.currentTimeMillis()}.jpg"
            
            // Create temporary file
            val tempFile = File(context.cacheDir, fileName)
            val outputStream = FileOutputStream(tempFile)
            
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            
            // Get MIME type
            val mimeType = contentResolver.getType(uri) ?: "image/*"
            
            // Create RequestBody
            val requestBody = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())
            
            // Create MultipartBody.Part
            return MultipartBody.Part.createFormData(partName, fileName, requestBody)
            
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    
    /**
     * Get file name from URI
     */
    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex >= 0) {
                        result = it.getString(columnIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }
}
