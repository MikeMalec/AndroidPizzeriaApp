package com.example.pizzeriaapp.ui.fragments.sharedfood.pita

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeriaapp.data.product.Pita
import com.example.pizzeriaapp.databinding.PitaItemBinding
import javax.inject.Inject

class PitaAdapter @Inject constructor() : RecyclerView.Adapter<PitaAdapter.PitaViewHolder>() {
    inner class PitaViewHolder(val binding: PitaItemBinding) : RecyclerView.ViewHolder(binding.root)

    lateinit var itemCallback: (pita: Pita) -> Unit

    private val diffCallback = object : DiffUtil.ItemCallback<Pita>() {
        override fun areItemsTheSame(oldItem: Pita, newItem: Pita): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pita, newItem: Pita): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submit(pita: List<Pita>) = differ.submitList(pita)
    fun getPita() = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PitaViewHolder {
        return PitaViewHolder(
            PitaItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PitaViewHolder, position: Int) {
        val pita = getPita()[position]
        holder.binding.apply {
            clPitaItem.setOnClickListener { itemCallback(pita) }
            tvPitaName.text = pita.name
            tvPitaIngredients.text = pita.ingredients.joinToString(", ")
            tvPitaSmallPrice.text = "${pita.smallPrice} zł"
            tvPitaBigPrice.text = "${pita.bigPrice} zł"
        }
    }

    override fun getItemCount(): Int {
        return getPita().size
    }
}