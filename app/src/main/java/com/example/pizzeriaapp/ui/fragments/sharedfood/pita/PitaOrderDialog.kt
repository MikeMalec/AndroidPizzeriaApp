package com.example.pizzeriaapp.ui.fragments.sharedfood.pita

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.pizzeriaapp.data.cart.pita.PitaCartItem
import com.example.pizzeriaapp.data.cart.pita.PitaType
import com.example.pizzeriaapp.data.product.Pita
import com.example.pizzeriaapp.databinding.PitaOrderDialogBinding
import com.example.pizzeriaapp.ui.MainActivity
import com.example.pizzeriaapp.ui.common.CartViewModel
import com.example.pizzeriaapp.utils.views.showSnackbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class PitaOrderDialog : BottomSheetDialogFragment() {
    private lateinit var binding: PitaOrderDialogBinding

    private val args: PitaOrderDialogArgs by navArgs()
    val pita: Pita
        get() = args.pita

    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PitaOrderDialogBinding.inflate(inflater, container, false)
        cartViewModel = (requireActivity() as MainActivity).cartViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvPitaName.text = "Pita ${pita.name}"
            btnCancelPita.setOnClickListener { dismiss() }
            btnAddPitaToCart.setOnClickListener { createPastaCartItem() }
        }
    }

    private fun createPastaCartItem() {
        val selected = binding.cgPitaType.checkedChipId
        val chip = view?.findViewById<Chip?>(selected)
        if (chip != null) {
            val pita = when (chip.text) {
                "Mała" -> PitaCartItem(this.pita, PitaType.Small())
                "Duża" -> PitaCartItem(this.pita, PitaType.Big())
                else -> null
            }
            pita?.also { cartViewModel.addItem(it) }
            dismiss()
            (requireActivity() as MainActivity).shortSnackbar("Dodano Pite ${this.pita.name} do koszyka")
        } else {
            showSizeMessage()
        }
    }

    private fun showSizeMessage() {
        showSnackbar(binding.root, "Wybierz rozmiar!")
    }
}