package com.example.pizzeriaapp.data.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Burger(
    val id: Int,
    val number: Int,
    val name: String,
    val ingredients: List<String>,
    @SerializedName(value = "solo_price")
    val soloPrice: Float,
    @SerializedName(value = "set_price")
    val setPrice: Float
) : Parcelable