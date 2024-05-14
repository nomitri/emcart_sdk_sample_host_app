package com.nomitri.nomitrisampleapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class VirtualShoppingCartAdapter : RecyclerView.Adapter<VirtualShoppingCartViewHolder>() {

    private val mData = mutableListOf<VirtualCartItem>()
    var itemClickListener: ((VirtualCartItem) -> Unit)? = null

    fun submitList(newData: List<VirtualCartItem>) {
        mData.clear()
        mData.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VirtualShoppingCartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VirtualShoppingCartViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: VirtualShoppingCartViewHolder, position: Int) {
        val virtualCartItem: VirtualCartItem = mData[position]
        holder.bind(item = virtualCartItem, clickListener = itemClickListener)
    }
}