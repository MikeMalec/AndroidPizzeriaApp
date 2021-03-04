package com.example.pizzeriaapp.ui.fragments.userorders

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzeriaapp.data.order.responses.Order
import com.example.pizzeriaapp.data.order.responses.OrdersResponse
import com.example.pizzeriaapp.data.preferences.DataStoreRepository
import com.example.pizzeriaapp.data.usecases.GetOrders
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.ui.common.RequestResponseTokenValidator
import com.example.pizzeriaapp.ui.common.TokenManager
import com.example.pizzeriaapp.utils.rv.RecyclerViewItem
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.ceil

class OrdersViewModel @ViewModelInject constructor(
    val tokenManager: TokenManager,
    val getOrders: GetOrders,
    val requestResponseTokenValidator: RequestResponseTokenValidator
) : ViewModel() {

    val orders = MutableLiveData<List<RecyclerViewItem<Order>>>()

    val fetchRequestState = Channel<Resource<OrdersResponse?>>()

    private val fetchedOrders = mutableListOf<Order>()
    private var page = 0
    private var pages = 1
    private var fetching = false

    fun fetchOrders() {
        viewModelScope.launch(IO) {
            if (!fetching && page < pages) {
                fetching = true
                page++
                var lastFetched: String? = null
                if (fetchedOrders.isNotEmpty()) {
                    lastFetched = fetchedOrders[fetchedOrders.size - 1].orderInfo.createdAt
                }
                getOrders.getOrders(tokenManager.token!!, lastFetched).collect {
                    requestResponseTokenValidator.validateResponse(it)
                    fetchRequestState.send(it)
                    if (it is Resource.Success) {
                        it.data?.also { orders ->
                            dispatchNewData(orders)
                        }
                    }
                    if (it is Resource.Success || it is Resource.Error) {
                        fetching = false
                    }
                }
            }
        }
    }

    private fun dispatchNewData(data: OrdersResponse) {
        pages = ceil(data.results.toFloat() / 6f).toInt()
        fetchedOrders.addAll(data.orders)
        orders.postValue(fetchedOrders.map { RecyclerViewItem.Item(it) })
    }

    fun resetPagination() {
        page = 0
        pages = 1
        fetching = false
        fetchedOrders.clear()
        fetchOrders()
    }
}