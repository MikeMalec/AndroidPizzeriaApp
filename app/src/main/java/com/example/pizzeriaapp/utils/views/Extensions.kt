package com.example.pizzeriaapp.utils.views

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.pizzeriaapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun BottomSheetDialogFragment.showSnackbar(bindingView: View, message: String) {
    Snackbar.make(bindingView, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.white))
        .setTextColor(ContextCompat.getColor(requireContext(), R.color.red)).apply {
            view.elevation = 1000f
        }
        .show()
}

fun AppCompatActivity.showSnackbar(
    bottomNav: BottomNavigationView,
    bindingView: View,
    message: String
) {
    Snackbar.make(bindingView, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(ContextCompat.getColor(this, R.color.white))
        .setTextColor(ContextCompat.getColor(this, R.color.red)).apply {
            view.elevation = 1000f
            anchorView = bottomNav
        }
        .show()
}