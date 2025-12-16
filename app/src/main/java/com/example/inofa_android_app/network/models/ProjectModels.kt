package com.example.inofa_android_app.network.models

import com.google.gson.annotations.SerializedName

data class ProjectListResponse(
    val success: Boolean,
    val data: List<ProjectDto>?,
    val message: String? = null
)

data class ProjectDto(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    val title: String,
    val description: String?,
    val budget: Double?,
    @SerializedName("skill_requirements") val skillRequirements: List<String>?,
    val constraints: String?,
    val status: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)
