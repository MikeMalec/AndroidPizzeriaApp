package com.example.pizzeriaapp.data.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Beverage(
    val id: Int,
    val name: String,
    val type: String,
    @SerializedName(value = "alcohol_type")
    val alcoholType: String,
    val size: String?,
    @SerializedName(value = "small_price")
    val smallPrice: Float?,
    @SerializedName(value = "big_price")
    val bigPrice: Float?,
    val price: Float?,
    val ingredients: List<String>?,
    val orderable: Boolean
) : Parcelable