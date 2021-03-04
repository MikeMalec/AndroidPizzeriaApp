package com.example.pizzeriaapp.ui.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.pizzeriaapp.databinding.OrderInfoDialogBinding
import com.example.pizzeriaapp.ui.MainActivity
import com.example.pizzeriaapp.ui.common.CartViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderInfoDialog(private val time: Int) : BottomSheetDialogFragment() {

    private lateinit var binding: OrderInfoDialogBinding

    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OrderInfoDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel = (requireActivity() as MainActivity).cartViewModel
        binding.tvWaitTimeInfo.text = "Przewidywany czas oczekiwania : $time minut"
        binding.btnBackToMenu.setOnClickListener {
            dismiss()
        }
    }

    override fun dismiss() {
        super.dismiss()
        cartViewModel.orderCompleted.offer(true)
    }
}