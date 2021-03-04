package com.example.pizzeriaapp.data.usecases

import com.example.pizzeriaapp.data.order.responses.OrdersResponse
import com.example.pizzeriaapp.data.product.ProductRemoteDataSource
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.data.utils.Resource.Loading
import com.example.pizzeriaapp.data.utils.safeApiCall
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOrders @Inject constructor(
    val productRemoteDataSource: ProductRemoteDataSource
) {
    fun getOrders(token: String, lastFetched: String?): Flow<Resource<OrdersResponse?>> = flow {
        emit(Loading())
        val response =
            safeApiCall(IO) { productRemoteDataSource.getOrders("Bearer $token", lastFetched) }
        emit(response)
    }
}