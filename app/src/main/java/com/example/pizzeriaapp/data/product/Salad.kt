package com.example.pizzeriaapp.data.product

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Salad(
    val id: Int,
    val number: Int,
    val name: String,
    val ingredients: List<String>,
    val price: Float
) : Parcelable