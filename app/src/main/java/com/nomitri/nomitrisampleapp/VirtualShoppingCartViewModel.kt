package com.nomitri.nomitrisampleapp

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.nomitri.ailib.event_data.BasketChangeType
import com.nomitri.nomitrisampleapp.repository.MainRepository
import kotlinx.coroutines.launch


class VirtualShoppingCartViewModel : ViewModel() {
    private val repository: MainRepository = MainRepository
    val cartContent: LiveData<List<VirtualCartItem>> = repository.getContent()

    private val _manualBarcodeEntry = MutableLiveData<VirtualCartItem>(null)
    val manualBarcodeEntry: LiveData<VirtualCartItem> = _manualBarcodeEntry

    fun cartContentChange(gtin: String, type: BasketChangeType, amount: Int) {
        when (type) {
            BasketChangeType.ADD -> {
                if (MainRepository.itemExists(gtin)) {
                    Handler(Looper.getMainLooper()).post {
                        MainRepository.addBarcodeToCart(gtin, amount)
                    }
                } else {
                    viewModelScope.launch {
                        val virtualCartItem = VirtualCartItem(gtin)
                        Handler(Looper.getMainLooper()).post {
                            MainRepository.addItemToVirtualShoppingCart(virtualCartItem)
                        }
                    }
                }
            }

            BasketChangeType.REMOVE -> {
                Handler(Looper.getMainLooper()).post {
                    MainRepository.removeBarcodeFromCart(gtin, amount)
                }
            }
        }
    }

    fun totalPrice(cartContent: List<VirtualCartItem>): String {
        val price = cartContent.sumOf { it.price.toDouble() }
        return "$price €"
    }

    fun manualCodeEntry(gtin: String, type: BasketChangeType, amount: Int) {
        when (type) {
            BasketChangeType.ADD -> {
                if (MainRepository.itemExists(gtin)) {
                    Handler(Looper.getMainLooper()).post {
                        MainRepository.addBarcodeToCart(gtin, amount)
                        val virtualCartItem =
                            MainRepository.getContent().value?.find { it.code == gtin } ?: return@post
                        _manualBarcodeEntry.postValue(virtualCartItem)
                    }
                } else {
                    viewModelScope.launch {
                        val virtualCartItem = VirtualCartItem(gtin)
                        Handler(Looper.getMainLooper()).post {
                            MainRepository.addItemToVirtualShoppingCart(virtualCartItem)
                            _manualBarcodeEntry.postValue(virtualCartItem)
                        }
                    }
                }
            }

            BasketChangeType.REMOVE -> {
                Handler(Looper.getMainLooper()).post {
                    MainRepository.removeBarcodeFromCart(gtin, amount)
                    val virtualCartItem =
                        MainRepository.getContent().value?.find { it.code == gtin } ?: return@post
                    _manualBarcodeEntry.postValue(virtualCartItem)
                }
            }
        }
    }
}
