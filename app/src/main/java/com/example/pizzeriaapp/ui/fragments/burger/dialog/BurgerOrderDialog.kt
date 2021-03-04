package com.example.pizzeriaapp.ui.fragments.burger.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.pizzeriaapp.data.cart.burger.BurgerCartItem
import com.example.pizzeriaapp.data.product.Burger
import com.example.pizzeriaapp.data.cart.burger.BurgerType
import com.example.pizzeriaapp.databinding.BurgerOrderDialogBinding
import com.example.pizzeriaapp.ui.MainActivity
import com.example.pizzeriaapp.ui.common.CartViewModel
import com.example.pizzeriaapp.utils.views.showSnackbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class BurgerOrderDialog : BottomSheetDialogFragment() {
    private lateinit var binding: BurgerOrderDialogBinding

    private val args: BurgerOrderDialogArgs by navArgs()

    private val burger: Burger
        get() = args.burger

    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BurgerOrderDialogBinding.inflate(inflater, container, false)
        cartViewModel = (requireActivity() as MainActivity).cartViewModel
        binding.tvBurgerName.text = burger.name
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnCancelBurger.setOnClickListener { dismiss() }
            btnAddBurgerToCart.setOnClickListener { createBurgerCartItem() }
        }
    }

    private fun createBurgerCartItem() {
        val selected = binding.cgBurgerType.checkedChipId
        val chip = view?.findViewById<Chip?>(selected)
        if (chip != null) {
            val burger = when (chip.text) {
                "Solo" -> BurgerCartItem(burger, BurgerType.Solo())
                "Zestaw" -> BurgerCartItem(burger, BurgerType.Set())
                else -> null
            }
            burger?.also { cartViewModel.addItem(it) }
            dismiss()
            (requireActivity() as MainActivity).shortSnackbar("Dodano ${this.burger.name} do koszyka")
        } else {
            showSizeMessage()
        }
    }

    private fun showSizeMessage() {
        showSnackbar(binding.root, "Wybierz rodzaj!")
    }
}