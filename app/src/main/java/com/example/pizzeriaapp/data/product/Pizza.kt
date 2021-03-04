package com.example.pizzeriaapp.data.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pizza(
    val id: Int,
    val number: Int,
    val name: String,
    val ingredients: List<String>,
    @SerializedName(value = "size_24_price")
    val size24Price: Float,
    @SerializedName(value = "size_30_price")
    val size30Price: Float,
    @SerializedName(value = "size_40_price")
    val size40Price: Float,
    @SerializedName(value = "size_50_price")
    val size50Price: Float
) : Parcelable