package com.example.pizzeriaapp.data.order.responses

import com.google.gson.annotations.SerializedName

data class FoodInfo(
    val name: String,
    val size: String,
    val amount: Int,
    @SerializedName("food_type")
    val foodType: String
)