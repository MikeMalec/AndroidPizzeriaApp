package com.example.pizzeriaapp.ui.fragments.beverage.cold

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.product.Beverage
import com.example.pizzeriaapp.databinding.ColdBeverageFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.NamedFragment
import com.example.pizzeriaapp.utils.views.gone
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ColdBeverageFragment(private val itemCallback: (beverage: Beverage) -> Unit) :
    BaseFragment(R.layout.cold_beverage_fragment), NamedFragment {

    private lateinit var binding: ColdBeverageFragmentBinding

    @Inject
    lateinit var beverageAdapter: BeverageAdapter

    override fun getName(): String {
        return "Napoje zimne"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ColdBeverageFragmentBinding.bind(view)
        setRv()
        observeColdBeverages()
    }

    private fun setRv() {
        beverageAdapter.itemClick = itemCallback
        binding.rvCold.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = beverageAdapter
        }
    }

    private fun hideLoading() {
        binding.pbColdLoading.gone()
    }

    private fun observeColdBeverages() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.coldBeverage.observe(viewLifecycleOwner, Observer {
                hideLoading()
                beverageAdapter.submit(it)
                setRecyclerViewPosition()
            })
        }
    }

    private fun setRecyclerViewPosition() {
        mainViewModel.coldBeverageScrollState?.also { state ->
            binding.rvCold.apply {
                layoutManager?.onRestoreInstanceState(state)
            }
        }
    }


    override fun onPause() {
        super.onPause()
        mainViewModel.coldBeverageScrollState = binding.rvCold.layoutManager?.onSaveInstanceState()
    }
}