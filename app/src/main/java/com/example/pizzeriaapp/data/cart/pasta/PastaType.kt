package com.example.pizzeriaapp.data.cart.pasta

sealed class PastaType {
    data class Small(val size: String = "small") : PastaType() {
        override fun toString(): String {
            return "Mały"
        }
    }

    data class Big(val size: String = "big") : PastaType() {
        override fun toString(): String {
            return "Duży"
        }
    }
}