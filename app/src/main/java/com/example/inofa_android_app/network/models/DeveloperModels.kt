package com.example.inofa_android_app.network.models

import com.google.gson.annotations.SerializedName

data class DeveloperListResponse(
    val success: Boolean,
    val data: List<DeveloperDto>?,
    val message: String?
)

data class DeveloperDetailResponse(
    val success: Boolean,
    val data: DeveloperDto?,
    val message: String?
)

data class DeveloperDto(
    val id: Int,
    val email: String,
    val role: String?,
    val name: String?,
    val bio: String?,
    val location: String?,
    val skills: List<String>?,
    val whatsapp: String?,
    @SerializedName("photo_url") val photoUrl: String?,
    @SerializedName("whatsapp_link") val whatsappLink: String?,
    val portfolio: List<PortfolioDto>? = emptyList()
)

data class PortfolioDto(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    val title: String,
    val description: String?,
    val link: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
)
