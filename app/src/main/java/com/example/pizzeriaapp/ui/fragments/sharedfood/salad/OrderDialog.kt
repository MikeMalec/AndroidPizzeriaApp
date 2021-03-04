package com.example.pizzeriaapp.ui.fragments.sharedfood.salad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.pizzeriaapp.data.cart.CartItemInterface
import com.example.pizzeriaapp.data.cart.beverage.BeverageCartItem
import com.example.pizzeriaapp.data.cart.salad.SaladCartItem
import com.example.pizzeriaapp.data.product.Beverage
import com.example.pizzeriaapp.data.product.Salad
import com.example.pizzeriaapp.databinding.SaladOrderDialogBinding
import com.example.pizzeriaapp.ui.MainActivity
import com.example.pizzeriaapp.ui.common.CartViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OrderDialog : BottomSheetDialogFragment() {

    private val args: OrderDialogArgs by navArgs()
    private val salad: Salad?
        get() = args.salad

    private val beverage: Beverage?
        get() = args.beverage

    private lateinit var binding: SaladOrderDialogBinding

    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SaladOrderDialogBinding.inflate(inflater, container, false)
        cartViewModel = (requireActivity() as MainActivity).cartViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        binding.btnCancelSalad.setOnClickListener { dismiss() }
        binding.btnAddSaladToCart.setOnClickListener { addItem() }
    }

    private fun setTitle() {
        if (salad != null) {
            binding.tvSaladName.text = "Sałatka ${salad!!.name}"
        } else {
            binding.tvSaladName.text = beverage!!.name
        }
    }

    private fun addItem() {
        if (salad != null) {
            cartViewModel.addItem(SaladCartItem(salad!!))
            dismiss()
            (requireActivity() as MainActivity).shortSnackbar("Dodano sałatke ${salad!!.name} do koszyka")
        } else {
            cartViewModel.addItem(BeverageCartItem(beverage!!))
            dismiss()
            (requireActivity() as MainActivity).shortSnackbar("Dodano ${beverage!!.name} do koszyka")
        }
    }
}