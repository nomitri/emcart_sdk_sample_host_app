package com.nomitri.nomitrisampleapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VirtualShoppingCartViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_shopping_cart, parent, false)) {
    private var mTitleView: TextView = itemView.findViewById(R.id.item_name)
    private var mPriceView: TextView = itemView.findViewById(R.id.item_price)
    private var mQuantityView: TextView = itemView.findViewById(R.id.item_quantity)
    private var mIncrementButton: Button = itemView.findViewById(R.id.increment_button)
    private var mDecrementButton: Button = itemView.findViewById(R.id.decrement_button)

    fun bind(item: VirtualCartItem, clickListener: ((VirtualCartItem) -> Unit)? = null) {
        mTitleView.text = item.code
        mQuantityView.text = item.quantity.toString()
        mPriceView.text = "${item.price} €"

        mIncrementButton.setOnClickListener {
            clickListener?.invoke(item)
        }
        mDecrementButton.setOnClickListener {
            clickListener?.invoke(item)
        }
    }
}
