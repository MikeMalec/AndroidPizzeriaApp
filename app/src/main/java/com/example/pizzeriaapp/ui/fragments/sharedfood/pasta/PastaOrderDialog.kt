package com.example.pizzeriaapp.ui.fragments.sharedfood.pasta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.pizzeriaapp.data.cart.pasta.PastaCartItem
import com.example.pizzeriaapp.data.product.Pasta
import com.example.pizzeriaapp.data.cart.pasta.PastaType
import com.example.pizzeriaapp.databinding.PastaOrderDialogBinding
import com.example.pizzeriaapp.ui.MainActivity
import com.example.pizzeriaapp.ui.common.CartViewModel
import com.example.pizzeriaapp.utils.views.showSnackbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class PastaOrderDialog : BottomSheetDialogFragment() {

    private lateinit var binding: PastaOrderDialogBinding

    private val args: PastaOrderDialogArgs by navArgs()

    private val pasta: Pasta
        get() = args.pasta

    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PastaOrderDialogBinding.inflate(inflater, container, false)
        binding.tvPastaName.text = pasta.name
        cartViewModel = (requireActivity() as MainActivity).cartViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnCancelPasta.setOnClickListener { dismiss() }
            btnAddPastaToCart.setOnClickListener { createPastaCartItem() }
        }
    }

    private fun createPastaCartItem() {
        val selected = binding.cgPastaType.checkedChipId
        val chip = view?.findViewById<Chip?>(selected)
        if (chip != null) {
            val pasta = when (chip.text) {
                "Mały" -> PastaCartItem(pasta, PastaType.Small())
                "Duży" -> PastaCartItem(pasta, PastaType.Big())
                else -> null
            }
            pasta?.also { cartViewModel.addItem(it) }
            dismiss()
            (requireActivity() as MainActivity).shortSnackbar("Dodano Makaron ${this.pasta.name} do koszyka")
        } else {
            showSizeMessage()
        }
    }

    private fun showSizeMessage() {
        showSnackbar(binding.root, "Wybierz rozmiar!")
    }
}