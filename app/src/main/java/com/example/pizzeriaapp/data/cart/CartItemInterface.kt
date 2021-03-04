package com.example.pizzeriaapp.data.cart

interface CartItemInterface {
    fun getDescription(): String
    fun getPrice(): Float
    fun getFoodType(): FoodType
    var amount: Int
}