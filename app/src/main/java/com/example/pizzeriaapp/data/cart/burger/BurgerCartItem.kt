package com.example.pizzeriaapp.data.cart.burger

import com.example.pizzeriaapp.data.cart.CartItemInterface
import com.example.pizzeriaapp.data.cart.FoodType
import com.example.pizzeriaapp.data.cart.`package`.Package
import com.example.pizzeriaapp.data.product.Burger

data class BurgerCartItem(val burger: Burger, val type: BurgerType) :
    CartItemInterface {
    override var amount: Int = 1
    override fun getDescription(): String {
        var desc = ""
        desc += "${burger.name}"
        desc += "\n Opakowanie ${amount}x ${Package.Normal().price}zł"
        return desc
    }

    override fun getPrice(): Float {
        return when (type) {
            is BurgerType.Set -> (burger.setPrice + Package.Normal().price) * amount
            is BurgerType.Solo -> (burger.soloPrice + Package.Normal().price) * amount
        }
    }

    override fun getFoodType(): FoodType {
        return FoodType.Burger
    }
}