package com.amplitude.kmp.mappings.ios

import cocoapods.AmplitudeSwift.*
import com.amplitude.kmp.Configuration
import com.amplitude.kmp.LogLevel
import com.amplitude.kmp.ServerZone
import com.amplitude.kmp.AutocaptureOption
import com.amplitude.kmp.TrackingOptions
import com.amplitude.kmp.InteractionsOptions
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

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
    config.setServerZone(serverZone.toIOSServerZone())
    serverUrl?.let { config.setServerUrl(it) }
    plan?.let { config.setPlan(it.toIOSPlan()) }
    ingestionMetadata?.let { config.setIngestionMetadata(it.toIOSIngestionMetadata()) }
    config.setTrackingOptions(trackingOptions.toIOSTrackingOptions())
    config.setEnableCoppaControl(enableCoppaControl)
    config.setFlushEventsOnClose(flushEventsOnClose)
    config.setMinTimeBetweenSessionsMillis(minTimeBetweenSessionsMillis.toInt().toLong())
    config.setIdentifyBatchIntervalMillis(identifyBatchIntervalMillis.toInt().toLong())
    config.setMigrateLegacyData(migrateLegacyData)
    offline?.let { config.setOffline(NSNumber(bool = it)) }
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
 * Map KMP ServerZone to iOS AMPServerZone.
 * Note: Using AMPServerZone from AmplitudeCore (Objective-C enum).
 * Cinterop exposes enums as Long values.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun ServerZone.toIOSServerZone(): AMPServerZone {
    return when (this) {
        ServerZone.US -> AMPServerZoneUS  // US = 0
        ServerZone.EU -> AMPServerZoneEU  // EU = 1
    }
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
