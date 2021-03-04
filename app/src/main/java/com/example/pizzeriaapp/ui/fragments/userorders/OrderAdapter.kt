package com.example.pizzeriaapp.ui.fragments.userorders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeriaapp.data.order.responses.Order
import com.example.pizzeriaapp.databinding.LoadingItemBinding
import com.example.pizzeriaapp.databinding.OrderItemBinding
import com.example.pizzeriaapp.utils.date.DateConverter
import com.example.pizzeriaapp.utils.rv.RecyclerViewItem
import javax.inject.Inject

class OrderAdapter @Inject constructor(val dateConverter: DateConverter) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class OrderViewHolder(val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }


    inner class LoadingViewHolder(val binding: LoadingItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<RecyclerViewItem<Order>>() {
        override fun areItemsTheSame(
            oldItem: RecyclerViewItem<Order>,
            newItem: RecyclerViewItem<Order>
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RecyclerViewItem<Order>,
            newItem: RecyclerViewItem<Order>
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitList(items: List<RecyclerViewItem<Order>>) = differ.submitList(items)
    private fun getItems(): List<RecyclerViewItem<Order>> = differ.currentList

    fun showLoading() {
        submitList(listOf(*getItems().toTypedArray(), RecyclerViewItem.Loading))
    }

    fun hideLoading() {
        val items = getItems().toMutableList()
        items.removeLast()
        submitList(items)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItems()[position]
        return when (item) {
            is RecyclerViewItem.Loading -> 1
            is RecyclerViewItem.Item -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val ll = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> LoadingViewHolder(LoadingItemBinding.inflate(ll, parent, false))
            else -> OrderViewHolder(OrderItemBinding.inflate(ll, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItems()[position]
        val viewType = getItemViewType(position)
        if (viewType == 2) {
            val viewHolder = holder as OrderViewHolder
            val order = (item as RecyclerViewItem.Item).data
            viewHolder.binding.apply {
                tvOrderDate.text = dateConverter.mapDateToYearMonthHours(order.orderInfo.createdAt)
                tvOrderPrice.text = "${(order.orderInfo.price / 100)}zł"
                tvOrderInfo.text = order.getDescription()
                tvStreetName.text = order.orderInfo.street
                tvHouseNumber.text = order.orderInfo.houseNumber
                tvCity.text = order.orderInfo.city
                tvPhoneNumber.text = order.orderInfo.phoneNumber
            }
        }
    }

    override fun getItemCount(): Int {
        return getItems().size
    }
}