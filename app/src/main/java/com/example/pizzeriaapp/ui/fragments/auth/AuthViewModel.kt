package com.example.pizzeriaapp.ui.fragments.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzeriaapp.data.auth.AuthResponse
import com.example.pizzeriaapp.data.auth.GenericResponse
import com.example.pizzeriaapp.data.preferences.DataStoreRepository
import com.example.pizzeriaapp.data.usecases.auth.Login
import com.example.pizzeriaapp.data.usecases.auth.Logout
import com.example.pizzeriaapp.data.usecases.auth.RefreshToken
import com.example.pizzeriaapp.data.usecases.auth.Register
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.ui.common.TokenManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
    private val tokenManager: TokenManager,
    private val register: Register,
    private val login: Login,
    private val logout: Logout,
    private val refreshToken: RefreshToken
) :
    ViewModel() {

    val token = tokenManager.getTokenFlow()

    var registerRequestState = Channel<Resource<AuthResponse?>>()

    fun setRegisterChannel() {
        registerRequestState = Channel<Resource<AuthResponse?>>()
    }

    fun register(email: String, password: String) {
        viewModelScope.launch(IO) {
            register.register(email, password).collect {
                registerRequestState.send(it)
                dispatchResponse(it)
            }
        }
    }

    var loginRequestState = Channel<Resource<AuthResponse?>>()

    fun setLoginChannel() {
        loginRequestState = Channel<Resource<AuthResponse?>>()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(IO) {
            login.login(email, password).collect {
                loginRequestState.send(it)
                dispatchResponse(it)
            }
        }
    }

    private fun dispatchResponse(response: Resource<AuthResponse?>) {
        if (response is Resource.Success) {
            response.data?.also { resp ->
                tokenManager.saveToken(resp.token)
            }
        }
    }

    var logoutRequestState = Channel<Resource<GenericResponse?>>()

    fun setLogoutChannel() {
        logoutRequestState = Channel<Resource<GenericResponse?>>()
    }

    fun logout() {
        viewModelScope.launch(IO) {
            logout.logout(tokenManager.token!!).collect {
                logoutRequestState.send(it)
                if (it is Resource.Success) {
                    tokenManager.clearToken()
                }
                if (it is Resource.Error) {
                    it.error?.also { err ->
                        if (err.contains("Not authorized")) {
                            tokenManager.clearToken()
                        }
                    }
                }
            }
        }
    }

    private var refreshedToken = false

    fun refreshToken() {
        viewModelScope.launch(IO) {
            if (tokenManager.token !== null && !refreshedToken) {
                refreshedToken = true
                val newToken = refreshToken.refreshToken(tokenManager.token!!)
                newToken?.also {
                    tokenManager.saveToken(newToken)
                }
            }
        }
    }
}