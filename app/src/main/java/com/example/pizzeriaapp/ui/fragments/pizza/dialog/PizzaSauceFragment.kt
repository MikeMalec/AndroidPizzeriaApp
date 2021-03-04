package com.example.pizzeriaapp.ui.fragments.pizza.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.cart.pizza.SauceSize
import com.example.pizzeriaapp.data.product.Sauce
import com.example.pizzeriaapp.databinding.PizzaSauceFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.google.android.material.chip.Chip

class PizzaSauceFragment(
    private val backCallback: () -> Unit,
    private val itemCartViewModel: ItemCartViewModel
) :
    BaseFragment(R.layout.pizza_sauce_fragment) {
    private lateinit var binding: PizzaSauceFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PizzaSauceFragmentBinding.bind(view)
        observeSauces()
//        binding.btnBack.setOnClickListener { backCallback() }
        binding.btnAddToCart.setOnClickListener {
            itemCartViewModel.createPizzaCartItem()
        }
        binding.cbBreadSticks.setOnCheckedChangeListener { compoundButton, b ->
            itemCartViewModel.breadSticks = b
        }
        binding.cbSmallSauce.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.cbBigSauce.isChecked = false
                itemCartViewModel.sauceSize = SauceSize.Small()
            } else {
                itemCartViewModel.sauceSize = null
            }
        }
        binding.cbBigSauce.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.cbSmallSauce.isChecked = false
                itemCartViewModel.sauceSize = SauceSize.Big()
            } else {
                itemCartViewModel.sauceSize = null
            }
        }
    }


    private fun observeSauces() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.sauces.observe(viewLifecycleOwner, Observer {
                dispatchSauces(it)
            })
        }
    }

    private fun dispatchSauces(sauces: List<Sauce>) {
        sauces.forEach { sauce ->
            val chip = Chip(requireContext(), null, R.attr.CustomChipStyle)
            chip.text = sauce.name
            chip.setOnClickListener {
                itemCartViewModel.addSauce(sauce)
            }
            chip.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.gold))
            binding.cgSauces.addView(chip)
        }
    }
}