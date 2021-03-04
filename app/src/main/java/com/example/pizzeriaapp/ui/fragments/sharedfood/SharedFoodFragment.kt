package com.example.pizzeriaapp.ui.fragments.sharedfood

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.product.Pasta
import com.example.pizzeriaapp.data.product.Pita
import com.example.pizzeriaapp.data.product.Salad
import com.example.pizzeriaapp.databinding.SharedFoodFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.pasta.PastaFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.pita.PitaFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.salad.SaladFragment
import com.example.pizzeriaapp.utils.views.hide
import com.example.pizzeriaapp.utils.views.show
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SharedFoodFragment : BaseFragment(R.layout.shared_food_fragment) {
    private lateinit var binding: SharedFoodFragmentBinding

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SharedFoodFragmentBinding.bind(view)
        setToolbarState()
        setToolbarListener()
        setToolbarTitle("Makarony")
        setVp()
    }

    private var currentTitleName = "Makarony"

    private var isShow = true
    private var scrollRange = -1
    private fun setToolbarListener() {
        binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout?.totalScrollRange!!
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsToolbar.title = currentTitleName
                    isShow = true
                } else if (isShow) {
                    binding.sharedFoodImage?.show()
                    binding.collapsToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun setVp() {
        viewPagerAdapter =
            ViewPagerAdapter(
                listOf(
                    PastaFragment(::showPastaOrderFragment),
                    PitaFragment(::showPitaOrderFragment),
                    SaladFragment(::showSaladOrderFragment)
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
        binding.tvFoodType?.text = title
    }

    private fun setToolbarImage(title: String) {
        when (title) {
            "Makarony" ->
                binding.sharedFoodImage?.load(R.drawable.pasta_toolbar) {
                    crossfade(300)
                }
            "Pity" ->
                binding.sharedFoodImage?.load(R.drawable.pita_toolbar) {
                    crossfade(300)
                }
            "Sałatki" ->
                binding.sharedFoodImage?.load(R.drawable.salad_toolbar) {
                    crossfade(300)
                }
        }
    }

    private fun showPastaOrderFragment(pasta: Pasta) {
        SharedFoodFragmentDirections.actionPastaFragmentToPastaOrderDialog(pasta).also {
            findNavController().navigate(it)
        }
    }

    private fun showPitaOrderFragment(pita: Pita) {
        SharedFoodFragmentDirections.actionPastaFragmentToPitaOrderDialog(pita).also {
            findNavController().navigate(it)
        }
    }

    private fun showSaladOrderFragment(salad: Salad) {
        SharedFoodFragmentDirections.actionPastaFragmentToSaladOrderDialog(salad, null).also {
            findNavController().navigate(it)
        }
    }

    private fun setToolbarState() {
        if (mainViewModel.sharedFoodToolbarCollapsed) {
            binding.sharedFoodImage?.hide()
            binding.appBar.setExpanded(false, false)
        }
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.sharedFoodToolbarCollapsed = isShow
    }
}