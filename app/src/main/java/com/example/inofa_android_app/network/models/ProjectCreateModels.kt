package com.example.inofa_android_app.network.models

import com.google.gson.annotations.SerializedName

data class ProjectCreateRequest(
    val title: String,
    val description: String?,
    val budget: Double?,
    @SerializedName("skill_requirements") val skillRequirements: List<String>,
    val constraints: String?
)

data class ProjectCreateResponse(
    val success: Boolean,
    val message: String? = null,
    val data: ProjectDto? = null
)
