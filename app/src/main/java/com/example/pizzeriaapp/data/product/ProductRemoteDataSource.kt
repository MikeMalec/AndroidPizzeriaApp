package com.example.pizzeriaapp.data.product

import com.example.pizzeriaapp.data.order.requests.OrderRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRemoteDataSource @Inject constructor(val productApi: ProductApi) {
    suspend fun getProducts() = productApi.getProducts()

    suspend fun createOrder(token: String, orderRequest: OrderRequest) =
        productApi.createOrder(token, orderRequest)

    suspend fun getOrders(token: String, lastFetched: String?) =
        productApi.getOrders(token, lastFetched)
}