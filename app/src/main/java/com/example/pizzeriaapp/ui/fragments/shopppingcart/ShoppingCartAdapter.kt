package com.example.pizzeriaapp.ui.fragments.shopppingcart

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.cart.CartItemInterface
import com.example.pizzeriaapp.databinding.CartItemBinding
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.hide
import com.example.pizzeriaapp.utils.views.show
import javax.inject.Inject

class ShoppingCartAdapter @Inject constructor() :
    RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder>() {
    inner class ShoppingCartViewHolder(val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    lateinit var deleteCallback: (item: CartItemInterface) -> Unit
    lateinit var increaseAmount: (item: CartItemInterface) -> Unit
    lateinit var decreaseAmount: (item: CartItemInterface) -> Unit

    var showItemMenu = true
    var showAmountButtons = true

    private val diffCallback = object : DiffUtil.ItemCallback<CartItemInterface>() {
        override fun areItemsTheSame(
            oldItem: CartItemInterface,
            newItem: CartItemInterface
        ): Boolean {
            return oldItem.getDescription() == newItem.getDescription() && oldItem.amount == newItem.amount
        }

        override fun areContentsTheSame(
            oldItem: CartItemInterface,
            newItem: CartItemInterface
        ): Boolean {
            return oldItem.getDescription() == newItem.getDescription() && oldItem.amount == newItem.amount
        }
    }


    private val differ = AsyncListDiffer(this, diffCallback)
    fun submit(items: List<CartItemInterface>) {
        differ.submitList(items)
        notifyDataSetChanged()
    }

    fun getItems() = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder {
        return ShoppingCartViewHolder(
            CartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        val item = getItems()[position]
        holder.binding.apply {
            when (showItemMenu) {
                true -> ivMoreCartItem.show()
                false -> ivMoreCartItem.hide()
            }
            when (showAmountButtons) {
                true -> {
                    ivIncreaseAmount.show()
                    ivDecreaseAmount.show()
                    tvWholePrice.show()
                }
                false -> {
                    ivIncreaseAmount.gone()
                    tvWholePrice.gone()
                    ivDecreaseAmount.gone()
                }
            }
            tvCartItemInfo.text = item.getDescription()
            tvWholePrice.text = "${item.getPrice()} zł"
            tvAmount.text = item.amount.toString()
            ivIncreaseAmount.setOnClickListener {
                increaseAmount(item)
            }
            ivDecreaseAmount.setOnClickListener {
                decreaseAmount(item)
            }
            ivMoreCartItem.setOnClickListener {
                val menu = PopupMenu(it.context, it)
                menu.inflate(R.menu.cart_item_menu)
                val menuItem = menu.menu.getItem(0)
                val ss = SpannableString("Usuń")
                ss.setSpan(ForegroundColorSpan(Color.BLACK), 0, ss.length, 0)
                menuItem.title = ss
                menu.setOnMenuItemClickListener { menuItem ->
                    if (menuItem.itemId == R.id.deleteCartItem) {
                        deleteCallback(item)
                    }
                    true
                }
                menu.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return getItems().size
    }
}