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
            // Create a minimal KMP BaseEvent from the iOS event type
            // The iOS SDK has already enriched the event with all contextual data
            val kmpEvent = BaseEvent(
                eventType = iosObjCEvent.eventType()
            )

            // Execute KMP plugin - it can modify or drop the event
            val result = kmpPlugin.execute(kmpEvent)

            // Convert result back to iOS ObjCBaseEvent (AMPBaseEvent)
            // If plugin returns null, the event is dropped
            // Otherwise, return a new AMPBaseEvent with the potentially modified eventType
            result?.let {
                AMPBaseEvent.initWithEventType(eventType = it.eventType)
            }
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
