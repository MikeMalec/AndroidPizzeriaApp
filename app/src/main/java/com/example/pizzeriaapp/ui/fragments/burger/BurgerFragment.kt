package com.example.pizzeriaapp.ui.fragments.burger

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.product.Burger
import com.example.pizzeriaapp.databinding.BurgerFragmentBinding
import com.example.pizzeriaapp.ui.MainActivity
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.common.CartViewModel
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.hide
import com.example.pizzeriaapp.utils.views.show
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BurgerFragment : BaseFragment(R.layout.burger_fragment) {

    private lateinit var binding: BurgerFragmentBinding

    @Inject
    lateinit var burgerAdapter: BurgerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BurgerFragmentBinding.bind(view)
        setToolbarState()
        setToolbarListener()
        setRv()
        observeBurger()
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
                    binding.collapsingToolbar.title = "Burgery"
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
        burgerAdapter.itemCallback = ::showBurgerOrderDialog
        binding.rvBurger.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = burgerAdapter
        }
    }

    private fun hideLoading() {
        binding.burgerFragmentLoading.gone()
    }

    private fun observeBurger() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.burger.observe(viewLifecycleOwner, Observer {
                hideLoading()
                burgerAdapter.submit(it)
                setRecyclerViewPosition()
            })
        }
    }

    private fun showBurgerOrderDialog(burger: Burger) {
        BurgerFragmentDirections.actionBurgerFragmentToBurgerOrderDialog(burger).also {
            findNavController().navigate(it)
        }
    }

    private fun setRecyclerViewPosition() {
        mainViewModel.burgerScrollState?.also { state ->
            binding.rvBurger.apply {
                layoutManager?.onRestoreInstanceState(state)
            }
        }
    }

    private fun setToolbarState() {
        if (mainViewModel.burgerToolbarCollapsed) {
            binding.ivToolbar?.hide()
            binding.appBar.setExpanded(false, false)
        }
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.burgerScrollState = binding.rvBurger.layoutManager?.onSaveInstanceState()
        mainViewModel.burgerToolbarCollapsed = isShow
    }
}