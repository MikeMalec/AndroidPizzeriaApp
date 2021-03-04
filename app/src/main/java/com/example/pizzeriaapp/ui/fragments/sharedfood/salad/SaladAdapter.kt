package com.example.pizzeriaapp.ui.fragments.sharedfood.salad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeriaapp.data.product.Salad
import com.example.pizzeriaapp.databinding.SaladItemBinding
import javax.inject.Inject

class SaladAdapter @Inject constructor() : RecyclerView.Adapter<SaladAdapter.SaladViewHolder>() {
    inner class SaladViewHolder(val binding: SaladItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    lateinit var itemCallback: (salad: Salad) -> Unit

    private val diffCallback = object : DiffUtil.ItemCallback<Salad>() {
        override fun areItemsTheSame(oldItem: Salad, newItem: Salad): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Salad, newItem: Salad): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submit(salad: List<Salad>) = differ.submitList(salad)
    fun getSalad() = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaladViewHolder {
        return SaladViewHolder(
            SaladItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SaladViewHolder, position: Int) {
        val salad = getSalad()[position]
        holder.binding.apply {
            clSaladItem.setOnClickListener { itemCallback(salad) }
            tvSaladName.text = salad.name
            tvSaladIngredients.text = salad.ingredients.joinToString(", ")
            tvSaladPrice.text = "${salad.price} zł"
        }
    }

    override fun getItemCount(): Int {
        return getSalad().size
    }
}