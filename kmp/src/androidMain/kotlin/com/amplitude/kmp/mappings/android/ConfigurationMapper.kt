package com.amplitude.kmp.mappings.android

import android.content.Context
import com.amplitude.android.Configuration as AndroidConfiguration
import com.amplitude.android.AutocaptureOption as AndroidAutocaptureOption
import com.amplitude.android.InteractionsOptions as AndroidInteractionsOptions
import com.amplitude.android.RageClickOptions as AndroidRageClickOptions
import com.amplitude.android.DeadClickOptions as AndroidDeadClickOptions
import com.amplitude.android.TrackingOptions as AndroidTrackingOptions
import com.amplitude.core.ServerZone as AndroidServerZone
import com.amplitude.kmp.Configuration
import com.amplitude.kmp.ServerZone
import com.amplitude.kmp.AutocaptureOption
import com.amplitude.kmp.InteractionsOptions
import com.amplitude.kmp.TrackingOptions

/**
 * Map KMP Configuration to Android SDK Configuration.
 */
internal fun Configuration.toAndroidConfiguration(): AndroidConfiguration {
    val context = (androidContext as? Context)
        ?: throw IllegalArgumentException("androidContext must be android.content.Context on Android platform")

    return AndroidConfiguration(
        apiKey = apiKey,
        context = context,
        flushQueueSize = flushQueueSize,
        flushIntervalMillis = flushIntervalMillis,
        instanceName = instanceName,
        optOut = optOut,
        minIdLength = minIdLength,
        partnerId = partnerId,
        callback = callback?.toAndroidCallback(),
        flushMaxRetries = flushMaxRetries,
        useBatch = useBatch,
        serverZone = serverZone.toAndroidServerZone(),
        serverUrl = serverUrl,
        plan = plan?.toAndroidPlan(),
        ingestionMetadata = ingestionMetadata?.toAndroidIngestionMetadata(),
        useAdvertisingIdForDeviceId = useAdvertisingIdForDeviceId,
        useAppSetIdForDeviceId = useAppSetIdForDeviceId,
        newDeviceIdPerInstall = newDeviceIdPerInstall,
        trackingOptions = trackingOptions.toAndroidTrackingOptions(),
        enableCoppaControl = enableCoppaControl,
        locationListening = locationListening,
        flushEventsOnClose = flushEventsOnClose,
        minTimeBetweenSessionsMillis = minTimeBetweenSessionsMillis,
        autocapture = autocapture.map { it.toAndroidAutocaptureOption() }.toSet(),
        identifyBatchIntervalMillis = identifyBatchIntervalMillis,
        migrateLegacyData = migrateLegacyData,
        offline = offline,
        deviceId = deviceId,
        sessionId = sessionId,
        interactionsOptions = interactionsOptions.toAndroidInteractionsOptions()
    )
}

/**
 * Map KMP ServerZone to Android ServerZone.
 */
internal fun ServerZone.toAndroidServerZone(): AndroidServerZone {
    return when (this) {
        ServerZone.US -> AndroidServerZone.US
        ServerZone.EU -> AndroidServerZone.EU
    }
}

/**
 * Map KMP AutocaptureOption to Android AutocaptureOption.
 */
internal fun AutocaptureOption.toAndroidAutocaptureOption(): AndroidAutocaptureOption {
    return when (this) {
        AutocaptureOption.SESSIONS -> AndroidAutocaptureOption.SESSIONS
        AutocaptureOption.APP_LIFECYCLES -> AndroidAutocaptureOption.APP_LIFECYCLES
        AutocaptureOption.DEEP_LINKS -> AndroidAutocaptureOption.DEEP_LINKS
        AutocaptureOption.SCREEN_VIEWS -> AndroidAutocaptureOption.SCREEN_VIEWS
        AutocaptureOption.ELEMENT_INTERACTIONS -> AndroidAutocaptureOption.ELEMENT_INTERACTIONS
        AutocaptureOption.FRUSTRATION_INTERACTIONS -> AndroidAutocaptureOption.FRUSTRATION_INTERACTIONS
    }
}

/**
 * Map KMP TrackingOptions to Android TrackingOptions.
 */
internal fun TrackingOptions.toAndroidTrackingOptions(): AndroidTrackingOptions {
    return AndroidTrackingOptions().apply {
        this@toAndroidTrackingOptions.disabledFields.forEach { field ->
            when (field) {
                "adid" -> disableAdid()
                "appSetId" -> disableAppSetId()
                "carrier" -> disableCarrier()
                "city" -> disableCity()
                "country" -> disableCountry()
                "device_brand" -> disableDeviceBrand()
                "device_manufacturer" -> disableDeviceManufacturer()
                "device_model" -> disableDeviceModel()
                "dma" -> disableDma()
                "ip_address" -> disableIpAddress()
                "language" -> disableLanguage()
                "lat_lng" -> disableLatLng()
                "os_name" -> disableOsName()
                "os_version" -> disableOsVersion()
                "platform" -> disablePlatform()
                "region" -> disableRegion()
                "version_name" -> disableVersionName()
            }
        }
    }
}

/**
 * Map KMP InteractionsOptions to Android InteractionsOptions.
 */
internal fun InteractionsOptions.toAndroidInteractionsOptions(): AndroidInteractionsOptions {
    return AndroidInteractionsOptions(
        rageClick = AndroidRageClickOptions(enabled = rageClick.enabled),
        deadClick = AndroidDeadClickOptions(enabled = deadClick.enabled)
    )
}
