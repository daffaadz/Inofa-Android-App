package com.example.inofa_android_app.network.models

data class PortfolioCreateRequest(
    val title: String,
    val description: String?,
    val link: String?,
    val image_url: String?
)

data class PortfolioItemDto(
    val id: Int,
    val user_id: Int,
    val title: String,
    val description: String?,
    val link: String?,
    val image_url: String?,
    val created_at: String?,
    val updated_at: String?
)

data class PortfolioListResponse(
    val success: Boolean,
    val message: String? = null,
    val data: List<PortfolioItemDto>?
)

data class PortfolioCreateResponse(
    val success: Boolean,
    val message: String? = null,
    val data: PortfolioItemDto?
)

data class PortfolioDeleteResponse(
    val success: Boolean,
    val message: String?
)
