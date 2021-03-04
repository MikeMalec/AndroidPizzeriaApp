package com.example.pizzeriaapp.ui.fragments.pizza

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.product.Pizza
import com.example.pizzeriaapp.databinding.PizzaFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.hide
import com.example.pizzeriaapp.utils.views.show
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PizzaFragment : BaseFragment(R.layout.pizza_fragment) {

    private lateinit var binding: PizzaFragmentBinding

    @Inject
    lateinit var pizzaAdapter: PizzaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PizzaFragmentBinding.bind(view)
        binding.collapsingToolbar.title = "Pizze"
        binding.pizzaToolbar?.setTitleTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.collapsingToolbar.setCollapsedTitleTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        setToolbarState()
        setToolbarListener()
        setRv()
        observePizza()
    }

    private var isShow = true
    private var scrollRange = -1
    private fun setToolbarListener() {
        binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout?.totalScrollRange!!
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsingToolbar.title = "Pizza"
                    isShow = true
                } else if (isShow) {
                    binding.ivToolbar?.show()
                    binding.collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun setRv() {
        pizzaAdapter.itemClick = ::showPizzaOrderDialog
        binding.rvPizza.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pizzaAdapter
        }
    }

    private fun hideLoading() {
        binding.pbLoadingPizza.gone()
    }

    private fun observePizza() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.pizza.observe(viewLifecycleOwner, Observer {
                hideLoading()
                pizzaAdapter.submit(it)
                setRecyclerViewPosition()
            })
        }
    }

    private fun showPizzaOrderDialog(pizza: Pizza) {
        PizzaFragmentDirections.actionPizzaFragmentToPizzaOrderDialog(pizza).also {
            findNavController().navigate(it)
        }
    }

    private fun setRecyclerViewPosition() {
        mainViewModel.pizzaScrollState?.also { state ->
            binding.rvPizza.apply {
                layoutManager?.onRestoreInstanceState(state)
            }
        }
    }

    private fun setToolbarState() {
        if (mainViewModel.pizzaToolbarCollapsed) {
            binding.ivToolbar?.hide()
            binding.appBar.setExpanded(false, false)
        }
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.pizzaScrollState = binding.rvPizza.layoutManager?.onSaveInstanceState()
        mainViewModel.pizzaToolbarCollapsed = isShow
    }
}