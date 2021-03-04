package com.example.pizzeriaapp.ui.fragments.beverage.alcohol

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeriaapp.data.product.AlcoholViewItem
import com.example.pizzeriaapp.data.product.Beverage
import com.example.pizzeriaapp.databinding.AlcoholGroupHeaderBinding
import com.example.pizzeriaapp.databinding.AlcoholItemBinding
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.hide
import com.example.pizzeriaapp.utils.views.show
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlcoholAdapter @Inject constructor(@ApplicationContext val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class AlcoholItemViewHolder(val binding: AlcoholItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class AlcoholHeaderViewHolder(val binding: AlcoholGroupHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    lateinit var itemCallback: (beverage: Beverage) -> Unit

    private val diffCallback = object : DiffUtil.ItemCallback<AlcoholViewItem>() {
        override fun areItemsTheSame(oldItem: AlcoholViewItem, newItem: AlcoholViewItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: AlcoholViewItem,
            newItem: AlcoholViewItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getAlcohols()[position]) {
            is AlcoholViewItem.Header -> 1
            is AlcoholViewItem.Item -> 2
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submit(alcohols: List<AlcoholViewItem>) = differ.submitList(alcohols)
    private fun getAlcohols() = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> AlcoholHeaderViewHolder(
                AlcoholGroupHeaderBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            2 -> AlcoholItemViewHolder(
                AlcoholItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> AlcoholHeaderViewHolder(
                AlcoholGroupHeaderBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getAlcohols()[position]
        when (getItemViewType(position)) {
            1 -> {
                (holder as AlcoholHeaderViewHolder).binding.apply {
                    val header = item as AlcoholViewItem.Header
                    tvHeader.text = header.type.toString()
                }
            }
            2 -> {
                (holder as AlcoholItemViewHolder).binding.apply {
                    val alcohol = (item as AlcoholViewItem.Item).beverage
                    when (alcohol.orderable) {
                        true -> ivAlcoAdd.show()
                        false -> ivAlcoAdd.gone()
                    }
                    if (alcohol.orderable) {
                        clAlcoItem.setOnClickListener { itemCallback(alcohol) }
                    }
                    tvAlcoName.text = alcohol.name
                    tvAlcoIngredients.text =
                        "${alcohol.size ?: ""} ${alcohol.ingredients?.joinToString(", ") ?: ""}"
                    when (alcohol.alcoholType) {
                        "draught" -> {
                            tvAlcoSmallPrice.text = "${alcohol.smallPrice} zł"
                            tvAlcoSmallPrice.show()
                            tvAlcoBigPrice.text = "${alcohol.bigPrice} zł"
                        }
                        else -> {
                            tvAlcoSmallPrice.hide()
                            tvAlcoBigPrice.text = "${alcohol.price} zł"
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return getAlcohols().size
    }
}