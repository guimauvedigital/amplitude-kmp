package com.amplitude.kmp.mappings.android

import com.amplitude.core.platform.Plugin as AndroidPlugin
import com.amplitude.core.events.BaseEvent as AndroidBaseEvent
import com.amplitude.kmp.plugins.Plugin
import com.amplitude.kmp.Amplitude

/**
 * Wrapper to adapt KMP Plugin to Android SDK Plugin.
 */
internal class KmpPluginAdapter(private val kmpPlugin: Plugin) : AndroidPlugin {
    override lateinit var amplitude: com.amplitude.core.Amplitude

    override val type: AndroidPlugin.Type
        get() = kmpPlugin.type.toAndroidPluginType()

    override fun setup(amplitude: com.amplitude.core.Amplitude) {
        this.amplitude = amplitude
        // Convert Android Amplitude to KMP Amplitude for setup
        // Note: This requires access to the KMP Amplitude wrapper instance
        // For now, we'll skip the setup conversion as it's rarely used
    }

    override fun execute(event: AndroidBaseEvent): AndroidBaseEvent? {
        // Convert Android event to KMP event
        val kmpEvent = com.amplitude.kmp.events.BaseEvent(
            eventType = event.eventType,
            eventProperties = event.eventProperties,
            userProperties = event.userProperties,
            groups = event.groups,
            groupProperties = event.groupProperties
        ).apply {
            // Copy all EventOptions properties from Android event to KMP event
            event.userId?.let { this.userId = it }
            event.deviceId?.let { this.deviceId = it }
            event.timestamp?.let { this.timestamp = it }
            event.eventId?.let { this.eventId = it }
            event.sessionId?.let { this.sessionId = it }
            event.insertId?.let { this.insertId = it }
            event.locationLat?.let { this.locationLat = it }
            event.locationLng?.let { this.locationLng = it }
            event.appVersion?.let { this.appVersion = it }
            event.versionName?.let { this.versionName = it }
            event.platform?.let { this.platform = it }
            event.osName?.let { this.osName = it }
            event.osVersion?.let { this.osVersion = it }
            event.deviceBrand?.let { this.deviceBrand = it }
            event.deviceManufacturer?.let { this.deviceManufacturer = it }
            event.deviceModel?.let { this.deviceModel = it }
            event.carrier?.let { this.carrier = it }
            event.country?.let { this.country = it }
            event.region?.let { this.region = it }
            event.city?.let { this.city = it }
            event.dma?.let { this.dma = it }
            event.idfa?.let { this.idfa = it }
            event.idfv?.let { this.idfv = it }
            event.adid?.let { this.adid = it }
            event.appSetId?.let { this.appSetId = it }
            event.androidId?.let { this.androidId = it }
            event.language?.let { this.language = it }
            event.library?.let { this.library = it }
            event.ip?.let { this.ip = it }
            event.revenue?.let { this.revenue = it }
            event.price?.let { this.price = it }
            event.quantity?.let { this.quantity = it }
            event.productId?.let { this.productId = it }
            event.revenueType?.let { this.revenueType = it }
            event.currency?.let { this.currency = it }
            event.partnerId?.let { this.partnerId = it }
        }

        // Execute KMP plugin
        val result = kmpPlugin.execute(kmpEvent)

        // Convert result back to Android event
        return result?.toAndroidBaseEvent()
    }
}

/**
 * Map KMP Plugin to Android Plugin.
 */
internal fun Plugin.toAndroidPlugin(): AndroidPlugin {
    return KmpPluginAdapter(this)
}

/**
 * Map KMP Plugin.Type to Android Plugin.Type.
 */
internal fun Plugin.Type.toAndroidPluginType(): AndroidPlugin.Type {
    return when (this) {
        Plugin.Type.BEFORE -> AndroidPlugin.Type.Before
        Plugin.Type.ENRICHMENT -> AndroidPlugin.Type.Enrichment
        Plugin.Type.DESTINATION -> AndroidPlugin.Type.Destination
        Plugin.Type.OBSERVE -> AndroidPlugin.Type.Observe
        Plugin.Type.UTILITY -> AndroidPlugin.Type.Utility
    }
}
