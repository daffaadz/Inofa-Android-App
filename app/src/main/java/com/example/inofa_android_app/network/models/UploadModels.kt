package com.example.inofa_android_app.network.models

data class UploadImageResponse(
    val success: Boolean,
    val message: String? = null,
    val data: ImageData?
)

data class ImageData(
    val filename: String,
    val url: String,
    val size: Long,
    val mimetype: String
)
