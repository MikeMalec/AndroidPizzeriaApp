package com.example.pizzeriaapp.utils.converters

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceConverter @Inject constructor() {
    fun convertPriceForUI(price: Float): String {
        val priceAsString = price.toString()
        val priceAsArray = priceAsString.split(".")
        val value = priceAsArray[1].toInt()
        if (value != 0) return "$priceAsString zł"
        return "${price.toInt()} zł"
    }
}