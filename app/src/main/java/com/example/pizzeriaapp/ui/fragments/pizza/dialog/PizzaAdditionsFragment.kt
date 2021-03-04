package com.example.pizzeriaapp.ui.fragments.pizza.dialog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.databinding.PizzaAdditionsFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.utils.views.gone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class PizzaAdditionsFragment(
    private val backCallback: () -> Unit,
    private val forwardCallback: () -> Unit,
    private val itemCartViewModel: ItemCartViewModel
) : BaseFragment(R.layout.pizza_additions_fragment) {
    private lateinit var binding: PizzaAdditionsFragmentBinding

    @Inject
    lateinit var pizzaAddonAdapter: PizzaAddonAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PizzaAdditionsFragmentBinding.bind(view)
        binding.btnBack.setOnClickListener { backCallback() }
        binding.btnForward.setOnClickListener { forwardCallback() }
        setRv()
        observeAddons()
    }

    private fun setRv() {
        pizzaAddonAdapter.itemCartViewModel = itemCartViewModel
        binding.rvAddons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pizzaAddonAdapter
        }
    }

    private fun hideLoading() {
        binding.pbLoadingAddons.gone()
    }

    private fun observeAddons() {
        lifecycleScope.launchWhenStarted {
            delay(150)
            mainViewModel.pizzaAddons.observe(viewLifecycleOwner, Observer {
                hideLoading()
                pizzaAddonAdapter.submit(it)
            })
        }
    }
}