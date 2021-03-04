package com.example.pizzeriaapp.ui.fragments.sharedfood.pita

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.product.Pita
import com.example.pizzeriaapp.databinding.PitaFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.NamedFragment
import com.example.pizzeriaapp.utils.views.hide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PitaFragment(private val itemCallback: (pita: Pita) -> Unit) :
    BaseFragment(R.layout.pita_fragment), NamedFragment {

    private lateinit var binding: PitaFragmentBinding

    @Inject
    lateinit var pitaAdapter: PitaAdapter

    override fun getName(): String {
        return "Pity"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PitaFragmentBinding.bind(view)
        setRv()
        observePita()
    }

    private fun setRv() {
        pitaAdapter.itemCallback = itemCallback
        binding.rvPita.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pitaAdapter
        }
    }

    private fun observePita() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.pita.observe(viewLifecycleOwner, Observer {
                pitaAdapter.submit(it)
                setRecyclerViewPosition()
            })
        }
    }

    private fun setRecyclerViewPosition() {
        mainViewModel.pitaScrollState?.also { state ->
            binding.rvPita.apply {
                layoutManager?.onRestoreInstanceState(state)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.pitaScrollState = binding.rvPita.layoutManager?.onSaveInstanceState()
    }
}