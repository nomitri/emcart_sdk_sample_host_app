<a href="https://www.nomitri.com/">
<img src="https://www.nomitri.com/wp-content/uploads/2022/12/nomitri_logo_dark.svg?x16522" alt="Nomitri SDK Logo" width="400" />
</a>

# Nomitri Retail Android SDK

The Nomitri Retail Android SDK enables developers to integrate Nomitri's advanced AI-powered shopping assistant and
fraud detection capabilities into their Android applications, turning any shopping cart into an AI-enabled self-checkout
solution. This README file provides instructions on how to build and run this sample application to get started quickly.

A more extensive documentation of the SDK is available at [Nomitri SDK Documentation](https://sdk.nomitri.com/).

## Table of Contents

- [Prerequisites](#prerequisites)
- [Description](#description)
- [Building](#building)
- [Supported devices](#supported-devices)
- [Troubleshooting](#troubleshooting)
- [Support](#Support)

## Prerequisites

To use the Nomitri SDK in your Android project, ensure that you have received an authentication token for the Nomitri
Maven repository, as well as a license for the SDK.

1. In this sample app's `gradle.properties` file, add the Maven authentication token as the value for the
   `artifactRegistryMavenSecret` variable. The value should not be surrounded by quotes.

2. If necessary, update the Nomitri SDK version in `app/build.gradle`:

```gradle
// Nomitri Retail SDK
implementation 'com.nomitri:retailsdk-qc:x.y.z'
```

3. Fill in the license details in `app/src/main/java/com/nomitri/nomitrisampleapp/MainActivity.kt`:

```kotlin
private fun initNomitriSDK(): NomitriCameraView {
    val license = License(
        apiKey = "YOUR_API_KEY",
        licenseKey = "YOUR_LICENSE_KEY",
        storeGroupKey = "YOUR_STORE_KEY",
        storeUniqueId = "YOUR_STORE_ID",
    )
```

## Description

The sample application is an example of a minimal integration of the Nomitri SDK. It combines the camera view generated
by the SDK with a shopping list displayed on the right side of the screen. Scanned items and quantities are shown in the
shopping list. It also demonstrates how custom UI buttons are added to the view for manual barcode entry or fruits and
vegetables visual recognition.

No backend integration is provided in this example; it is up to the implementor to connect to the store's price database
to resolve the items' names and prices from their barcodes and to the store POS to transmit the shopping list and
proceed to payment.

Additionally, the SDK reports events such as detected frauds and scanned items in the form of logs, sometimes
accompanied by a short video snippet of the action. The implementor should decide whether to send these logs to a
backend service to perform shopping trip verifications before or during payment.

## Building

The sample application was developed and built under Android Studio Giraffe (2022.3.1 Patch 1) and tested on Android 12
and Android 13.

- Open the project in Android Studio.
- Sync the project with Gradle files.
- Build and run the application on a supported device.

## Supported devices

The sample application was optimized to run on tablet devices in landscape orientation. A phone layout in portrait
orientation is not guaranteed to be supported.

Choosing the appropriate device to run or test the Nomitri Retail SDK is critical to ensure sufficient performance of
the AI-related features such as error and fraud detection, item tracking, and scanning. The device should be equipped
with an AI-capable chipset supporting the Qualcomm SNPE drivers (Qualcomm chipsets only). Therefore, it is not possible
to test the application in the Android Emulator. For convenience, it is possible to replay a video file as opposed to
using the device's camera live video input (see [Test with Video](https://sdk.nomitri.com/usage/example.html#test)).

Lastly, the device must be connected to the internet to validate the SDK license at app startup. The SDK use will be
blocked otherwise.

See [Supported Devices Best Practices](https://sdk.nomitri.com/best-practices.html) for more details on supported
devices.

## Troubleshooting

See the [troubleshooting](https://sdk.nomitri.com/troubleshooting.html) section of the SDK documentation for further
help.

### License Validation Issues:

- Ensure the device is connected to the internet.
- Double-check the API key, license key, store group key, and store unique ID.

### Performance Issues:

- Verify that the device has a Qualcomm AI-capable chipset.
- Make sure the device meets the minimum hardware requirements outlined in
  the [Supported Devices Best Practices](https://sdk.nomitri.com/best-practices.html).

### Build Errors:

- Ensure all dependencies are correctly added in the `build.gradle` file and that the Maven authentication token is
  declared in `gradle.properties`.
- Sync Gradle files after making changes to the `gradle.properties` or `build.gradle` files.

## Support

If you encounter any issues, have questions, or need assistance with integrating the Nomitri SDK, please reach out via
our [support page](https://sdk.nomitri.com/support.html). We are here to help you with integration assistance,
troubleshooting issues, providing guidance on best practices and devices or answering any questions about the SDK
features and capabilities.

Additionally, we welcome your suggestions to improve the Nomitri SDK or this sample application.
