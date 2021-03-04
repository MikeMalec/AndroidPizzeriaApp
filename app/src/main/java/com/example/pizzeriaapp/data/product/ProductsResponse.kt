package com.example.pizzeriaapp.data.product

data class ProductsResponse(
    val pizza: List<Pizza>,
    val pizzaAddons: List<PizzaAddon>,
    val sauces: List<Sauce>,
    val burger: List<Burger>,
    val pasta: List<Pasta>,
    val pita: List<Pita>,
    val salad: List<Salad>,
    val beverage: List<Beverage>
)