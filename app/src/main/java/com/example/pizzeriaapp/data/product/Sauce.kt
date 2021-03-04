package com.example.pizzeriaapp.data.product

import com.google.gson.annotations.SerializedName

data class Sauce(
    val id: Int,
    val name: String,
    @SerializedName("small_price")
    val smallPrice: Float,
    @SerializedName("big_price")
    val bigPrice: Float
)