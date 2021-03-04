package com.example.pizzeriaapp.ui.fragments.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.databinding.DialogCheckoutBinding
import com.example.pizzeriaapp.ui.MainActivity
import com.example.pizzeriaapp.ui.common.CartViewModel
import com.example.pizzeriaapp.ui.fragments.order.OrderInfoDialog
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.show
import com.example.pizzeriaapp.utils.views.showSnackbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.GsonBuilder
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

class CheckoutDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogCheckoutBinding

    protected lateinit var mainActivity: MainActivity
    protected lateinit var cartViewModel: CartViewModel

    private lateinit var stripe: Stripe
    private lateinit var clientSecret: String
    private var orderTime: Int? = null

    private var canClick = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        cartViewModel = mainActivity.cartViewModel
        cartViewModel.setOrderChannel()
        binding.btnCancelPayment.setOnClickListener { dismiss() }
        stripe = Stripe(
            requireContext(),
            ""
        )
        cartViewModel.startCheckout()
        observeOrderRequest()
    }

    private fun observeOrderRequest() {
        lifecycleScope.launchWhenStarted {
            cartViewModel.orderRequest.consumeAsFlow().collect {
                when (it) {
                    is Resource.Loading -> showLoading()
                    is Resource.Error -> {
                        hideLoading()
                        dispatchError(it)
                    }
                    is Resource.Success -> {
                        it.data?.also { resp ->
                            clientSecret = resp.clientSecret
                            orderTime = resp.time
                            setUiWithClientSecret()
                        }
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun setUiWithClientSecret() {
        binding.payButton.setOnClickListener {
            if (canClick) {
                binding.cardInputWidget.paymentMethodCreateParams?.let { params ->
                    showLoading()
                    canClick = false
                    val confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, clientSecret)
                    stripe.confirmPayment(this, confirmParams)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                hideLoading()
                canClick = true
                val paymentIntent = result.intent
                val status = paymentIntent.status
                if (status == StripeIntent.Status.Succeeded) {
//                    val gson = GsonBuilder().setPrettyPrinting().create()
//                    val paymentIntent = gson.toJson(paymentIntent)
//                    Log.d("XXX", "PAYMENT INTENT = $paymentIntent")
                    showOrderTime(orderTime!!)
                } else if (status == StripeIntent.Status.RequiresPaymentMethod) {
                    showSnackbar(binding.root, "Płatność nie udana")
                }
            }

            override fun onError(e: Exception) {
                hideLoading()
                canClick = true
//                Log.d("XXX", "PAYMENT ERROR $e")
                showSnackbar(binding.root, "Płatność nie udana")
            }
        })
    }

    private fun showOrderTime(time: Int) {
        dismiss()
        OrderInfoDialog(time).show(parentFragmentManager, "ORDER_INFO_DIALOG")
    }

    private fun showLoading() {
        binding.pbPayment.show()
    }

    private fun hideLoading() {
        binding.pbPayment.gone()
    }

    private fun dispatchError(error: Resource.Error) {
        if (error.error != null) {
            if (error.error.contains("Not authorized")) {
                showSnackbar(binding.root, "Wymagane ponowne zalogowanie")
            } else {
                if (error.error.contains("późno")) {
                    showSnackbar(binding.root, "Zamówenia przyjmowane są do 21:30")
                } else {
                    showSnackbar(binding.root, "Coś poszło nie tak")
                }
            }
        } else {
            showSnackbar(binding.root, "Coś poszło nie tak")
        }
    }
}