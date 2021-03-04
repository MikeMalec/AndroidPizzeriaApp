package com.example.pizzeriaapp.ui.fragments.userorders

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.databinding.UserOrdersFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject

@AndroidEntryPoint
class UserOrdersFragment : BaseFragment(R.layout.user_orders_fragment) {

    private lateinit var binding: UserOrdersFragmentBinding

    private val ordersViewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var orderAdapter: OrderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = UserOrdersFragmentBinding.bind(view)
        binding.refreshLayout.setOnRefreshListener {
            showRefreshing()
            ordersViewModel.resetPagination()
        }
        setToolbar()
        setRv()
        ordersViewModel.fetchOrders()
        observeOrders()
        observeFetchRequestState()
    }

    private fun showRefreshing() {
        binding.refreshLayout.isRefreshing = true
    }

    private fun hideRefreshing() {
        binding.refreshLayout.isRefreshing = false
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            navigationIcon =
                ContextCompat.getDrawable(requireContext(), R.drawable.back_icon)
            navigationIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setRv() {
        binding.rvOrders.apply {
            setHasFixedSize(true)
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val ll = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItem = ll.findLastVisibleItemPosition()
                    if (lastVisibleItem == orderAdapter.itemCount - 1 || lastVisibleItem == orderAdapter.itemCount - 2) {
                        ordersViewModel.fetchOrders()
                    }
                }
            })
        }
    }

    private fun observeOrders() {
        lifecycleScope.launchWhenStarted {
            ordersViewModel.orders.observe(viewLifecycleOwner, Observer {
                hideRefreshing()
                orderAdapter.hideLoading()
                orderAdapter.submitList(it)
            })
        }
    }

    private fun observeFetchRequestState() {
        lifecycleScope.launchWhenStarted {
            ordersViewModel.fetchRequestState.consumeAsFlow().collect {
                hideRefreshing()
                when (it) {
                    is Resource.Loading -> orderAdapter.showLoading()
                    is Resource.Error -> {
                        orderAdapter.hideLoading()
                        dispatchRequestError(it.error)
                    }
                }
            }
        }
    }

    private fun dispatchRequestError(error: String?) {
        if (error == null) {
            mainActivity.shortSnackbar("Coś poszło nie tak")
        } else {
            if (error.contains("Not authorized")) {
                mainActivity.shortSnackbar("Wymagane ponowne zalogowanie")
                findNavController().popBackStack()
            }
        }
    }
}