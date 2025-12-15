package com.example.inofa_android_app.data.repository

import com.example.inofa_android_app.data.Developer
import com.example.inofa_android_app.data.PortfolioItem
import com.example.inofa_android_app.network.NetworkModule
import com.example.inofa_android_app.network.models.DeveloperDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeveloperRepository {
    private val api = NetworkModule.apiService

    suspend fun getDevelopers(skill: String? = null): Result<List<Developer>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.getDevelopers(skill)
            if (response.success && response.data != null) {
                Result.success(response.data.map { it.toDomain() })
            } else {
                Result.failure(IllegalStateException(response.message ?: "Failed to load developers"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDeveloper(id: Int): Result<Developer> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.getDeveloperById(id)
            val data = response.data
            if (response.success && data != null) {
                Result.success(data.toDomain())
            } else {
                Result.failure(IllegalStateException(response.message ?: "Developer not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private fun DeveloperDto.toDomain(): Developer {
    return Developer(
        id = id,
        email = email,
        name = name ?: "Developer $id",
        bio = bio,
        location = location,
        skills = skills ?: emptyList(),
        whatsapp = whatsapp,
        whatsappLink = whatsappLink,
        photoUrl = photoUrl,
        portfolio = portfolio?.map {
            PortfolioItem(
                id = it.id,
                title = it.title,
                description = it.description,
                link = it.link,
                imageUrl = it.imageUrl
            )
        } ?: emptyList(),
        rating = 4.8f,
        reviewCount = 0,
        projectsCompleted = 0,
        reviews = emptyList()
    )
}
