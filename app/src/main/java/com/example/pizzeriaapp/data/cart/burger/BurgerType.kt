package com.example.pizzeriaapp.data.cart.burger

sealed class BurgerType {
    data class Solo(val type: String = "solo") : BurgerType() {
        override fun toString(): String {
            return "Solo"
        }
    }

    data class Set(val type: String = "set") : BurgerType() {
        override fun toString(): String {
            return "Zestaw"
        }
    }
}