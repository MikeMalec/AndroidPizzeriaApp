package com.example.pizzeriaapp.data.product

data class AlcoholGroup(val type: AlcoholType, val alcohols: List<Beverage>)

sealed class AlcoholType {
    class Normal : AlcoholType() {
        override fun toString(): String {
            return "Alkohole"
        }
    }

    class Draught : AlcoholType() {
        override fun toString(): String {
            return "Piwa Lane"
        }
    }

    class Bottled : AlcoholType() {
        override fun toString(): String {
            return "Piwa Butelkowe"
        }
    }

    class Wine : AlcoholType() {
        override fun toString(): String {
            return "Wina"
        }
    }

    class Canned : AlcoholType() {
        override fun toString(): String {
            return "W puszce"
        }
    }
}