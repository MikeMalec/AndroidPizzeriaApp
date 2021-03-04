package com.example.pizzeriaapp.data.cart.pizza

sealed class SauceSize {
    data class Small(val size: String = "small") : SauceSize()
    data class Big(val size: String = "big") : SauceSize()
}