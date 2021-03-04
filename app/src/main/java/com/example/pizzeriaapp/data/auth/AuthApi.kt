package com.example.pizzeriaapp.data.auth

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body authBody: AuthRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body authBody: AuthRequest): AuthResponse

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): GenericResponse

    @POST("auth/refresh")
    suspend fun refreshToken(@Header("Authorization") token: String): AuthResponse
}