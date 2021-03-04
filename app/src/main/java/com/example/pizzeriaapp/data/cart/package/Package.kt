package com.example.pizzeriaapp.data.cart.`package`

sealed class Package {
    data class PizzaSmallMedium(val price: Float = 1.5f)
    data class PizzaBigLarge(val price: Float = 2f)
    data class Normal(val price: Float = 1f)
}