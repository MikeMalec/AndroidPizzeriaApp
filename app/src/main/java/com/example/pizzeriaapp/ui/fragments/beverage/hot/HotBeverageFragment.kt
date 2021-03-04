package com.example.pizzeriaapp.ui.fragments.beverage.hot

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.databinding.HotBeverageFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.fragments.beverage.cold.BeverageAdapter
import com.example.pizzeriaapp.ui.fragments.sharedfood.NamedFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HotBeverageFragment : BaseFragment(R.layout.hot_beverage_fragment), NamedFragment {

    private lateinit var binding: HotBeverageFragmentBinding

    @Inject
    lateinit var beverageAdapter: BeverageAdapter

    override fun getName(): String {
        return "Napoje gorące"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HotBeverageFragmentBinding.bind(view)
        setRv()
        observeHotBeverages()
    }

    private fun setRv() {
        binding.rvHot.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = beverageAdapter
        }
    }

    private fun observeHotBeverages() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.hotBeverage.observe(viewLifecycleOwner, Observer {
                beverageAdapter.submit(it)
                setRecyclerViewPosition()
            })
        }
    }

    private fun setRecyclerViewPosition() {
        mainViewModel.hotBeverageScrollState?.also { state ->
            binding.rvHot.apply {
                layoutManager?.onRestoreInstanceState(state)
            }
        }
    }


    override fun onPause() {
        super.onPause()
        mainViewModel.hotBeverageScrollState = binding.rvHot.layoutManager?.onSaveInstanceState()
    }
}