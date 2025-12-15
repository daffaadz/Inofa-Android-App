package com.example.inofa_android_app.network

import com.example.inofa_android_app.network.models.AuthResponse
import com.example.inofa_android_app.network.models.DeveloperDetailResponse
import com.example.inofa_android_app.network.models.DeveloperListResponse
import com.example.inofa_android_app.network.models.LoginRequest
import com.example.inofa_android_app.network.models.RegisterRequest
import com.example.inofa_android_app.network.models.SetRoleRequest
import com.example.inofa_android_app.network.models.StatusResponse
import com.example.inofa_android_app.network.models.ProfileCreateRequest
import com.example.inofa_android_app.network.models.ProfileResponse
import com.example.inofa_android_app.network.models.ProjectListResponse
import com.example.inofa_android_app.network.models.ProjectCreateRequest
import com.example.inofa_android_app.network.models.ProjectCreateResponse
import com.example.inofa_android_app.network.models.PortfolioCreateRequest
import com.example.inofa_android_app.network.models.PortfolioListResponse
import com.example.inofa_android_app.network.models.PortfolioCreateResponse
import com.example.inofa_android_app.network.models.PortfolioDeleteResponse
import com.example.inofa_android_app.network.models.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("auth/set-role")
    suspend fun setRole(@Body body: SetRoleRequest): StatusResponse

    @GET("auth/status")
    suspend fun status(): StatusResponse

    @GET("developer/all")
    suspend fun getDevelopers(@Query("skill") skill: String? = null): DeveloperListResponse

    @GET("developer/{id}")
    suspend fun getDeveloperById(@Path("id") id: Int): DeveloperDetailResponse

    @POST("profile")
    suspend fun createProfile(@Body body: ProfileCreateRequest): ProfileResponse

    @GET("profile/me")
    suspend fun getMyProfile(): ProfileResponse

    @PUT("profile")
    suspend fun updateProfile(@Body body: ProfileCreateRequest): ProfileResponse

    @GET("project/me")
    suspend fun getMyProjects(): ProjectListResponse

    @GET("project/all")
    suspend fun getAllProjects(): ProjectListResponse

    @POST("project")
    suspend fun createProject(@Body body: ProjectCreateRequest): ProjectCreateResponse

    @POST("portfolio")
    suspend fun createPortfolio(@Body body: PortfolioCreateRequest): PortfolioCreateResponse

    @GET("portfolio/me")
    suspend fun getMyPortfolio(): PortfolioListResponse

    @GET("portfolio/{id}")
    suspend fun getPortfolioById(@Path("id") id: Int): PortfolioCreateResponse

    @PUT("portfolio/{id}")
    suspend fun updatePortfolio(@Path("id") id: Int, @Body body: PortfolioCreateRequest): PortfolioCreateResponse

    @DELETE("portfolio/{id}")
    suspend fun deletePortfolio(@Path("id") id: Int): PortfolioDeleteResponse

    @retrofit2.http.Multipart
    @POST("upload/image")
    suspend fun uploadImage(@retrofit2.http.Part image: MultipartBody.Part): UploadImageResponse
}
