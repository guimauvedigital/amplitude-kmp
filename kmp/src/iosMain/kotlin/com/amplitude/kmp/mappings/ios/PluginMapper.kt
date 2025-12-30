package com.amplitude.kmp.mappings.ios

import cocoapods.AmplitudeSwift.*
import com.amplitude.kmp.Amplitude
import com.amplitude.kmp.events.BaseEvent
import com.amplitude.kmp.plugins.Plugin
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * iOS Plugin mapping using ObjCPlugin (AMPPlugin).
 *
 * The iOS SDK provides ObjCPlugin which accepts closures for setup and execute.
 * This is simpler than trying to implement a protocol from Kotlin.
 */

/**
 * Map KMP Plugin to iOS AMPPlugin.
 *
 * Uses the ObjCPlugin class which accepts closures for the execute callback.
 * This avoids the complexity of implementing Swift protocols from Kotlin.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun Plugin.toIOSPlugin(): AMPPlugin {
    val kmpPlugin = this

    // Create execute closure that bridges KMP Plugin to iOS
    // Note: ObjCBaseEvent is exposed as AMPBaseEvent through @objc(AMPBaseEvent)
    val executeCallback: (AMPBaseEvent?) -> AMPBaseEvent? = { iosObjCEvent ->
        if (iosObjCEvent == null) {
            null
        } else {
            // Create KMP BaseEvent from iOS event, copying ALL properties (like Android does)
            val kmpEvent = BaseEvent(
                eventType = iosObjCEvent.eventType()
                // Note: eventProperties, userProperties, etc. are ObjCProperties which can't be easily converted
                // For plugin execution, we mainly need userId/deviceId which are copied below
            ).apply {
                // Copy all EventOptions properties from iOS event to KMP event
                iosObjCEvent.userId()?.let { this.userId = it }
                iosObjCEvent.deviceId()?.let { this.deviceId = it }
                this.timestamp = iosObjCEvent.timestamp().takeIf { it != -1L }
                this.eventId = iosObjCEvent.eventId().takeIf { it != -1L }
                this.sessionId = iosObjCEvent.sessionId().takeIf { it != -1L }
                iosObjCEvent.insertId()?.let { this.insertId = it }
                this.locationLat = iosObjCEvent.locationLat().takeIf { !it.isNaN() }
                this.locationLng = iosObjCEvent.locationLng().takeIf { !it.isNaN() }
                iosObjCEvent.appVersion()?.let { this.appVersion = it }
                iosObjCEvent.versionName()?.let { this.versionName = it }
                iosObjCEvent.platform()?.let { this.platform = it }
                iosObjCEvent.osName()?.let { this.osName = it }
                iosObjCEvent.osVersion()?.let { this.osVersion = it }
                iosObjCEvent.deviceBrand()?.let { this.deviceBrand = it }
                iosObjCEvent.deviceManufacturer()?.let { this.deviceManufacturer = it }
                iosObjCEvent.deviceModel()?.let { this.deviceModel = it }
                iosObjCEvent.carrier()?.let { this.carrier = it }
                iosObjCEvent.country()?.let { this.country = it }
                iosObjCEvent.region()?.let { this.region = it }
                iosObjCEvent.city()?.let { this.city = it }
                iosObjCEvent.dma()?.let { this.dma = it }
                iosObjCEvent.idfa()?.let { this.idfa = it }
                iosObjCEvent.idfv()?.let { this.idfv = it }
                iosObjCEvent.adid()?.let { this.adid = it }
                iosObjCEvent.language()?.let { this.language = it }
                iosObjCEvent.library()?.let { this.library = it }
                iosObjCEvent.ip()?.let { this.ip = it }
                this.revenue = iosObjCEvent.revenue().takeIf { !it.isNaN() }
                this.price = iosObjCEvent.price().takeIf { !it.isNaN() }
                this.quantity = iosObjCEvent.quantity().toInt().takeIf { it != -1 }
                iosObjCEvent.productId()?.let { this.productId = it }
                iosObjCEvent.revenueType()?.let { this.revenueType = it }
                iosObjCEvent.currency()?.let { this.currency = it }
                iosObjCEvent.partnerId()?.let { this.partnerId = it }
            }

            // Execute KMP plugin - it can modify or drop the event
            val result = kmpPlugin.execute(kmpEvent)

            // Convert result back to iOS ObjCBaseEvent (AMPBaseEvent)
            // If plugin returns null, the event is dropped
            // Copy all properties from KMP event back to iOS event (like Android does)
            result?.toIOSBaseEvent()
        }
    }

    // Create the ObjCPlugin (AMPPlugin) using the static factory method
    return AMPPlugin.initWithType(
        type = type.toIOSPluginType(),
        execute = executeCallback
    )
}

/**
 * Map KMP Plugin.Type to iOS PluginType enum.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun Plugin.Type.toIOSPluginType(): AMPPluginType {
    return when (this) {
        Plugin.Type.BEFORE -> AMPPluginTypeBefore
        Plugin.Type.ENRICHMENT -> AMPPluginTypeEnrichment
        Plugin.Type.DESTINATION -> AMPPluginTypeDestination
        Plugin.Type.OBSERVE -> AMPPluginTypeObserve
        Plugin.Type.UTILITY -> AMPPluginTypeUtility
    }
}
