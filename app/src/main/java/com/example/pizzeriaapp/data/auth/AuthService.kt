package com.example.pizzeriaapp.data.auth

import javax.inject.Inject

class AuthService @Inject constructor(val authApi: AuthApi) {
    suspend fun register(authRequest: AuthRequest): AuthResponse = authApi.register(authRequest)

    suspend fun login(authRequest: AuthRequest): AuthResponse = authApi.login(authRequest)

    suspend fun logout(token: String) = authApi.logout(token)

    suspend fun refreshToken(token: String) = authApi.refreshToken(token)
}