package com.example.inofa_android_app.data.repository

import com.example.inofa_android_app.data.Project
import com.example.inofa_android_app.network.NetworkModule
import com.example.inofa_android_app.network.models.ProjectDto
import com.example.inofa_android_app.network.models.ProjectCreateRequest
import com.example.inofa_android_app.network.models.ProjectCreateResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjectRepository {
    private val api = NetworkModule.apiService

    suspend fun getMyProjects(): Result<List<Project>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMyProjects()
            if (response.success && response.data != null) {
                Result.success(response.data.map { it.toDomain() })
            } else {
                Result.failure(IllegalStateException(response.message ?: "Gagal memuat proyek"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllProjects(): Result<List<Project>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllProjects()
            if (response.success && response.data != null) {
                Result.success(response.data.map { it.toDomain() })
            } else {
                Result.failure(IllegalStateException(response.message ?: "Gagal memuat proyek"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProject(request: ProjectCreateRequest): Result<ProjectCreateResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.createProject(request)
            if (response.success) Result.success(response)
            else Result.failure(IllegalStateException(response.message ?: "Gagal membuat proyek"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private fun ProjectDto.toDomain(): Project = Project(
    id = id,
    userId = userId,
    title = title,
    description = description ?: "Deskripsi belum tersedia",
    budget = budget,
    skillRequirements = skillRequirements ?: emptyList(),
    constraints = constraints,
    createdAt = createdAt ?: "",
    updatedAt = updatedAt ?: ""
)
