package com.example.inofa_android_app.data.repository

import com.example.inofa_android_app.data.TokenStorage
import com.example.inofa_android_app.data.UserRoleStorage
import com.example.inofa_android_app.network.NetworkModule
import com.example.inofa_android_app.network.models.AuthResponse
import com.example.inofa_android_app.network.models.LoginRequest
import com.example.inofa_android_app.network.models.RegisterRequest
import com.example.inofa_android_app.network.models.SetRoleRequest
import com.example.inofa_android_app.network.models.StatusResponse

class AuthRepository {
    private val api = NetworkModule.apiService

    suspend fun login(email: String, password: String): AuthResponse {
        val response = api.login(LoginRequest(email = email, password = password))
        response.data?.token?.let { TokenStorage.saveToken(it) }
        response.data?.user?.role?.let { UserRoleStorage.saveRole(it) }
        response.data?.user?.email?.let { UserRoleStorage.saveEmail(it) }
        return response
    }

    suspend fun register(email: String, password: String, role: String? = null): AuthResponse {
        val response = api.register(RegisterRequest(email = email, password = password, role = role))
        response.data?.token?.let { TokenStorage.saveToken(it) }
        response.data?.user?.role?.let { UserRoleStorage.saveRole(it) }
        response.data?.user?.email?.let { UserRoleStorage.saveEmail(it) }
        return response
    }

    suspend fun setRole(role: String): StatusResponse {
        val response = api.setRole(SetRoleRequest(role = role))
        if (response.success) UserRoleStorage.saveRole(role)
        return response
    }

    suspend fun status(): StatusResponse = api.status()

    fun logout() {
        TokenStorage.clear()
        UserRoleStorage.clear()
    }
}
