package com.example.pizzeriaapp.ui.fragments.sharedfood.salad

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.product.Salad
import com.example.pizzeriaapp.databinding.SaladFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.NamedFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SaladFragment(private val itemCallback: (salad: Salad) -> Unit) :
    BaseFragment(R.layout.salad_fragment), NamedFragment {

    private lateinit var binding: SaladFragmentBinding

    @Inject
    lateinit var saladAdapter: SaladAdapter

    override fun getName(): String {
        return "Sałatki"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SaladFragmentBinding.bind(view)
        setRv()
        observeSalad()
    }

    private fun setRv() {
        saladAdapter.itemCallback = itemCallback
        binding.rvSalad.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = saladAdapter
        }
    }

    private fun observeSalad() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.salad.observe(viewLifecycleOwner, Observer {
                saladAdapter.submit(it)
                setRecyclerViewPosition()
            })
        }
    }

    private fun setRecyclerViewPosition() {
        mainViewModel.saladScrollState?.also { state ->
            binding.rvSalad.apply {
                layoutManager?.onRestoreInstanceState(state)
            }
        }
    }


    override fun onPause() {
        super.onPause()
        mainViewModel.saladScrollState = binding.rvSalad.layoutManager?.onSaveInstanceState()
    }
}