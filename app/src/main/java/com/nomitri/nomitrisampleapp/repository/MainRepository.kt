package com.nomitri.nomitrisampleapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nomitri.nomitrisampleapp.VirtualCartItem
import java.util.Collections


/**
 * VirtualShoppingCartRepository is a singleton that keeps track of everything that is added/removed
 * to the cart. It is the single source of truth for what the shopping cart contains at any
 * point of time.
 */
object MainRepository {

    private val contentRepository = MutableLiveData<List<VirtualCartItem>>(Collections.emptyList())
    fun getContent(): LiveData<List<VirtualCartItem>> = contentRepository


    private fun updateCartContent(updatedList: List<VirtualCartItem>) {
        contentRepository.value = updatedList
    }

    fun addBarcodeToCart(barcode: String, count: Int) {
        incrementItemCount(barcode, count)
    }

    fun removeBarcodeFromCart(barcode: String, count: Int) {
        decrementItemCount(barcode, count)
    }

    fun itemExists(barcode: String): Boolean {
        val list = ArrayList(contentRepository.value!!)
        return list.any { it.code == barcode }
    }

    private fun incrementItemCount(barcode: String, increment: Int = 1) {
        val list = ArrayList(contentRepository.value!!)
        val item = list.find { it.code == barcode }
        if (item != null) {
            item.quantity += increment
            updateCartContent(list)
        }
    }

    private fun decrementItemCount(barcode: String, decrement: Int = 1) {
        val list = ArrayList(contentRepository.value!!)
        val item = list.find { it.code == barcode } ?: return
        if (item.quantity > decrement) {
            item.quantity -= decrement
        } else {
            list.remove(item)
        }
        updateCartContent(list)
    }

    fun addItemToVirtualShoppingCart(item: VirtualCartItem, increment: Int = 1) {
        val list = ArrayList(contentRepository.value!!)
        val updateItem = list.find { it.code == item.code }
        if (updateItem != null) {
            updateItem.quantity += increment
        } else {
            list.add(item)
        }
        updateCartContent(list)
    }
}
