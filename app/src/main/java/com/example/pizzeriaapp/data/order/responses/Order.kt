package com.example.pizzeriaapp.data.order.responses

data class Order(
    val orderInfo: OrderInfo,
    val pizzas: List<PizzaInfo>,
    val burgers: List<FoodInfo>,
    val pastas: List<FoodInfo>,
    val pitas: List<FoodInfo>,
    val salads: List<FoodInfo>,
    val beverages: List<FoodInfo>
) {
    fun getDescription(): String {
        var desc = ""
        pizzas.forEachIndexed { index, pizzaInfo ->
            desc += "${if (index == 0) "" else "\n"} ${if (!pizzaInfo.pizza.name.contains("pizza")) "Pizza" else ""} ${pizzaInfo.pizza.name} rozmiar: ${pizzaInfo.pizzaSize} ilość: ${pizzaInfo.amount}"
            if (pizzaInfo.sauce != null) {
                when (pizzaInfo.sauceSize) {
                    "small" -> desc += "\n Sos: ${pizzaInfo.sauce.name} rozmiar: mały"
                    "big" -> desc += "\n Sos: ${pizzaInfo.sauce.name} rozmiar: duży"
                }
            }
            if (pizzaInfo.addons.isNotEmpty()) {
                desc += "\n Dodatki:"
                pizzaInfo.addons.forEach { addon ->
                    desc += "\n ${addon.name}"
                }
            }
        }
        burgers.forEach {
            when (it.size) {
                "small" -> desc += "\n ${it.name} ilość: ${it.amount}"
                "big" -> desc += "\n ${it.name} w zestawie ilość: ${it.amount}"
            }
        }
        pastas.forEach {
            when (it.size) {
                "small" -> desc += "\n Makaron ${it.name} mały ilość: ${it.amount}"
                "big" -> desc += "\n Makaron ${it.name} duży ilość: ${it.amount}"
            }
        }
        pitas.forEach {
            when (it.size) {
                "small" -> desc += "\n Pita ${it.name} mała ilość: ${it.amount}"
                "big" -> desc += "\n Pita ${it.name} duża ilość: ${it.amount}"
            }
        }
        salads.forEach {
            desc += "\n Sałatka ${it.name} ilość: ${it.amount}"
        }
        beverages.forEach {
            desc += "\n ${it.name} rozmiar: ${it.size} ilość: ${it.amount}"
        }
        return desc
    }
}