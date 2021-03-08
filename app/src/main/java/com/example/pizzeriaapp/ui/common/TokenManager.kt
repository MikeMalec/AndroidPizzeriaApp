package com.example.pizzeriaapp.ui.common

import com.example.pizzeriaapp.data.preferences.DataStoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class TokenManager @Inject constructor(val dataStoreRepository: DataStoreRepository) {
    var token: String? = null

    val job = Job()

    init {
        observeToken()
    }

    fun getTokenFlow() = dataStoreRepository.getToken()

    private fun observeToken() {
        CoroutineScope(IO + job).launch {
            dataStoreRepository.getToken().catch { }.collect {
                token = it
            }
        }
    }

    fun clearToken() {
        CoroutineScope(IO + job).launch {
            dataStoreRepository.clearToken()
        }
    }

    fun saveToken(token: String) {
        CoroutineScope(IO + job).launch {
            dataStoreRepository.saveToken(token)
        }
    }
}