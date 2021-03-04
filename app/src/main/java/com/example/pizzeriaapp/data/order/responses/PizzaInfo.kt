package com.example.pizzeriaapp.data.order.responses
import com.google.gson.annotations.SerializedName

data class PizzaInfo(
    val pizza: Pizza,
    @SerializedName("pizza_size")
    val pizzaSize: Int,
    val amount: Int,
    val addons: List<Addon>,
    val sauce: Sauce?,
    @SerializedName("sauce_size")
    val sauceSize: String?,
    val breadsticks: Boolean,
)