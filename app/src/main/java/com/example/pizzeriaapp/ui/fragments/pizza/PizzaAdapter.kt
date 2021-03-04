package com.example.pizzeriaapp.ui.fragments.pizza

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeriaapp.data.product.Pizza
import com.example.pizzeriaapp.databinding.PizzaItemBinding
import com.example.pizzeriaapp.utils.converters.PriceConverter
import javax.inject.Inject

class PizzaAdapter @Inject constructor(val priceConverter: PriceConverter) :
    RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder>() {
    inner class PizzaViewHolder(val binding: PizzaItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    lateinit var itemClick: (pizza: Pizza) -> Unit

    private val diffCallback = object : DiffUtil.ItemCallback<Pizza>() {
        override fun areItemsTheSame(oldItem: Pizza, newItem: Pizza): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pizza, newItem: Pizza): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submit(pizza: List<Pizza>) = differ.submitList(pizza)
    private fun getPizza() = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaViewHolder {
        return PizzaViewHolder(
            PizzaItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PizzaViewHolder, position: Int) {
        val pizza = getPizza()[position]
        holder.binding.apply {
            clPizzaItem.setOnClickListener { itemClick(pizza) }
            tv24Price.text = priceConverter.convertPriceForUI(pizza.size24Price)
            tv30Price.text = priceConverter.convertPriceForUI(pizza.size30Price)
            tv40Price.text = priceConverter.convertPriceForUI(pizza.size40Price)
            tv50Price.text = priceConverter.convertPriceForUI(pizza.size50Price)
            tvPizzaName.text = pizza.name
            tvPizzaIngredients.text = pizza.ingredients.joinToString(", ")
        }
    }

    override fun getItemCount(): Int {
        return getPizza().size
    }
}