package com.example.pizzeriaapp.utils.date

import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateConverter @Inject constructor() {
    fun mapDateToYearMonthHours(date: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val parsedDate = sdf.parse(date)
        val requiredFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return requiredFormat.format(parsedDate)
    }

    fun mapDateToHours(date: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val parsedDate = sdf.parse(date)
        val requiredFormat = SimpleDateFormat("HH:mm:ss")
        return requiredFormat.format(parsedDate)
    }
}