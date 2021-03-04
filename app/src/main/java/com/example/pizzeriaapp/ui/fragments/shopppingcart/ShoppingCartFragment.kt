package com.example.pizzeriaapp.ui.fragments.shopppingcart

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.cart.CartItemInterface
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.databinding.ShoppingCartFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.fragments.auth.AuthViewModel
import com.example.pizzeriaapp.utils.fragments.getColor
import com.example.pizzeriaapp.utils.fragments.getDrawable
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.hide
import com.example.pizzeriaapp.utils.views.show
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingCartFragment : BaseFragment(R.layout.shopping_cart_fragment) {
    private lateinit var binding: ShoppingCartFragmentBinding

    @Inject
    lateinit var shoppingCartAdapter: ShoppingCartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ShoppingCartFragmentBinding.bind(view)
        authViewModel.setLogoutChannel()
        observeToken()
        observeLogoutRequestState()
        setOrderButtonAccordingToTime()
        setRv()
        observeItems()
        observePrice()
        binding.btnMoveToOrderInfo.setOnClickListener { navigateToOrderInfoFragment() }
    }

    private fun observeToken() {
        lifecycleScope.launchWhenStarted {
            authViewModel.token.collect {
                setToolbar(it)
            }
        }
    }

    private fun observeLogoutRequestState() {
        lifecycleScope.launchWhenStarted {
            authViewModel.logoutRequestState.consumeAsFlow().collect {
                when (it) {
                    is Resource.Success -> {
                        mainActivity.shortSnackbar("Wylogowano")
                    }
                    is Resource.Error -> {
                        if (it.error !== null) {
                            if (it.error.contains("Not authorized")) {
                                mainActivity.shortSnackbar("Wylogowano")
                            } else {
                                mainActivity.shortSnackbar("Coś poszło nie tak!")
                            }
                        } else {
                            mainActivity.shortSnackbar("Coś poszło nie tak!")
                        }
                    }
                }
            }
        }
    }

    private fun setToolbar(token: String?) {
        binding.shoppingCartToolbar.apply {
            inflateMenu(R.menu.cart_fragment_menu)
            overflowIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            when (token) {
                null -> {
                    menu.findItem(R.id.log_in).isVisible = true
                    menu.findItem(R.id.user_orders).isVisible = false
                    menu.findItem(R.id.log_out).isVisible = false
                }
                else -> {
                    menu.findItem(R.id.user_orders).isVisible = true
                    menu.findItem(R.id.log_out).isVisible = true
                    menu.findItem(R.id.log_in).isVisible = false
                }
            }
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.user_orders -> navigateToUserOrdersFragment()
                    R.id.log_out -> logout()
                    R.id.log_in -> navigateToAuthFragment()
                }
                false
            }
        }
    }

    private var canMakeOrder = true
    private fun setOrderButtonAccordingToTime() {
        val time = Calendar.getInstance()
        val hours = time.get(Calendar.HOUR_OF_DAY)
        val minutes = time.get(Calendar.MINUTE)
        if (hours >= 21 && minutes >= 30) {
            disableOrder()
        } else if (hours >= 22) {
            disableOrder()
        }
    }

    private fun disableOrder() {
        canMakeOrder = false
        binding.btnMoveToOrderInfo.text = "Zamówienia przyjmowane są do 21:30"
        binding.btnMoveToOrderInfo.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.grey_lighten_2
            )
        )
        binding.btnMoveToOrderInfo.isClickable = false
    }

    private fun setRv() {
        shoppingCartAdapter.deleteCallback = ::removeItem
        shoppingCartAdapter.increaseAmount = ::increaseAmount
        shoppingCartAdapter.decreaseAmount = ::decreaseAmount
        binding.rvCartItems.apply {
            val ll = LinearLayoutManager(requireContext())
            layoutManager = ll
            adapter = shoppingCartAdapter
            val itemDecorator = object : DividerItemDecoration(requireContext(), ll.orientation) {
                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    if (shoppingCartAdapter.itemCount <= 2) {
                        super.onDraw(c, parent, state)
                    } else {
                        val d = drawable
                        parent.children
                            .forEach { view ->
                                val position = parent.getChildAdapterPosition(view)
                                val amountOfItems = shoppingCartAdapter.itemCount - 1
                                if (position != amountOfItems) {
                                    val params = view.layoutParams as RecyclerView.LayoutParams
                                    val top = view.bottom + params.bottomMargin
                                    val bottom = top + d!!.intrinsicHeight
                                    d.setBounds(0, top, parent.height, bottom)
                                    d.draw(c)
                                }
                            }
                    }
                }
            }
            itemDecorator.setDrawable(getDrawable(R.drawable.divider))
            addItemDecoration(itemDecorator)
        }
    }

    private fun observeItems() {
        lifecycleScope.launchWhenStarted {
            cartViewModel.cartItems.observe(viewLifecycleOwner,
                Observer {
                    shoppingCartAdapter.submit(it)
                })
        }
    }

    private fun observePrice() {
        lifecycleScope.launchWhenStarted {
            cartViewModel.price.observe(viewLifecycleOwner, Observer {
                if (it == 0f) {
                    binding.tvOrderPrice.hide()
                    showEmptyBasket()
                } else {
                    hideEmptyBasket()
                    binding.tvOrderPrice.show()
                    binding.tvOrderPrice.text = "$it zł"
                }
            })
        }
    }

    private fun showEmptyBasket() {
        binding.apply {
            tvEmptyBasket.show()
        }
    }


    private fun hideEmptyBasket() {
        binding.apply {
            tvEmptyBasket.gone()
        }
    }

    private fun removeItem(item: CartItemInterface) {
        cartViewModel.removeItem(item)
    }

    private fun increaseAmount(item: CartItemInterface) {
        cartViewModel.increaseAmount(item)
    }

    private fun decreaseAmount(item: CartItemInterface) {
        cartViewModel.decreaseAmount(item)
    }

    private fun navigateToOrderInfoFragment() {
        setOrderButtonAccordingToTime()
        if (canMakeOrder) {
            if (cartViewModel.cartItems.value != null && cartViewModel.cartItems.value!!.isNotEmpty()) {
                if (cartViewModel.price.value!! >= 25) {
                    ShoppingCartFragmentDirections.actionShoppingCartFragmentToOrderInfoFragment()
                        .also {
                            findNavController().navigate(it)
                        }
                } else {
                    mainActivity.shortSnackbar("Minimalna kwota zamowienia to 25zł")
                }
            } else {
                mainActivity.shortSnackbar("Koszyk jest pusty")
            }
        }
    }

    private fun navigateToAuthFragment() {
        ShoppingCartFragmentDirections.actionShoppingCartFragmentToAuthFragment().also {
            findNavController().navigate(it)
        }
    }

    private fun navigateToUserOrdersFragment() {
        lifecycleScope.launch {
            delay(200)
            ShoppingCartFragmentDirections.actionShoppingCartFragmentToUserOrdersFragment().also {
                findNavController().navigate(it)
            }
        }
    }

    private fun logout() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Chcesz się wylogować?")
            .setIcon(R.drawable.ic_baseline_logout_24)
            .setPositiveButton("Tak") { _, _ ->
                authViewModel.logout()
            }
            .setNegativeButton("Nie") { _, _ -> }
            .show()
    }
}