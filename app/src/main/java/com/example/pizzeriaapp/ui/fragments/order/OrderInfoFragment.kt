package com.example.pizzeriaapp.ui.fragments.order

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.cart.payment.PaymentMethod
import com.example.pizzeriaapp.databinding.OrderInfoFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OrderInfoFragment : BaseFragment(R.layout.order_info_fragment) {

    private lateinit var binding: OrderInfoFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = OrderInfoFragmentBinding.bind(view)
        binding.btnForwardToOrderSummary.setOnClickListener {
            forwardToOrderSummary()
        }
        binding.toolbarOrder.apply {
            navigationIcon =
                ContextCompat.getDrawable(requireContext(), R.drawable.back_icon)
            navigationIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            setNavigationOnClickListener { mainActivity.onBackPressed() }
        }
        observeUserInfo()
    }

    private fun observeUserInfo() {
        lifecycleScope.launchWhenStarted {
            cartViewModel.userInfo.collect {
                setStreetName(it.streetName)
                setHouseNumber(it.houseNumber)
                setCityName(it.cityName)
                setPhoneNumber(it.phoneNumber)
            }
        }
    }

    private fun setStreetName(name: String) {
        if (cartViewModel.streetNameInput != null) {
            binding.etStreetName.setText(cartViewModel.streetNameInput)
        } else {
            binding.etStreetName.setText(name)
        }
    }

    private fun setHouseNumber(name: String) {
        if (cartViewModel.houseNumberInput != null) {
            binding.etHouseNumber.setText(cartViewModel.houseNumberInput)
        } else {
            binding.etHouseNumber.setText(name)
        }
    }

    private fun setCityName(name: String) {
        if (cartViewModel.cityNameInput != null) {
            binding.etCityName.setText(cartViewModel.cityNameInput)
        } else {
            binding.etCityName.setText(name)
        }
    }

    private fun setPhoneNumber(name: String) {
        if (cartViewModel.phoneNumberInput != null) {
            binding.etPhoneNumber.setText(cartViewModel.phoneNumberInput)
        } else {
            binding.etPhoneNumber.setText(name)
        }
    }

    private val streetName: String
        get() = binding.etStreetName.text.toString()

    private val houseNumber: String
        get() = binding.etHouseNumber.text.toString()

    private val cityName: String
        get() = binding.etCityName.text.toString()

    private val phoneNumber: String
        get() = binding.etPhoneNumber.text.toString()

    override fun onPause() {
        super.onPause()
        saveUserInput()
    }

    private fun saveUserInput() {
        cartViewModel.streetNameInput = streetName
        cartViewModel.houseNumberInput = houseNumber
        cartViewModel.cityNameInput = cityName
        cartViewModel.phoneNumberInput = phoneNumber
    }

    private fun setPaymentMethod() {
        binding.rbPayment.run {
            val checkedItem = findViewById<RadioButton>(this.checkedRadioButtonId)
            when (checkedItem.id) {
                R.id.rbPaymentCart -> cartViewModel.paymentMethod = PaymentMethod.Cart()
                R.id.rbPaymentCash -> cartViewModel.paymentMethod = PaymentMethod.Cash()
            }
        }
    }

    private fun forwardToOrderSummary() {
        if (streetName.isNotEmpty() && houseNumber.isNotEmpty() && cityName.isNotEmpty() && phoneNumber.isNotEmpty()) {
            if (cartViewModel.tokenManager.token == null) {
                mainActivity.shortSnackbar("Musisz być zalogowany aby złożyć zamówienie")
                OrderInfoFragmentDirections.actionOrderInfoFragmentToAuthFragment(true).also {
                    findNavController().navigate(it)
                }
            } else {
                saveUserInput()
                setPaymentMethod()
                cartViewModel.setUserInfo(streetName, houseNumber, cityName, phoneNumber)
                OrderInfoFragmentDirections.actionOrderInfoFragmentToOrderSummaryFragment().also {
                    findNavController().navigate(it)
                }
            }
        } else {
            mainActivity.shortSnackbar("Podaj wszystkie wymagane informacje")
        }
    }
}