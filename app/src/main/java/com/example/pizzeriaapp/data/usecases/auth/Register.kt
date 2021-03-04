package com.example.pizzeriaapp.data.usecases.auth

import com.example.pizzeriaapp.data.auth.AuthRequest
import com.example.pizzeriaapp.data.auth.AuthResponse
import com.example.pizzeriaapp.data.auth.AuthService
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.data.utils.Resource.Loading
import com.example.pizzeriaapp.data.utils.safeApiCall
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Register @Inject constructor(val authService: AuthService) {
    fun register(email: String, password: String): Flow<Resource<AuthResponse?>> = flow {
        emit(Loading())
        val response = safeApiCall(IO) { authService.register(AuthRequest(email, password)) }
        emit(response)
    }
}