package com.amplitude.kmp.mappings.ios

import cocoapods.AmplitudeSwift.*
import com.amplitude.kmp.*
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import platform.Foundation.setValue
import platform.darwin.NSObject

/**
 * iOS mapping utilities for Configuration.
 *
 * Maps KMP Configuration to iOS AMPConfiguration using CocoaPods cinterop.
 */

/**
 * Map KMP Configuration to iOS AMPConfiguration.
 * Note: CocoaPods cinterop exposes properties as functions, so we use setter methods.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun Configuration.toIOSConfiguration(): AMPConfiguration {
    val config = AMPConfiguration(apiKey = apiKey, instanceName = instanceName)

    // Set all properties using setter methods
    config.setFlushQueueSize(flushQueueSize.toLong())
    config.setFlushIntervalMillis(flushIntervalMillis.toLong())
    config.setOptOut(optOut)
    config.setLogLevel(logLevel.toIOSLogLevel())
    minIdLength?.let { config.setMinIdLength(it.toLong()) }
    partnerId?.let { config.setPartnerId(it) }
    callback?.let { config.setCallback(it.toIOSCallback()) }
    config.setFlushMaxRetries(flushMaxRetries.toLong())
    config.setUseBatch(useBatch)
    // AMPServerZone is a Swift-native enum that cinterop can't instantiate directly.
    // Use KVC to set the NSInteger-backed property by value (US=0, EU=1).
    (config as NSObject).setValue(NSNumber(long = serverZone.toIOSServerZoneValue()), forKey = "serverZone")
    serverUrl?.let { config.setServerUrl(it) }
    plan?.let { config.setPlan(it.toIOSPlan()) }
    ingestionMetadata?.let { config.setIngestionMetadata(it.toIOSIngestionMetadata()) }
    config.setTrackingOptions(trackingOptions.toIOSTrackingOptions())
    config.setEnableCoppaControl(enableCoppaControl)
    config.setFlushEventsOnClose(flushEventsOnClose)
    config.setMinTimeBetweenSessionsMillis(minTimeBetweenSessionsMillis.toInt().toLong())
    config.setIdentifyBatchIntervalMillis(identifyBatchIntervalMillis.toInt().toLong())
    config.setMigrateLegacyData(migrateLegacyData)
    config.setOffline(NSNumber(bool = offline))
    config.setInteractionsOptions(interactionsOptions.toIOSInteractionsOptions())

    return config
}

/**
 * Map KMP LogLevel to iOS AMPLogLevel.
 * Note: Using AMPLogLevel from AmplitudeSwift (Objective-C enum).
 * Cinterop exposes enums as Long values.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun LogLevel.toIOSLogLevel(): AMPLogLevel {
    return when (this) {
        LogLevel.NONE -> AMPLogLevelOFF  // OFF = 0
        LogLevel.ERROR -> AMPLogLevelERROR  // ERROR = 1
        LogLevel.WARN -> AMPLogLevelWARN  // WARN = 2
        LogLevel.LOG -> AMPLogLevelLOG  // LOG = 3
        LogLevel.DEBUG -> AMPLogLevelDEBUG  // DEBUG = 4
        LogLevel.VERBOSE -> AMPLogLevelDEBUG  // iOS doesn't have VERBOSE, use DEBUG
    }
}

/**
 * Map KMP ServerZone to its iOS NSInteger value.
 * AMPServerZone is a Swift-native enum that cinterop can't instantiate directly under Swift 6/Xcode 26.
 * Returns the raw NSInteger value: US=0, EU=1.
 */
internal fun ServerZone.toIOSServerZoneValue(): Long = when (this) {
    ServerZone.US -> 0L  // AMPServerZoneUS
    ServerZone.EU -> 1L  // AMPServerZoneEU
}

/**
 * Map KMP AutocaptureOption set to iOS AMPAutocaptureOptions.
 * Note: Using list-based constructor for Swift OptionSet.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun Set<AutocaptureOption>.toIOSAutocaptureOptions(): AMPAutocaptureOptions {
    val options = mutableListOf<AMPAutocaptureOptions>()

    this.forEach { option ->
        when (option) {
            AutocaptureOption.SESSIONS -> options.add(AMPAutocaptureOptions.Companion.sessions())
            AutocaptureOption.APP_LIFECYCLES -> options.add(AMPAutocaptureOptions.Companion.appLifecycles())
            AutocaptureOption.DEEP_LINKS -> options.add(AMPAutocaptureOptions.Companion.screenViews())  // Mapped to screenViews
            AutocaptureOption.SCREEN_VIEWS -> options.add(AMPAutocaptureOptions.Companion.screenViews())
            AutocaptureOption.ELEMENT_INTERACTIONS -> options.add(AMPAutocaptureOptions.Companion.elementInteractions())
            AutocaptureOption.FRUSTRATION_INTERACTIONS -> options.add(AMPAutocaptureOptions.Companion.frustrationInteractions())
        }
    }

    return if (options.isEmpty()) {
        AMPAutocaptureOptions()
    } else {
        AMPAutocaptureOptions(optionsToUnion = options)
    }
}

/**
 * Map KMP TrackingOptions to iOS AMPTrackingOptions.
 * Note: iOS SDK doesn't support all tracking options (e.g., adid, appSetId, device_brand, lat_lng).
 */
@OptIn(ExperimentalForeignApi::class)
internal fun TrackingOptions.toIOSTrackingOptions(): AMPTrackingOptions {
    val trackingOptions = AMPTrackingOptions()

    // Call disable methods for each disabled field
    // Only disable fields that iOS SDK supports
    disabledFields.forEach { field ->
        when (field) {
            "carrier" -> trackingOptions.disableTrackCarrier()
            "city" -> trackingOptions.disableTrackCity()
            "country" -> trackingOptions.disableTrackCountry()
            "device_manufacturer" -> trackingOptions.disableTrackDeviceManufacturer()
            "device_model" -> trackingOptions.disableTrackDeviceModel()
            "dma" -> trackingOptions.disableTrackDMA()
            "ip_address" -> trackingOptions.disableTrackIpAddress()
            "idfv" -> trackingOptions.disableTrackIDFV()
            "language" -> trackingOptions.disableTrackLanguage()
            "os_name" -> trackingOptions.disableTrackOsName()
            "os_version" -> trackingOptions.disableTrackOsVersion()
            "platform" -> trackingOptions.disableTrackPlatform()
            "region" -> trackingOptions.disableTrackRegion()
            "version_name" -> trackingOptions.disableTrackVersionName()
            // Unsupported on iOS: adid, appSetId, device_brand, lat_lng
        }
    }

    return trackingOptions
}

/**
 * Map KMP InteractionsOptions to iOS AMPInteractionsOptions.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun InteractionsOptions.toIOSInteractionsOptions(): AMPInteractionsOptions {
    val rageClickOptions = AMPRageClickOptions(enabled = rageClick.enabled)
    val deadClickOptions = AMPDeadClickOptions(enabled = deadClick.enabled)

    return AMPInteractionsOptions(
        rageClick = rageClickOptions,
        deadClick = deadClickOptions
    )
}
