package com.example.pizzeriaapp.data.usecases.auth

import com.example.pizzeriaapp.data.auth.AuthService
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.data.utils.safeApiCall
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

class RefreshToken @Inject constructor(val authService: AuthService) {
    suspend fun refreshToken(token: String): String? {
        val response = safeApiCall(IO) { authService.refreshToken("Bearer $token") }
        if (response is Resource.Success) {
            if (response.data != null) {
                return response.data.token
            }
        }
        return null
    }
}