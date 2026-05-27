package com.hnure.smartlock.data.repository

import com.hnure.smartlock.data.api.AuthResponse
import com.hnure.smartlock.data.api.LoginRequest
import com.hnure.smartlock.data.api.RegisterRequest
import com.hnure.smartlock.data.api.SmartLockApi
import com.hnure.smartlock.data.local.TokenDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: SmartLockApi,
    private val tokenDataStore: TokenDataStore
) {
    val jwtToken: Flow<String?> = tokenDataStore.jwtToken
    val userEmail: Flow<String?> = tokenDataStore.userEmail
    val userFullName: Flow<String?> = tokenDataStore.userFullName
    val userId: Flow<String?> = tokenDataStore.userId

    suspend fun login(email: String, password: String): AuthResponse {
        val response = api.login(LoginRequest(email, password))
        if (response.isSuccessful) {
            val body = response.body()!!
            tokenDataStore.saveSession(body.jwtToken, body.id, body.email, body.fullName)
            return body
        } else {
            throw Exception("Невірний email або пароль (${response.code()})")
        }
    }

    suspend fun register(email: String, password: String, fullName: String): AuthResponse {
        val response = api.register(RegisterRequest(email, password, fullName))
        if (response.isSuccessful) {
            val body = response.body()!!
            tokenDataStore.saveSession(body.jwtToken, body.id, body.email, body.fullName)
            return body
        } else {
            val errMsg = response.errorBody()?.string() ?: "Помилка реєстрації"
            throw Exception(errMsg)
        }
    }

    suspend fun logout() {
        tokenDataStore.clearSession()
    }

    suspend fun isLoggedIn(): Boolean {
        return !tokenDataStore.jwtToken.first().isNullOrEmpty()
    }
}
