package com.example.pizzeriaapp.ui.fragments.burger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeriaapp.data.product.Burger
import com.example.pizzeriaapp.databinding.BurgerItemBinding
import javax.inject.Inject

class BurgerAdapter @Inject constructor() : RecyclerView.Adapter<BurgerAdapter.BurgerViewHolder>() {
    inner class BurgerViewHolder(val binding: BurgerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    lateinit var itemCallback: (burger: Burger) -> Unit

    private val diffCallback = object : DiffUtil.ItemCallback<Burger>() {
        override fun areItemsTheSame(oldItem: Burger, newItem: Burger): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Burger, newItem: Burger): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submit(burger: List<Burger>) = differ.submitList(burger)
    fun getBurgers() = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BurgerViewHolder {
        return BurgerViewHolder(
            BurgerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BurgerViewHolder, position: Int) {
        val burger = getBurgers()[position]
        holder.binding.apply {
            clBurgerItem.setOnClickListener { itemCallback(burger) }
            tvBurgerName.text = burger.name
            tvBurgerIngredients.text = burger.ingredients.joinToString(", ")
            tvBurgerSetPrice.text = "${burger.setPrice} zł"
            tvBurgerSoloPrice.text = "${burger.soloPrice} zł"
        }
    }

    override fun getItemCount(): Int = getBurgers().size
}