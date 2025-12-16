package com.example.inofa_android_app.network.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val role: String? = null
)

data class SetRoleRequest(
    val role: String
)

data class StatusResponse(
    val success: Boolean,
    val status: String?,
    val role_missing: Boolean? = null,
    val profile_missing: Boolean? = null,
    val message: String? = null
)

data class AuthResponse(
    val success: Boolean,
    val message: String?,
    val data: AuthData?
)

data class AuthData(
    val user: AuthUser?,
    val token: String?
)

data class AuthUser(
    val id: Int,
    val email: String,
    val role: String?
)
