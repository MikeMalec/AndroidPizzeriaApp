package com.example.pizzeriaapp.data.cart.pita

import com.example.pizzeriaapp.data.cart.CartItemInterface
import com.example.pizzeriaapp.data.cart.FoodType
import com.example.pizzeriaapp.data.cart.`package`.Package
import com.example.pizzeriaapp.data.product.Pita

data class PitaCartItem(val pita: Pita, val type: PitaType) :
    CartItemInterface {
    override var amount: Int = 1
    override fun getDescription(): String {
        var desc = "Pita ${pita.name}"
        desc += "\n Opakowanie ${amount}x ${Package.Normal().price}zł"
        return desc
    }

    override fun getPrice(): Float {
        return when (type) {
            is PitaType.Small -> (pita.smallPrice + Package.Normal().price) * amount
            is PitaType.Big -> (pita.bigPrice + Package.Normal().price) * amount
        }
    }

    override fun getFoodType(): FoodType {
        return FoodType.Pita
    }
}