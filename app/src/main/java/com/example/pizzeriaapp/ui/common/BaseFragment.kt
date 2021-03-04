package com.example.pizzeriaapp.ui.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.pizzeriaapp.ui.MainActivity
import com.example.pizzeriaapp.ui.fragments.auth.AuthViewModel

abstract class BaseFragment(layout: Int) : Fragment(layout) {
    protected lateinit var mainViewModel: MainViewModel
    protected lateinit var cartViewModel: CartViewModel
    protected lateinit var authViewModel: AuthViewModel
    protected lateinit var mainActivity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        cartViewModel = mainActivity.cartViewModel
        authViewModel = mainActivity.authViewModel
        mainViewModel = mainActivity.mainViewModel
    }
}