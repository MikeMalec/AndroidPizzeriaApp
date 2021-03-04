package com.example.pizzeriaapp.data.cart.pizza

import android.util.Log
import com.example.pizzeriaapp.data.cart.CartItemInterface
import com.example.pizzeriaapp.data.cart.FoodType
import com.example.pizzeriaapp.data.cart.`package`.Package
import com.example.pizzeriaapp.data.product.Pizza
import com.example.pizzeriaapp.data.product.PizzaAddon
import com.example.pizzeriaapp.data.product.Sauce

data class PizzaCartItem(
    val pizza: Pizza,
    var size: Int,
    val addons: List<PizzaAddon>?,
    val sauce: Sauce?,
    val sauceSize: SauceSize?,
    val breadSticks: Boolean
) :
    CartItemInterface {
    override var amount: Int = 1
    override fun getDescription(): String {
        var description = ""
        description += "${pizza.name}"
        description += "\n Rozmiar $size"
        if (addons != null && addons.isNotEmpty()) {
            description += "\n Dodatki:"
            addons.forEach { addon ->
                description += "\n ${addon.name}"
            }
        }
        if (sauce != null) {
            description += "\n sos ${sauce.name} "
        }
        if (breadSticks) {
            description += "\n paluszki"
        }
        description += "\n ${getPackageDescription()}"
        return description
    }

    override fun getPrice(): Float {
        var price = when (size) {
            24 -> pizza.size24Price
            30 -> pizza.size30Price
            40 -> pizza.size40Price
            50 -> pizza.size50Price
            else -> pizza.size24Price
        }
        if (sauce != null) {
            when (sauceSize) {
                is SauceSize.Small -> price += sauce.smallPrice
                is SauceSize.Big -> price += sauce.bigPrice
            }
        }
        if (addons != null && addons.isNotEmpty()) {
            addons.forEach {
                when (size) {
                    24 -> price += it.price24
                    30 -> price += it.price30
                    40 -> price += it.price40
                    50 -> price += it.price50
                }
            }
        }
        if (breadSticks) price += 11f
        price += getPackagePrice()
        return price * amount
    }

    private fun getPizzaPrice(): Float {
        return when (size) {
            24 -> pizza.size24Price
            30 -> pizza.size30Price
            40 -> pizza.size40Price
            50 -> pizza.size50Price
            else -> 0f
        }
    }

    override fun getFoodType(): FoodType {
        return FoodType.Pizza
    }

    private fun getPackageDescription(): String {
        return when (size) {
            24, 30 -> "Opakowanie ${amount}x ${Package.PizzaSmallMedium().price}zł"
            40, 50 -> "Opakowanie ${amount}x ${Package.PizzaBigLarge().price}zł"
            else -> ""
        }
    }

    private fun getPackagePrice(): Float {
        return when (size) {
            24, 30 -> Package.PizzaSmallMedium().price
            40, 50 -> Package.PizzaBigLarge().price
            else -> 0f
        }
    }
}