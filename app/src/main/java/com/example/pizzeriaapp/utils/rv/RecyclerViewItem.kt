package com.example.pizzeriaapp.utils.rv

sealed class RecyclerViewItem<out T> {
    object Loading : RecyclerViewItem<Nothing>()
    data class Item<T>(val data: T) : RecyclerViewItem<T>()
}