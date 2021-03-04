package com.example.pizzeriaapp.data.order.responses

data class OrderInfo(
    val id: Int,
    var status: String,
    val street: String,
    val houseNumber: String,
    val city: String,
    val phoneNumber: String,
    val price: Float,
    val createdAt: String,
    val updatedAt: String
)