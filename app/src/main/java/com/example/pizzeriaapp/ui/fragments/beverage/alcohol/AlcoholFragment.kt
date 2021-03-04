package com.example.pizzeriaapp.ui.fragments.beverage.alcohol

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.product.Beverage
import com.example.pizzeriaapp.databinding.AlcoholFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.NamedFragment
import com.example.pizzeriaapp.utils.rv.StickyHeaderItemDecoration
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlcoholFragment(private val itemCallback: (beverage: Beverage) -> Unit) :
    BaseFragment(R.layout.alcohol_fragment), NamedFragment {

    private lateinit var binding: AlcoholFragmentBinding

    @Inject
    lateinit var alcoholAdapter: AlcoholAdapter

    override fun getName(): String {
        return "Alkohole"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AlcoholFragmentBinding.bind(view)
        setRv()
        observeAlcohols()
    }

    private fun setRv() {
        alcoholAdapter.itemCallback = itemCallback
        binding.rvAlcohols.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = alcoholAdapter
            addItemDecoration(
                StickyHeaderItemDecoration(this, true) { position ->
                    alcoholAdapter.getItemViewType(
                        position
                    ) == 1
                }
            )
        }
    }

    private fun observeAlcohols() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.alcohol.observe(viewLifecycleOwner, Observer {
                alcoholAdapter.submit(it)
                setRecyclerViewPosition()
            })
        }
    }


    private fun setRecyclerViewPosition() {
        mainViewModel.alcoholScrollState?.also { state ->
            binding.rvAlcohols.apply {
                layoutManager?.onRestoreInstanceState(state)
            }
        }
    }


    override fun onPause() {
        super.onPause()
        mainViewModel.alcoholScrollState = binding.rvAlcohols.layoutManager?.onSaveInstanceState()
    }
}