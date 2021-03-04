package com.example.pizzeriaapp.data.cart.payment

sealed class PaymentMethod {
    class Cash : PaymentMethod() {
        override fun toString(): String {
            return "Gotówka"
        }
    }

    class Cart : PaymentMethod() {
        override fun toString(): String {
            return "Karta"
        }
    }
}