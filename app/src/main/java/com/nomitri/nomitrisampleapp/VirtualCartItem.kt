package com.nomitri.nomitrisampleapp

data class VirtualCartItem(
    val code: String,
    var quantity: Int = 1,
    var price: Int = 0
)
