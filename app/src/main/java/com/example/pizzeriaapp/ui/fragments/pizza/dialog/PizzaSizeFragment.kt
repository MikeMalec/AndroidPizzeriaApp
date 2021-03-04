package com.example.pizzeriaapp.ui.fragments.pizza.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.databinding.PizzaSizeFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.google.android.material.chip.Chip

class PizzaSizeFragment(
    private val forwardCallback: () -> Unit,
    private val itemCartViewModel: ItemCartViewModel
) :
    BaseFragment(R.layout.pizza_size_fragment) {
    private lateinit var binding: PizzaSizeFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PizzaSizeFragmentBinding.bind(view)
        binding.btnForward.setOnClickListener { forwardCallback() }
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val chip = getView()?.findViewById<Chip>(checkedId)
                chip?.also {
                    val size = it.text.toString().toInt()
                    itemCartViewModel.size = size
                }
            } else {
                itemCartViewModel.size = null
            }
        }
    }
}