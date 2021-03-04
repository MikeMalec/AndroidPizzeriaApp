package com.example.pizzeriaapp.data.cart.beverage

import com.example.pizzeriaapp.data.cart.CartItemInterface
import com.example.pizzeriaapp.data.cart.FoodType
import com.example.pizzeriaapp.data.product.Beverage

data class BeverageCartItem(val beverage: Beverage) : CartItemInterface {
    override var amount: Int = 1

    override fun getDescription(): String {
        return beverage.name
    }

    override fun getPrice(): Float {
        return amount * beverage.price!!
    }

    override fun getFoodType(): FoodType {
        return FoodType.Beverage
    }
}