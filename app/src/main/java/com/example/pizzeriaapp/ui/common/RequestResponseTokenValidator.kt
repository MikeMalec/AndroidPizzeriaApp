package com.example.pizzeriaapp.ui.common

import com.example.pizzeriaapp.data.preferences.DataStoreRepository
import com.example.pizzeriaapp.data.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestResponseTokenValidator @Inject constructor(val dataStoreRepository: DataStoreRepository) {

    private val job = Job()

    fun validateResponse(response: Resource<*>) {
        if (response is Resource.Error) {
            response.error?.also { err ->
                if (err.contains("Not authorized")) {
                    CoroutineScope(IO + job).launch {
                        dataStoreRepository.clearToken()
                    }
                }
            }
        }
    }
}