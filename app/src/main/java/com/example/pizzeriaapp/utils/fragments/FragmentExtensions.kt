package com.example.pizzeriaapp.utils.fragments

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.getDrawable(drawable: Int): Drawable {
    return ContextCompat.getDrawable(requireContext(), drawable)!!
}

fun Fragment.getColor(color: Int): Int {
    return ContextCompat.getColor(requireContext(), color)
}