package com.example.pizzeriaapp.data.product

import com.example.pizzeriaapp.data.order.requests.OrderRequest
import com.example.pizzeriaapp.data.order.responses.Order
import com.example.pizzeriaapp.data.order.responses.OrderResponse
import com.example.pizzeriaapp.data.order.responses.OrdersResponse
import retrofit2.http.*

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): ProductsResponse

    @POST("orders")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body order: OrderRequest
    ): OrderResponse

    @GET("users/orders")
    suspend fun getOrders(
        @Header("Authorization") token: String,
        @Query("lastFetched")
        lastFetched: String?
    ): OrdersResponse
}