package com.example.pizzeriaapp.data.cart.pasta

import com.example.pizzeriaapp.data.cart.CartItemInterface
import com.example.pizzeriaapp.data.cart.FoodType
import com.example.pizzeriaapp.data.cart.`package`.Package
import com.example.pizzeriaapp.data.product.Pasta

data class PastaCartItem(val pasta: Pasta, val pastaType: PastaType) :
    CartItemInterface {
    override var amount: Int = 1
    override fun getDescription(): String {
        var desc = "Makaron ${pasta.name}"
        desc += "\n Opakowanie ${amount}x  ${Package.Normal().price}zł"
        return desc
    }

    override fun getPrice(): Float {
        return when (pastaType) {
            is PastaType.Small -> (pasta.smallPrice + Package.Normal().price) * amount
            is PastaType.Big -> (pasta.bigPrice + Package.Normal().price) * amount
        }
    }

    override fun getFoodType(): FoodType {
        return FoodType.Pasta
    }
}