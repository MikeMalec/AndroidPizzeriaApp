package com.example.pizzeriaapp.data.usecases.auth

import com.example.pizzeriaapp.data.auth.AuthService
import com.example.pizzeriaapp.data.auth.GenericResponse
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.data.utils.Resource.Loading
import com.example.pizzeriaapp.data.utils.safeApiCall
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Logout @Inject constructor(val authService: AuthService) {
    fun logout(token: String): Flow<Resource<GenericResponse?>> = flow {
        val response = safeApiCall(IO) { authService.logout("Bearer $token") }
        emit(response)
    }
}