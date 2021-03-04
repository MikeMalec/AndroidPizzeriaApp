package com.example.pizzeriaapp.ui.fragments.sharedfood.pasta

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeriaapp.data.product.Pasta
import com.example.pizzeriaapp.databinding.PastaItemBinding
import javax.inject.Inject

class PastaAdapter @Inject constructor() : RecyclerView.Adapter<PastaAdapter.PastaViewHolder>() {
    inner class PastaViewHolder(val binding: PastaItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    lateinit var itemCallback: (pasta: Pasta) -> Unit

    private val diffCallback = object : DiffUtil.ItemCallback<Pasta>() {
        override fun areItemsTheSame(oldItem: Pasta, newItem: Pasta): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pasta, newItem: Pasta): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submit(pasta: List<Pasta>) = differ.submitList(pasta)
    fun getPasta() = differ.currentList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastaViewHolder {
        return PastaViewHolder(
            PastaItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PastaViewHolder, position: Int) {
        val pasta = getPasta()[position]
        holder.binding.apply {
            clPastItem.setOnClickListener { itemCallback(pasta) }
            tvPastaName.text = pasta.name
            tvPastaIngredients.text = pasta.ingredients.joinToString(", ")
            tvPastaSmallPrice.text = "${pasta.smallPrice} zł"
            tvPastaBigPrice.text = "${pasta.bigPrice} zł"
        }
    }

    override fun getItemCount(): Int {
        return getPasta().size
    }
}