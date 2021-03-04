package com.example.pizzeriaapp.ui.fragments.pizza.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeriaapp.data.product.Pizza
import com.example.pizzeriaapp.data.product.PizzaAddon
import com.example.pizzeriaapp.databinding.PizzaAddonItemBinding
import com.example.pizzeriaapp.utils.converters.PriceConverter
import javax.inject.Inject

class PizzaAddonAdapter @Inject constructor(val priceConverter: PriceConverter) :
    RecyclerView.Adapter<PizzaAddonAdapter.PizzaAddonViewHolder>() {
    inner class PizzaAddonViewHolder(val binding: PizzaAddonItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    lateinit var itemCartViewModel: ItemCartViewModel

    private val diffCallback = object : DiffUtil.ItemCallback<PizzaAddon>() {
        override fun areItemsTheSame(oldItem: PizzaAddon, newItem: PizzaAddon): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PizzaAddon, newItem: PizzaAddon): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submit(items: List<PizzaAddon>) = differ.submitList(items)
    fun getItems() = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaAddonViewHolder {
        return PizzaAddonViewHolder(
            PizzaAddonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PizzaAddonViewHolder, position: Int) {
        val item = getItems()[position]
        holder.binding.apply {
            cbAddon.text = item.name
            cbAddon.setOnClickListener { itemCartViewModel.pizzaAddonAction(item) }
            tvAddon24Price.text = priceConverter.convertPriceForUI(item.price24)
            tvAddon30Price.text = priceConverter.convertPriceForUI(item.price30)
            tvAddon40Price.text = priceConverter.convertPriceForUI(item.price40)
            tvAddon50Price.text = priceConverter.convertPriceForUI(item.price50)
        }
    }

    override fun getItemCount(): Int {
        return getItems().size
    }
}