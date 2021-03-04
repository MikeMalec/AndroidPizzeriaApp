package com.example.pizzeriaapp.ui.fragments.beverage

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.product.Beverage
import com.example.pizzeriaapp.databinding.BeverageFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.fragments.beverage.alcohol.AlcoholFragment
import com.example.pizzeriaapp.ui.fragments.beverage.cold.ColdBeverageFragment
import com.example.pizzeriaapp.ui.fragments.beverage.hot.HotBeverageFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.NamedFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.ViewPagerAdapter
import com.example.pizzeriaapp.utils.views.hide
import com.example.pizzeriaapp.utils.views.show
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeverageFragment : BaseFragment(R.layout.beverage_fragment) {
    private lateinit var binding: BeverageFragmentBinding

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BeverageFragmentBinding.bind(view)
        setToolbarState()
        setToolbarTitle("Napoje zimne")
        setToolbarListener()
        setVp()
    }

    private var currentTitleName = "Napoje zimne"

    private var isShow = true
    private var scrollRange = -1
    private fun setToolbarListener() {
        binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout?.totalScrollRange!!
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.beverageCollapsToolbar.title = currentTitleName
                    isShow = true
                } else if (isShow) {
                    binding.sharedBeverageImage?.show()
                    binding.beverageCollapsToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun setVp() {
        viewPagerAdapter = ViewPagerAdapter(
            listOf(
                ColdBeverageFragment(::showOrderDialog),
                HotBeverageFragment(),
                AlcoholFragment(::showOrderDialog)
            ),
            childFragmentManager,
            lifecycle
        )
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val fragment = viewPagerAdapter.fragments[position] as NamedFragment
            tab.text = fragment.getName()
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.also {
                    val title =
                        (viewPagerAdapter.fragments[tab.position] as NamedFragment).getName()
                    setToolbarTitle(title)
                    setToolbarImage(title)
                    viewPagerAdapter.createFragment(tab.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setToolbarTitle(title: String) {
        currentTitleName = title
        binding.tvBeverageType?.text = title
    }

    private fun setToolbarImage(title: String) {
        when (title) {
            "Napoje zimne" ->
                binding.sharedBeverageImage?.load(R.drawable.cold_toolbar) {
                    crossfade(300)
                }
            "Napoje gorące" -> binding.sharedBeverageImage?.load(R.drawable.hot_toolbar) {
                crossfade(300)
            }
            "Alkohole" -> binding.sharedBeverageImage?.load(R.drawable.alcohol_toolbar) {
                crossfade(300)
            }
        }
    }

    private fun setToolbarState() {
        if (mainViewModel.beverageToolbarCollapsed) {
            binding.sharedBeverageImage?.hide()
            binding.appBar.setExpanded(false, false)
        }
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.beverageToolbarCollapsed = isShow
    }

    private fun showOrderDialog(beverage: Beverage) {
        BeverageFragmentDirections.actionBeverageFragmentToSaladOrderDialog(null, beverage).also {
            findNavController().navigate(it)
        }
    }
}