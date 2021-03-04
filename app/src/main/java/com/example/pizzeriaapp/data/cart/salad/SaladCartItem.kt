package com.example.pizzeriaapp.data.cart.salad

import com.example.pizzeriaapp.data.cart.CartItemInterface
import com.example.pizzeriaapp.data.cart.FoodType
import com.example.pizzeriaapp.data.cart.`package`.Package
import com.example.pizzeriaapp.data.product.Salad

data class SaladCartItem(val salad: Salad) : CartItemInterface {
    override var amount: Int = 1
    override fun getDescription(): String {
        var desc = "Sałatka ${salad.name}"
        desc += "\n Opakowanie ${amount}x ${Package.Normal().price}zł"
        return desc
    }

    override fun getPrice(): Float {
        return (salad.price + Package.Normal().price) * amount
    }

    override fun getFoodType(): FoodType {
        return FoodType.Salad
    }
}