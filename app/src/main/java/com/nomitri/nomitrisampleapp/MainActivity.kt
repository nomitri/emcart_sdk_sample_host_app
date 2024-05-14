package com.nomitri.nomitrisampleapp

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.input.input
import com.nomitri.ailib.NomitriSDK
import com.nomitri.ailib.event_data.BasketChangeType
import com.nomitri.ailib.models.License
import com.nomitri.ailib.models.NomitriCartItem
import com.nomitri.ailib.states_logic.Event
import com.nomitri.ailib.views.NomitriCameraView
import com.nomitri.nomitrisampleapp.interfaces.MainActivityToShoppingCartProtocol
import com.nomitri.nomitrisampleapp.interfaces.ShoppingCartToMainActivityProtocol
import com.nomitri.nomitrisampleapp.repository.MainRepository

class MainActivity : AppCompatActivity(), ShoppingCartToMainActivityProtocol {
    private lateinit var nomitriSDK: NomitriSDK
    private lateinit var keyboardButton: Button
    private lateinit var fruitVeggieButton: Button
    private lateinit var mainActivityToShoppingCartFragmentProtocol: MainActivityToShoppingCartProtocol

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = if (resources.getBoolean(R.bool.portrait_only)) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        keyboardButton = findViewById(R.id.keyboard_button)
        fruitVeggieButton = findViewById(R.id.fruit_veggie_button)

        attachNomitriSDK()
        attachShoppingCartFragment()

        keyboardButton.setOnClickListener {
            openManualBarcodeEntryDialog()
        }
        fruitVeggieButton.setOnClickListener {
            nomitriSDK.enableVisualClassification()
        }
    }

    private fun attachNomitriSDK() {
        val nomitriAIViewPlaceHolder =
            findViewById<ConstraintLayout>(R.id.nomitri_camera_view_placeholder)
        val nomitriCameraView = initNomitriSDK()
        nomitriAIViewPlaceHolder.addView(nomitriCameraView)
    }

    private fun initNomitriSDK(): NomitriCameraView {
        val license = License(
            apiKey = "YOUR_API_KEY", // Provided by Nomitri
            licenseKey = "YOUR_LICENCE_KEY", // Provided by Nomitri
            storeGroupKey = "YOUR_STORE_KEY", // Provided by Nomitri
            storeUniqueId = "YOUR_STORE_ID", // Provided by Nomitri
        )

        nomitriSDK = NomitriSDK(this)
        return nomitriSDK.configureView(
            license = license,
            persistedCartItems = getMockedPersistenceItems(), // If SDK needs to be initialized with some items. In case of re-opening a session.
            recordingConfig = null, // Used for collecting video data
            uxEventHandler = { uxEvent ->
                // For reacting Ux Events sent by SDK. Developers can choose their UI. Or use built in SDK UI Elements like below.
                when (uxEvent.event) {
                    Event.ITEM_ADDED -> {
                        nomitriSDK.showScanMessage(getString(R.string.item_added))
                    }

                    Event.ITEM_REMOVED -> {
                        nomitriSDK.showScanMessage(getString(R.string.item_removed))
                    }

                    Event.ITEM_ADDED_WITHOUT_SCAN -> {
                        nomitriSDK.showErrorMessage(getString(R.string.item_added_without_scan))
                    }

                    Event.ITEM_CLASSIFIED -> {
                        nomitriSDK.showInfoMessage(getString(R.string.item_classified_add))
                    }

                    Event.ERROR_CORRECTED -> {
                        nomitriSDK.showInfoMessage(getString(R.string.error_corrected))
                    }

                    Event.TOO_MANY_TRACKS -> {
                        nomitriSDK.showWarningMessage(getString(R.string.too_many_tracks))
                    }

                    Event.SHOW_ITEM_LONGER_NEXT_TIME -> {
                        nomitriSDK.showWarningMessage(getString(R.string.show_item_longer_next_time))
                    }

                    Event.WRONG_GTIN_SCANNED_REMOVE -> {
                        nomitriSDK.showWarningMessage(getString(R.string.wrong_gtin_scanned_remove))
                    }

                    Event.ITEM_REIDENTIFIED -> {
                        nomitriSDK.showInfoMessage(getString(R.string.item_reidentified))
                    }

                    Event.LOST_REIDENTIFIED_TRACK -> {
                        nomitriSDK.showWarningMessage(getString(R.string.lost_reidentified_track))
                    }

                    Event.DID_YOU_ACTUALLY_REMOVE_ITEM -> {
                        nomitriSDK.showErrorMessage(getString(R.string.scanned_item_not_removed))
                    }

                    Event.DEVICE_IS_RUNNING_SLOW -> {
                        nomitriSDK.showWarningMessage(getString(R.string.slow_device))
                    }

                    Event.ITEM_CLASSIFIED_AS_OTHER -> {
                        nomitriSDK.showDefaultMessageForEvent(uxEvent)
                    }

                    else -> {}
                }
            },
            basketChangeEventHandler = { basketChangeEvent ->
                // For reacting Basket Change Events sent by SDK. Developers can choose their UI.
                // Only called when an item is successfully scanned and added/removed or visually classified.
                Toast.makeText(
                    this,
                    "BASKET CHANGE ${basketChangeEvent.type}: ${basketChangeEvent.barcode} ",
                    Toast.LENGTH_SHORT
                ).show()
                mainActivityToShoppingCartFragmentProtocol.onBasketChangeEvent(
                    basketChangeEvent.barcode,
                    basketChangeEvent.type
                )
            },
            interactionHandler = { interactionEventType ->
                // For reacting Interaction Events sent by SDK. Like SDK CameraView Button Clicks
                Toast.makeText(
                    this,
                    "INTERACTION ${interactionEventType.name} ",
                    Toast.LENGTH_SHORT
                ).show()
            },
            interopLogsHandler = { nomitriLogs ->
                // Logs sent by SDK about tracks/items/errors for reporting purposes
            },
            interopImageLogsHandler = { nomitriImageLogs ->
                // SDK Image Crop Logs sent by SDK about tracks/items/errors for reporting purposes
            },
            recordingListener = { isRecording ->
                // If a recording configuration is passed, sdk would notify when it is recording the session.
                Toast.makeText(
                    this,
                    "RECORDING : $isRecording ",
                    Toast.LENGTH_SHORT
                ).show()
            },
            visualClassificationHandler = { visualClassification ->
                // For handling visual classification events for fruits and vegetables products
                Toast.makeText(
                    this,
                    "CLASSIFICATION: ${visualClassification.itemIdentifiers.size} candidate(s)",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, "Visual Classification: $visualClassification")
                nomitriSDK.disableVisualClassification()
            },
            testUri = null //Uri.parse("file:///android_asset/test_shopping_sequence.mp4")
        )
    }

    private fun getMockedPersistenceItems(): List<NomitriCartItem> {
        return listOf(
            NomitriCartItem(
                code = "4105250022003",
                amount = 2
            )
        )
    }

    private fun initPersistenceItems() {
        val persistenceItems = getMockedPersistenceItems()

        persistenceItems.forEach {
            mainActivityToShoppingCartFragmentProtocol.onBasketChangeEvent(
                it.code,
                BasketChangeType.ADD,
                it.amount
            )
        }
    }

    private fun attachShoppingCartFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.fragment_container_small,
                VirtualShoppingCartFragment(),
                "shoppingCartFragment"
            )
            .commit()
    }

    override fun onAttachFragment(fragment: androidx.fragment.app.Fragment) {
        super.onAttachFragment(fragment)

        if (fragment is VirtualShoppingCartFragment) {
            fragment.setOnCartItemRemovalCallback(this)
            mainActivityToShoppingCartFragmentProtocol = fragment
            initPersistenceItems()
        }
    }

    override fun onCartItemClicked(item: VirtualCartItem) {
        val initialAmount = item.quantity
        var finalAmount = item.quantity

        val itemDetailPopup = MaterialDialog(this).show {
            customView(R.layout.layout_item_detail_popup)
                .cornerRadius(20f)
            cancelOnTouchOutside(false)
            cancelable(false)
        }

        val view = itemDetailPopup.getCustomView()
        val itemNameView = view.findViewById<TextView>(R.id.item_name_tw)
        val itemPriceView = view.findViewById<TextView>(R.id.item_price_tw)
        val itemCountView = view.findViewById<TextView>(R.id.amount_tw)
        val plusButton = view.findViewById<TextView>(R.id.plus_button)
        val minusButton = view.findViewById<TextView>(R.id.minus_button)
        val updateButton = view.findViewById<Button>(R.id.amount_upg_btn)

        itemNameView.text = item.code
        itemPriceView.text = item.price.toString()
        itemCountView.text = initialAmount.toString()

        plusButton.setOnClickListener {
            finalAmount += 1
            itemPriceView.text = item.price.toString()
            itemCountView.text = finalAmount.toString()
        }

        minusButton.setOnClickListener {
            if (finalAmount >= 1) {
                finalAmount -= 1
                itemPriceView.text = item.price.toString()
                itemCountView.text = finalAmount.toString()
            }
        }

        updateButton.setOnClickListener {
            if (finalAmount > initialAmount) {
                nomitriSDK.updateItemCount(item.code, finalAmount, item.code)
                mainActivityToShoppingCartFragmentProtocol.onBasketChangeEvent(
                    gtin = item.code,
                    type = BasketChangeType.ADD,
                    amount = finalAmount - initialAmount
                )
            } else if (finalAmount < initialAmount) {
                nomitriSDK.updateItemCount(item.code, finalAmount, item.code)
                mainActivityToShoppingCartFragmentProtocol.onBasketChangeEvent(
                    gtin = item.code,
                    type = BasketChangeType.REMOVE,
                    amount = initialAmount - finalAmount
                )
            } else {
                nomitriSDK.updateItemCount(item.code, finalAmount, item.code)
            }

            // Send to sdk with a delay because cart update operation above is async
            Handler(Looper.getMainLooper()).postDelayed({
                notifySDKForCartChange()
            }, 1000)

            itemDetailPopup.dismiss()
        }
    }

    private fun notifySDKForCartChange() {
        val cartContent = MainRepository.getContent().value
        val nomitriCartContent = cartContent?.map {
            NomitriCartItem(it.code, it.quantity)
        }

        if (nomitriCartContent != null) {
            nomitriSDK.setCartContent(nomitriCartContent)
        }
    }

    private fun openManualBarcodeEntryDialog() {
        val type = InputType.TYPE_CLASS_NUMBER
        var barcode = ""
        val popup = MaterialDialog(this)
        popup.show {
            title(res = R.string.manual_barcode_entry)
            input(waitForPositiveButton = false, inputType = type) { _, text ->
                barcode = text.toString()
            }
            positiveButton {
                val state = nomitriSDK.getSDKState()
                if (state == "ADD" || state == "REMOVE") {
                    mainActivityToShoppingCartFragmentProtocol.onManualCodeEntered(barcode, state)
                }
            }
            negativeButton {
                dismiss()
            }
        }
    }

    companion object {
        private const val TAG = "Nomitri Sample App"
    }
}
