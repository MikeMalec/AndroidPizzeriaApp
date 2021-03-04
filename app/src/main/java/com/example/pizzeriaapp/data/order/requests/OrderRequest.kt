package com.example.pizzeriaapp.data.order.requests

import com.example.pizzeriaapp.data.cart.beverage.BeverageCartItem
import com.example.pizzeriaapp.data.cart.burger.BurgerCartItem
import com.example.pizzeriaapp.data.cart.pasta.PastaCartItem
import com.example.pizzeriaapp.data.cart.pita.PitaCartItem
import com.example.pizzeriaapp.data.cart.pizza.PizzaCartItem
import com.example.pizzeriaapp.data.cart.salad.SaladCartItem
import com.example.pizzeriaapp.data.preferences.UserInfo

data class OrderRequest(
    val userInfo: UserInfo,
    val paymentMethod: String,
    val pizza: List<PizzaCartItem>,
    val burger: List<BurgerCartItem>,
    val pasta: List<PastaCartItem>,
    val pita: List<PitaCartItem>,
    val salad: List<SaladCartItem>,
    val beverage: List<BeverageCartItem>
)