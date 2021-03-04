package com.example.pizzeriaapp.data.product

sealed class AlcoholViewItem {
    data class Header(val type: AlcoholType) : AlcoholViewItem()
    data class Item(val beverage: Beverage) : AlcoholViewItem()
}