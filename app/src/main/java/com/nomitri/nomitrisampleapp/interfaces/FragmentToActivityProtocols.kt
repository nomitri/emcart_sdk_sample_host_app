package com.nomitri.nomitrisampleapp.interfaces

import com.nomitri.ailib.event_data.BasketChangeType
import com.nomitri.nomitrisampleapp.VirtualCartItem

interface ShoppingCartToMainActivityProtocol {
    fun onCartItemClicked(item: VirtualCartItem)
}

interface MainActivityToShoppingCartProtocol {
    fun onBasketChangeEvent(gtin: String, type: BasketChangeType, amount: Int = 1)
    fun onManualCodeEntered(code: String, state: String)
}
