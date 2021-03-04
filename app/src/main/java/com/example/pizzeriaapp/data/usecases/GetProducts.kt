package com.example.pizzeriaapp.data.usecases

import com.example.pizzeriaapp.data.product.ProductRemoteDataSource
import com.example.pizzeriaapp.data.product.ProductsResponse
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.data.utils.Resource.Loading
import com.example.pizzeriaapp.data.utils.safeApiCall
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProducts @Inject constructor(val productRemoteDataSource: ProductRemoteDataSource) {
    fun getProducts(): Flow<Resource<ProductsResponse?>> = flow {
        emit(Loading())
        val response = safeApiCall(IO) { productRemoteDataSource.getProducts() }
        emit(response)
    }
}