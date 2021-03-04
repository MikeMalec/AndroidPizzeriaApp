package com.example.pizzeriaapp.ui.fragments.sharedfood.pasta

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.product.Pasta
import com.example.pizzeriaapp.databinding.PastaFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.NamedFragment
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.hide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PastaFragment(private val itemCallback: (pasta: Pasta) -> Unit) :
    BaseFragment(R.layout.pasta_fragment), NamedFragment {

    private lateinit var binding: PastaFragmentBinding

    @Inject
    lateinit var pastaAdapter: PastaAdapter

    override fun getName(): String {
        return "Makarony"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PastaFragmentBinding.bind(view)
        setRv()
        observePasta()
    }

    private fun setRv() {
        pastaAdapter.itemCallback = itemCallback
        binding.rvPasta.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pastaAdapter
        }
    }

    private fun hideLoading() {
        binding.pastaLoading.gone()
    }

    private fun observePasta() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.pasta.observe(viewLifecycleOwner, Observer {
                hideLoading()
                pastaAdapter.submit(it)
                setRecyclerViewPosition()
            })
        }
    }

    private fun setRecyclerViewPosition() {
        mainViewModel.pastaScrollState?.also { state ->
            binding.rvPasta.apply {
                layoutManager?.onRestoreInstanceState(state)
            }
        }
    }


    override fun onPause() {
        super.onPause()
        mainViewModel.pastaScrollState = binding.rvPasta.layoutManager?.onSaveInstanceState()
    }
}