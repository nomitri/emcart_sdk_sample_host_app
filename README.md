# Nomitri SDK Android Library

![Nomitri SDK Logo](link-to-logo.png) <!-- If you have a logo, you can add it here -->

The Nomitri SDK Android Library allows developers to integrate Nomitri's advanced AI-powered scanning and tracking capabilities into their Android applications. This README.md file provides instructions on how to use the SDK and includes code samples to get you started quickly.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Event Handling](#event-handling)
- [License](#license)

## Installation

To use the Nomitri SDK Android Library in your Android project, follow these steps:

1. Download the latest version of the SDK from [Nomitri's website](https://www.nomitri.com).
2. Copy the downloaded AAR file to your project's `libs` directory.
3. Add the following dependencies in your app's `build.gradle` file:

```gradle
dependencies {
    //////////// Nomitri Retail SDK Dependencies Start \\\\\\\\\\\\\\
    implementation(name:'nomitri-retailapp-sdk-release', ext:'aar')
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation 'com.squareup.retrofit2:converter-gson:2.8.1'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.5.0'
    implementation 'com.squareup.retrofit2:converter-scalars:2.1.0'
    implementation 'com.github.mrmike:ok2curl:0.7.0'
    implementation 'com.google.protobuf:protobuf-javalite:3.14.0'
    implementation "io.reactivex.rxjava2:rxandroid:2.1.1"
    implementation 'com.afollestad.material-dialogs:core:3.3.0'
    implementation 'com.afollestad.material-dialogs:input:3.3.0'
    //////////// Nomitri Retail SDK Dependencies End \\\\\\\\\\\\\\
    
    // Other dependencies...
}
```

## Usage

In your Android application, create an instance of NomitriSDK and call the configureView method to initialize it. Make sure to replace the placeholders with your actual API key, license key, and store key provided by Nomitri.

```
private fun initNomitriSDK(): NomitriCameraView {
    nomitriSDK = NomitriSDK(this)
    return nomitriSDK.configureView(
        apiKey = "YOUR_API_KEY", // Provided by Nomitri
        licenseKey = "YOUR_LICENSE_KEY", // Provided by Nomitri
        storeGroupKey = "YOUR_STORE_KEY", // Provided by Nomitri
        persistedCartItems = listOf(
            NomitriCartItem(
                code = "4105250022003",
                amount = 2
            )
        ),
        // Additional configuration options...
    )
}
```

Handle various events sent by the SDK using the provided event handlers. Customize your application's behavior based on these events:

## Event Handling

### UX Event Handling:
The uxEventHandler is called to react to UX events sent by the SDK. You can choose to display your UI or use the built-in SDK UI elements based on the event received.

``` 
// Example implementation of the UX event handler
uxEventHandler = { uxEvent ->
    when (uxEvent.event) {
        Event.ITEM_ADDED -> {
            // Display a message when an item is added to the cart
            nomitriSDK.showItemAddedMessage(getString(R.string.item_added))
        }
        // Add more cases for other events...
    }
}
```

### Basket Change Event Handling
The basketChangeEventHandler is called when an item is successfully scanned and added/removed or visually classified. Customize your application's behavior based on the change type and GTIN received.

```
// Example implementation of the basket change event handler
basketChangeEventHandler = { basketChangeEvent ->
    // Display a Toast with information about the change
    Toast.makeText(
        this,
        "BASKET CHANGE ${basketChangeEvent.changeType}: ${basketChangeEvent.gtin} ",
        Toast.LENGTH_SHORT
    ).show()
}
```

## Licence
[TODO: // ADD LICENCE INFO]