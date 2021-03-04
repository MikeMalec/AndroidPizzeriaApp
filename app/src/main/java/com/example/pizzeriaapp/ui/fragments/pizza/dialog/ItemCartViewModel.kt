package com.example.pizzeriaapp.ui.fragments.pizza.dialog

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.pizzeriaapp.data.cart.pizza.PizzaCartItem
import com.example.pizzeriaapp.data.cart.pizza.SauceSize
import com.example.pizzeriaapp.data.product.Pizza
import com.example.pizzeriaapp.data.product.PizzaAddon
import com.example.pizzeriaapp.data.product.Sauce
import kotlinx.coroutines.channels.Channel

class ItemCartViewModel @ViewModelInject constructor() : ViewModel() {
    val pizzaCartItem = Channel<PizzaCartItem>(Channel.CONFLATED)
    val createCartItemValidation = Channel<String>(Channel.CONFLATED)

    var pizza: Pizza? = null

    var size: Int? = null

    var pizzaAddons = mutableListOf<PizzaAddon>()

    var sauce: Sauce? = null

    var sauceSize: SauceSize? = null

    var breadSticks: Boolean = false

    fun pizzaAddonAction(addon: PizzaAddon) {
        if (pizzaAddons.contains(addon)) {
            removeAddon(addon)
        } else {
            addAddon(addon)
        }
    }

    private fun addAddon(addon: PizzaAddon) {
        pizzaAddons.add(addon)
    }

    private fun removeAddon(addon: PizzaAddon) {
        pizzaAddons.remove(addon)
    }

    fun addSauce(sauce: Sauce) {
        if (this.sauce == sauce) {
            this.sauce = null
        } else {
            this.sauce = sauce
        }
    }

    fun createPizzaCartItem() {
        if (size != null) {
            if (sauce != null && sauceSize == null) {
                createCartItemValidation.offer("Wybierz rozmiar sosu")
            } else {
                val cartItem =
                    PizzaCartItem(pizza!!, size!!, pizzaAddons, sauce, sauceSize, breadSticks)
                pizzaCartItem.offer(cartItem)
            }
        } else {
            createCartItemValidation.offer("Wybierz rozmiar pizzy")
        }
    }

    fun clearPizzaCartInfo() {
        pizza = null
        size = null
        pizzaAddons = mutableListOf()
        sauce = null
        breadSticks = false
    }
}