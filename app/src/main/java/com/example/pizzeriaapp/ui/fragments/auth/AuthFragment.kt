package com.example.pizzeriaapp.ui.fragments.auth

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.databinding.AuthFragmentBinding
import com.example.pizzeriaapp.ui.fragments.sharedfood.NamedFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.auth_fragment) {

    private lateinit var binding: AuthFragmentBinding

    private val args: AuthFragmentArgs by navArgs()
    private val navigateToInfoFragment: Boolean
        get() = args.navigatesFromInfoFragment

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AuthFragmentBinding.bind(view)
        setToolbar()
        setVp()
    }

    private fun setToolbar() {
        binding.tbAuth.apply {
            navigationIcon =
                ContextCompat.getDrawable(requireContext(), R.drawable.back_icon)
            navigationIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            setNavigationOnClickListener { navigateBack() }
        }
    }

    private fun setVp() {
        viewPagerAdapter = ViewPagerAdapter(
            listOf(
                LoginFragment(
                    ::navigateBack
                ),
                RegisterFragment(
                    ::navigateBack
                )
            ),
            childFragmentManager,
            lifecycle
        )
        binding.vpAuth.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tlAuth, binding.vpAuth) { tab, position ->
            val frag = viewPagerAdapter.fragments[position] as NamedFragment
            tab.text = frag.getName()
        }.attach()
        binding.tlAuth.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.also {
                    viewPagerAdapter.createFragment(tab.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun navigateBack() {
        when (navigateToInfoFragment) {
            true -> navigateToOrderInfoFragment()
            false -> navigateToCartFragment()
        }
    }

    private fun navigateToCartFragment() {
        findNavController().popBackStack()
    }

    private fun navigateToOrderInfoFragment() {
        AuthFragmentDirections.actionAuthFragmentToOrderInfoFragment().also {
            findNavController().navigate(it)
        }
    }
}