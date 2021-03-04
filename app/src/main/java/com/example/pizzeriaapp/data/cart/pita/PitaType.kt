package com.example.pizzeriaapp.data.cart.pita

sealed class PitaType {
    data class Small(val size: String = "small") : PitaType() {
        override fun toString(): String {
            return "Mała"
        }
    }

    data class Big(val size: String = "big") : PitaType() {
        override fun toString(): String {
            return "Duża"
        }
    }
}