package com.nomitri.nomitrisampleapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nomitri.ailib.event_data.BasketChangeType
import com.nomitri.nomitrisampleapp.interfaces.MainActivityToShoppingCartProtocol
import com.nomitri.nomitrisampleapp.interfaces.ShoppingCartToMainActivityProtocol


class VirtualShoppingCartFragment : Fragment(), MainActivityToShoppingCartProtocol {

    private lateinit var viewModel: VirtualShoppingCartViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var priceInfoViewGroup: View
    private lateinit var priceView: TextView
    private lateinit var itemsCountView: TextView
    private lateinit var priceDetailView: TextView
    private lateinit var emptyView: LinearLayout
    private lateinit var virtualShoppingCartAdapter: VirtualShoppingCartAdapter

    private lateinit var mCartToMainActivityProtocolsCallback: ShoppingCartToMainActivityProtocol

    fun setOnCartItemRemovalCallback(callback: ShoppingCartToMainActivityProtocol) {
        this.mCartToMainActivityProtocolsCallback = callback
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this)[VirtualShoppingCartViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shopping_cart, container, false)
        recyclerView = root.findViewById(R.id.virtual_shopping_cart_recycler_view)
        priceInfoViewGroup = root.findViewById(R.id.viewgroup_price_info)
        priceView = root.findViewById(R.id.textview_total_shopping_cart)
        itemsCountView = root.findViewById(R.id.items_count_text_view)
        priceDetailView = root.findViewById(R.id.textview_total_price)
        emptyView = root.findViewById(R.id.empty_linear_layout)

        setObservers()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        virtualShoppingCartAdapter = VirtualShoppingCartAdapter()
        virtualShoppingCartAdapter.itemClickListener = { virtualCartItem ->
            mCartToMainActivityProtocolsCallback.onCartItemClicked(virtualCartItem)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = virtualShoppingCartAdapter
        }

    }

    private fun updateUI(cartContent: List<VirtualCartItem>) {
        val modifiedCartContent = cartContent.toMutableList()
        if (modifiedCartContent.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
        }

        virtualShoppingCartAdapter.submitList(modifiedCartContent.reversed())
        updateBottomView(modifiedCartContent)

    }

    private fun updateBottomView(cartContent: List<VirtualCartItem>) {
        if (cartContent.isEmpty()) {
            priceInfoViewGroup.visibility = View.GONE
        } else {
            priceInfoViewGroup.visibility = View.VISIBLE
            val cartTotal = viewModel.totalPrice(cartContent)
            priceDetailView.text = cartTotal
            val totalItemsCount = cartContent.sumOf { it.quantity }
            itemsCountView.text = "$totalItemsCount items"
        }
    }

    private fun setObservers() {
        viewModel.cartContent.observe(viewLifecycleOwner) { cartContent ->
            updateUI(cartContent)
        }

        viewModel.manualBarcodeEntry.observe(viewLifecycleOwner) { virtualCartItem ->
            if (virtualCartItem != null) {
                mCartToMainActivityProtocolsCallback.onCartItemClicked(virtualCartItem)
            }
        }
    }

    override fun onBasketChangeEvent(gtin: String, type: BasketChangeType, amount: Int) {
        viewModel.cartContentChange(gtin, type, amount)
    }

    override fun onManualCodeEntered(code: String, state: String) {
        val type = BasketChangeType.valueOf(state)
        viewModel.manualCodeEntry(code, type, 1)
    }
}
