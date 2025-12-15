package com.example.inofa_android_app.network.models

data class ProfileCreateRequest(
    val name: String,
    val photo_url: String? = null,
    val bio: String? = null,
    val location: String? = null,
    val skills: List<String>,
    val whatsapp: String? = null
)

data class ProfileResponse(
    val success: Boolean,
    val message: String? = null,
    val data: ProfileDto? = null
)

data class ProfileDto(
    val id: Int?,
    val user_id: Int?,
    val name: String?,
    val photo_url: String?,
    val bio: String?,
    val location: String?,
    val skills: List<String>?,
    val whatsapp: String?
)
