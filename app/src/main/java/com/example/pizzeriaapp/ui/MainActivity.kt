package com.example.pizzeriaapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.databinding.ActivityMainBinding
import com.example.pizzeriaapp.ui.common.CartViewModel
import com.example.pizzeriaapp.ui.common.MainViewModel
import com.example.pizzeriaapp.ui.fragments.auth.AuthViewModel
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.show
import com.example.pizzeriaapp.utils.views.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    val mainViewModel: MainViewModel by viewModels()

    val cartViewModel: CartViewModel by viewModels()

    val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_PizzeriaApp)
        mainViewModel.fetchProducts()
        refreshToken()
        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        setNavController()
        setNavigation()
        observeCartItems()
    }

    private fun refreshToken() {
        lifecycleScope.launchWhenStarted {
            delay(1000)
            authViewModel.refreshToken()
        }
    }

    private fun setNavController() {
        navController =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
    }

    private fun observeCartItems() {
        lifecycleScope.launchWhenStarted {
            cartViewModel.cartItemsAmount.observe(this@MainActivity, Observer {
                when (it) {
                    0 -> {
                        val badge =
                            binding.bottomNavigationView.getOrCreateBadge(R.id.shoppingCartFragment)
                        badge.isVisible = false
                    }
                    else -> {
                        val badge =
                            binding.bottomNavigationView.getOrCreateBadge(R.id.shoppingCartFragment)
                        badge.number = it
                        badge.isVisible = true
                    }
                }
            })
        }
    }

    private fun setNavigation() {
        navController.addOnDestinationChangedListener(this)
        binding.bottomNavigationView.itemIconTintList = null
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    fun shortSnackbar(message: String) =
        showSnackbar(binding.bottomNavigationView, binding.root, message)

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.orderInfoFragment, R.id.orderSummaryFragment, R.id.authFragment, R.id.userOrdersFragment -> hideNavigation()
            else -> showNavigation()
        }
    }

    private fun showNavigation() {
        binding.bottomNavigationView.show()
    }

    private fun hideNavigation() {
        binding.bottomNavigationView.gone()
    }
}