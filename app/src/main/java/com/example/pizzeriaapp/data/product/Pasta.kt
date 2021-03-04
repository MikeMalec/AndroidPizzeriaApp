package com.example.pizzeriaapp.data.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pasta(
    val id: Int,
    val number: Int,
    val name: String,
    val ingredients: List<String>,
    @SerializedName(value = "small_price")
    val smallPrice: Float,
    @SerializedName(value = "big_price")
    val bigPrice: Float
) : Parcelable