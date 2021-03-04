package com.example.pizzeriaapp.data.cart

sealed class FoodType {
    object Pizza : FoodType()
    object Burger : FoodType()
    object Pasta : FoodType()
    object Pita : FoodType()
    object Salad : FoodType()
    object Beverage : FoodType()
}