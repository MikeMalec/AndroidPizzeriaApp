package com.example.pizzeriaapp.data.product

import com.google.gson.annotations.SerializedName

data class PizzaAddon(
    val id:Int,
    val name: String,
    @SerializedName("price_24")
    val price24: Float,
    @SerializedName("price_30")
    val price30: Float,
    @SerializedName("price_40")
    val price40: Float,
    @SerializedName("price_50")
    val price50: Float
)