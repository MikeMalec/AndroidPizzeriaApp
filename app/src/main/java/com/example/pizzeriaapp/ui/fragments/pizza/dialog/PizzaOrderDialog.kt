package com.example.pizzeriaapp.ui.fragments.pizza.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.product.Pizza
import com.example.pizzeriaapp.databinding.PizzaOrderDialogBinding
import com.example.pizzeriaapp.ui.MainActivity
import com.example.pizzeriaapp.ui.common.CartViewModel
import com.example.pizzeriaapp.ui.fragments.sharedfood.ViewPagerAdapter
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.show
import com.example.pizzeriaapp.utils.views.showSnackbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

@AndroidEntryPoint
class PizzaOrderDialog : BottomSheetDialogFragment() {

    private val args: PizzaOrderDialogArgs by navArgs()

    private val itemCartViewModel: ItemCartViewModel by viewModels()

    private lateinit var cartViewModel: CartViewModel

    private val pizza: Pizza
        get() = args.pizza

    private lateinit var binding: PizzaOrderDialogBinding

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PizzaOrderDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel = (requireActivity() as MainActivity).cartViewModel
        itemCartViewModel.pizza = pizza
        setPizzaName()
        setVp()
        observeItemCart()
        observeItemCartValidation()
        binding.ivBackFromSauceFragment.setOnClickListener { showAdditions() }
    }

    private fun setPizzaName() {
        binding.tvPizzaName.text = pizza.name
    }

    private fun setVp() {
        viewPagerAdapter = ViewPagerAdapter(
            listOf(
                PizzaSizeFragment(::showAdditions, itemCartViewModel),
                PizzaAdditionsFragment(::showSize, ::showSauce, itemCartViewModel),
                PizzaSauceFragment(::showAdditions, itemCartViewModel)
            ), childFragmentManager,
            lifecycle
        )
        binding.viewPager.apply {
            adapter = viewPagerAdapter
        }
    }

    private fun observeItemCart() {
        lifecycleScope.launchWhenStarted {
            itemCartViewModel.pizzaCartItem.consumeAsFlow().collect {
                cartViewModel.addItem(it)
                dismiss()
                (requireActivity() as MainActivity).shortSnackbar(
                    "Dodano ${pizza.name} do koszyka"
                )
            }
        }
    }

    private fun observeItemCartValidation() {
        lifecycleScope.launchWhenStarted {
            itemCartViewModel.createCartItemValidation.consumeAsFlow().collect {
                showSnackbar(binding.root, it)
            }
        }
    }

    private fun showSize() {
        hideBackButton()
        binding.viewPager.currentItem = 0
    }

    private fun showAdditions() {
        hideBackButton()
        binding.viewPager.currentItem = 1
    }

    private fun showSauce() {
        showBackButton()
        binding.viewPager.currentItem = 2
    }

    override fun dismiss() {
        super.dismiss()
        itemCartViewModel.clearPizzaCartInfo()
    }

    private fun showBackButton() {
        binding.ivBackFromSauceFragment.show()
    }

    private fun hideBackButton() {
        binding.ivBackFromSauceFragment.gone()
    }
}