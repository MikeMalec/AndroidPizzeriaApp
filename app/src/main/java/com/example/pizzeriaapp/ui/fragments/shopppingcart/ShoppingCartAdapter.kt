package com.example.pizzeriaapp.ui.fragments.shopppingcart

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
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


    var items = listOf<CartItemInterface>()

    inner class CartDiffUtil(
        private val oldList: List<CartItemInterface>,
        private val newList: List<CartItemInterface>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].getDescription() == newList[newItemPosition].getDescription()
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return when {
                oldList[oldItemPosition].getDescription() == newList[newItemPosition].getDescription() -> false
                oldList[oldItemPosition].getPrice() == newList[newItemPosition].getPrice() -> false
                oldList[oldItemPosition].amount == newList[newItemPosition].amount -> false
                else -> true
            }
        }

    }

    fun submit(newItems: List<CartItemInterface>) {
        Log.d("XXX", "NEW ITEMS .. $newItems")
        val diffUtil = CartDiffUtil(items, newItems)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        items = newItems
        diffResults.dispatchUpdatesTo(this)
    }

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
        val item = items[position]
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
        return items.size
    }
}