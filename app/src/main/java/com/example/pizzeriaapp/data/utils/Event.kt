package com.example.pizzeriaapp.data.utils

class Event<out T>(val value: T) {
    private var fetched = false

    fun get(): T? {
        if (fetched) return null
        return value
    }

    fun peekContent() = value
}