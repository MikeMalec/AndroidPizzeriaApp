package com.example.pizzeriaapp.ui.fragments.beverage.cold

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeriaapp.data.product.Beverage
import com.example.pizzeriaapp.databinding.BeverageItemBinding
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.show
import javax.inject.Inject

class BeverageAdapter @Inject constructor() :
    RecyclerView.Adapter<BeverageAdapter.ColdBeverageViewHolder>() {
    inner class ColdBeverageViewHolder(val binding: BeverageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    lateinit var itemClick: (beverage: Beverage) -> Unit

    private val diffCallback = object : DiffUtil.ItemCallback<Beverage>() {
        override fun areItemsTheSame(oldItem: Beverage, newItem: Beverage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Beverage, newItem: Beverage): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submit(beverage: List<Beverage>) = differ.submitList(beverage)
    private fun getBeverage() = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColdBeverageViewHolder {
        return ColdBeverageViewHolder(
            BeverageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ColdBeverageViewHolder, position: Int) {
        val beverage = getBeverage()[position]
        holder.binding.apply {
            when (beverage.orderable) {
                true -> ivAddBeverage.show()
                false -> ivAddBeverage.gone()
            }
            if (beverage.orderable) {
                clBeverage.setOnClickListener { itemClick(beverage) }
            }
            tvColdInfo.text =
                "${beverage.size ?: ""} ${beverage.ingredients?.joinToString(", ") ?: ""}"
            tvColdName.text = beverage.name
            tvColdPrice.text = "${beverage.price} zł"
        }
    }

    override fun getItemCount(): Int {
        return getBeverage().size
    }
}