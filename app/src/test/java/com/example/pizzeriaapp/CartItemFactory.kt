package com.example.pizzeriaapp

import com.example.pizzeriaapp.data.cart.beverage.BeverageCartItem
import com.example.pizzeriaapp.data.cart.burger.BurgerCartItem
import com.example.pizzeriaapp.data.cart.burger.BurgerType
import com.example.pizzeriaapp.data.cart.pasta.PastaCartItem
import com.example.pizzeriaapp.data.cart.pasta.PastaType
import com.example.pizzeriaapp.data.cart.pita.PitaCartItem
import com.example.pizzeriaapp.data.cart.pita.PitaType
import com.example.pizzeriaapp.data.cart.pizza.PizzaCartItem
import com.example.pizzeriaapp.data.cart.pizza.SauceSize
import com.example.pizzeriaapp.data.cart.salad.SaladCartItem
import com.example.pizzeriaapp.data.product.*

class CartItemFactory {
    companion object {
        fun getSamePizzas(amount: Int): List<PizzaCartItem> {
            return (0..amount).map {
                PizzaCartItem(
                    Pizza(1, 1, "name", listOf("cheese"), 1f, 1f, 1f, 1f),
                    24,
                    listOf(PizzaAddon(1, "name", 1f, 1f, 1f, 1f)),
                    Sauce(1, "sauce", 1f, 1f),
                    SauceSize.Small(),
                    false
                )
            }
        }

        fun getPizzas(amount: Int): List<PizzaCartItem> {
            return (0..amount).map {
                PizzaCartItem(
                    Pizza(it, 1, "name", listOf("cheese"), 1f, 1f, 1f, 1f),
                    24,
                    listOf(PizzaAddon(1, "name", 1f, 1f, 1f, 1f)),
                    Sauce(1, "sauce", 1f, 1f),
                    SauceSize.Small(),
                    false
                )
            }
        }

        fun getSameBurgers(amount: Int): List<BurgerCartItem> {
            return (0..amount).map {
                BurgerCartItem(Burger(1, 1, "burger", listOf("cheese"), 1f, 1f), BurgerType.Solo())
            }
        }

        fun getBurgers(amount: Int): List<BurgerCartItem> {
            return (0..amount).map {
                BurgerCartItem(Burger(it, 1, "burger", listOf("cheese"), 1f, 1f), BurgerType.Solo())
            }
        }

        fun getSamePastas(amount: Int): List<PastaCartItem> {
            return (0..amount).map {
                PastaCartItem(Pasta(1, 1, "pasta", listOf("cheese"), 1f, 1f), PastaType.Small())
            }
        }

        fun getPastas(amount: Int): List<PastaCartItem> {
            return (0..amount).map {
                PastaCartItem(Pasta(it, 1, "pasta", listOf("cheese"), 1f, 1f), PastaType.Small())
            }
        }

        fun getSamePitas(amount: Int): List<PitaCartItem> {
            return (0..amount).map {
                PitaCartItem(Pita(1, 1, "pita", listOf("cheese"), 1f, 1f), PitaType.Small())
            }
        }

        fun getPitas(amount: Int): List<PitaCartItem> {
            return (0..amount).map {
                PitaCartItem(Pita(it, 1, "pita", listOf("cheese"), 1f, 1f), PitaType.Small())
            }
        }

        fun getSameSalads(amount: Int): List<SaladCartItem> {
            return (0..amount).map {
                SaladCartItem(Salad(1, 1, "salad", listOf("salad"), 1f))
            }
        }

        fun getSalads(amount: Int): List<SaladCartItem> {
            return (0..amount).map {
                SaladCartItem(Salad(it, 1, "salad", listOf("salad"), 1f))
            }
        }

        fun getSameBeverages(amount: Int): List<BeverageCartItem> {
            return (0..amount).map {
                BeverageCartItem(Beverage(1, "beverage", "", "", null, null, null, 1f, null, true))
            }
        }

        fun getBeverages(amount: Int): List<BeverageCartItem> {
            return (0..amount).map {
                BeverageCartItem(Beverage(it, "beverage", "", "", null, null, null, 1f, null, true))
            }
        }
    }
}