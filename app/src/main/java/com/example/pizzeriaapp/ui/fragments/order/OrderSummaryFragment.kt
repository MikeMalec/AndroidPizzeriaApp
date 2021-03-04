package com.example.pizzeriaapp.ui.fragments.order

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.databinding.OrderSummaryFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.fragments.payment.CheckoutDialog
import com.example.pizzeriaapp.ui.fragments.shopppingcart.ShoppingCartAdapter
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject

@AndroidEntryPoint
class OrderSummaryFragment : BaseFragment(R.layout.order_summary_fragment) {

    private lateinit var binding: OrderSummaryFragmentBinding

    @Inject
    lateinit var shoppingCartAdapter: ShoppingCartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = OrderSummaryFragmentBinding.bind(view)
        cartViewModel.setOrderCompletionChannel()
        binding.toolbarSummary.apply {
            navigationIcon =
                ContextCompat.getDrawable(requireContext(), R.drawable.back_icon)
            navigationIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            setNavigationOnClickListener { mainActivity.onBackPressed() }
        }
        setRv()
        setViews()
        observeCartItems()
        binding.btnMakeOrder.setOnClickListener {
            CheckoutDialog().show(parentFragmentManager, "checkout_dialog")
        }
        observeOrderCompleted()
    }

    private fun setRv() {
        shoppingCartAdapter.showItemMenu = false
        shoppingCartAdapter.showAmountButtons = false
        shoppingCartAdapter.deleteCallback = {}
        binding.rvSummaryProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shoppingCartAdapter
        }
    }

    private fun setViews() {
        binding.apply {
            tvSummaryStreet.text = cartViewModel.streetNameInput
            tvSummaryHouseNumber.text = cartViewModel.houseNumberInput
            tvSummaryCity.text = cartViewModel.cityNameInput
            tvSummaryPhoneNumber.text = cartViewModel.phoneNumberInput
            tvSummaryPaymentMethod.text = cartViewModel.paymentMethod.toString()
            tvSummaryPrice.text = "${cartViewModel.price.value.toString()} zł"
        }
    }

    private fun observeCartItems() {
        lifecycleScope.launchWhenStarted {
            cartViewModel.cartItems.observe(viewLifecycleOwner, Observer {
                shoppingCartAdapter.submit(it)
            })
        }
    }

    private fun observeOrderCompleted() {
        lifecycleScope.launchWhenStarted {
            cartViewModel.orderCompleted.consumeAsFlow().collect {
                if (it) {
                    cartViewModel.clearCart()
                    navigateToMenu()
                }
            }
        }
    }

    private fun navigateToMenu() {
        OrderSummaryFragmentDirections.actionOrderSummaryFragmentToPizzaFragment().also {
            findNavController().navigate(it)
        }
    }
}