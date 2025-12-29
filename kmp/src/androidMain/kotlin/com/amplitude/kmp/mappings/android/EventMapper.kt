package com.amplitude.kmp.mappings.android

import com.amplitude.core.events.BaseEvent as AndroidBaseEvent
import com.amplitude.core.events.EventOptions as AndroidEventOptions
import com.amplitude.core.events.Plan as AndroidPlan
import com.amplitude.core.events.IngestionMetadata as AndroidIngestionMetadata
import com.amplitude.core.EventCallBack as AndroidEventCallBack
import com.amplitude.kmp.events.BaseEvent
import com.amplitude.kmp.events.EventOptions
import com.amplitude.kmp.events.EventCallBack
import com.amplitude.kmp.events.Plan
import com.amplitude.kmp.events.IngestionMetadata

/**
 * Map KMP BaseEvent to Android BaseEvent.
 */
internal fun BaseEvent.toAndroidBaseEvent(): AndroidBaseEvent {
    return AndroidBaseEvent().apply {
        this.eventType = this@toAndroidBaseEvent.eventType
        this.eventProperties = this@toAndroidBaseEvent.eventProperties
        this.userProperties = this@toAndroidBaseEvent.userProperties
        this.groups = this@toAndroidBaseEvent.groups
        this.groupProperties = this@toAndroidBaseEvent.groupProperties

        // Map all EventOptions properties
        this@toAndroidBaseEvent.userId?.let { this.userId = it }
        this@toAndroidBaseEvent.deviceId?.let { this.deviceId = it }
        this@toAndroidBaseEvent.timestamp?.let { this.timestamp = it }
        this@toAndroidBaseEvent.eventId?.let { this.eventId = it }
        this@toAndroidBaseEvent.sessionId?.let { this.sessionId = it }
        this@toAndroidBaseEvent.insertId?.let { this.insertId = it }
        this@toAndroidBaseEvent.locationLat?.let { this.locationLat = it }
        this@toAndroidBaseEvent.locationLng?.let { this.locationLng = it }
        this@toAndroidBaseEvent.appVersion?.let { this.appVersion = it }
        this@toAndroidBaseEvent.versionName?.let { this.versionName = it }
        this@toAndroidBaseEvent.platform?.let { this.platform = it }
        this@toAndroidBaseEvent.osName?.let { this.osName = it }
        this@toAndroidBaseEvent.osVersion?.let { this.osVersion = it }
        this@toAndroidBaseEvent.deviceBrand?.let { this.deviceBrand = it }
        this@toAndroidBaseEvent.deviceManufacturer?.let { this.deviceManufacturer = it }
        this@toAndroidBaseEvent.deviceModel?.let { this.deviceModel = it }
        this@toAndroidBaseEvent.carrier?.let { this.carrier = it }
        this@toAndroidBaseEvent.country?.let { this.country = it }
        this@toAndroidBaseEvent.region?.let { this.region = it }
        this@toAndroidBaseEvent.city?.let { this.city = it }
        this@toAndroidBaseEvent.dma?.let { this.dma = it }
        this@toAndroidBaseEvent.idfa?.let { this.idfa = it }
        this@toAndroidBaseEvent.idfv?.let { this.idfv = it }
        this@toAndroidBaseEvent.adid?.let { this.adid = it }
        this@toAndroidBaseEvent.appSetId?.let { this.appSetId = it }
        this@toAndroidBaseEvent.androidId?.let { this.androidId = it }
        this@toAndroidBaseEvent.language?.let { this.language = it }
        this@toAndroidBaseEvent.library?.let { this.library = it }
        this@toAndroidBaseEvent.ip?.let { this.ip = it }
        this@toAndroidBaseEvent.plan?.let { this.plan = it.toAndroidPlan() }
        this@toAndroidBaseEvent.ingestionMetadata?.let { this.ingestionMetadata = it.toAndroidIngestionMetadata() }
        this@toAndroidBaseEvent.revenue?.let { this.revenue = it }
        this@toAndroidBaseEvent.price?.let { this.price = it }
        this@toAndroidBaseEvent.quantity?.let { this.quantity = it }
        this@toAndroidBaseEvent.productId?.let { this.productId = it }
        this@toAndroidBaseEvent.revenueType?.let { this.revenueType = it }
        this@toAndroidBaseEvent.currency?.let { this.currency = it }
        this@toAndroidBaseEvent.extra?.let { this.extra = it.toMutableMap() }
        this@toAndroidBaseEvent.callback?.let { this.callback = it.toAndroidCallback() }
        this@toAndroidBaseEvent.partnerId?.let { this.partnerId = it }
    }
}

/**
 * Map KMP EventOptions to Android EventOptions.
 */
internal fun EventOptions.toAndroidEventOptions(): AndroidEventOptions {
    return AndroidEventOptions().apply {
        this@toAndroidEventOptions.userId?.let { this.userId = it }
        this@toAndroidEventOptions.deviceId?.let { this.deviceId = it }
        this@toAndroidEventOptions.timestamp?.let { this.timestamp = it }
        this@toAndroidEventOptions.eventId?.let { this.eventId = it }
        this@toAndroidEventOptions.sessionId?.let { this.sessionId = it }
        this@toAndroidEventOptions.insertId?.let { this.insertId = it }
        this@toAndroidEventOptions.locationLat?.let { this.locationLat = it }
        this@toAndroidEventOptions.locationLng?.let { this.locationLng = it }
        this@toAndroidEventOptions.appVersion?.let { this.appVersion = it }
        this@toAndroidEventOptions.versionName?.let { this.versionName = it }
        this@toAndroidEventOptions.platform?.let { this.platform = it }
        this@toAndroidEventOptions.osName?.let { this.osName = it }
        this@toAndroidEventOptions.osVersion?.let { this.osVersion = it }
        this@toAndroidEventOptions.deviceBrand?.let { this.deviceBrand = it }
        this@toAndroidEventOptions.deviceManufacturer?.let { this.deviceManufacturer = it }
        this@toAndroidEventOptions.deviceModel?.let { this.deviceModel = it }
        this@toAndroidEventOptions.carrier?.let { this.carrier = it }
        this@toAndroidEventOptions.country?.let { this.country = it }
        this@toAndroidEventOptions.region?.let { this.region = it }
        this@toAndroidEventOptions.city?.let { this.city = it }
        this@toAndroidEventOptions.dma?.let { this.dma = it }
        this@toAndroidEventOptions.idfa?.let { this.idfa = it }
        this@toAndroidEventOptions.idfv?.let { this.idfv = it }
        this@toAndroidEventOptions.adid?.let { this.adid = it }
        this@toAndroidEventOptions.appSetId?.let { this.appSetId = it }
        this@toAndroidEventOptions.androidId?.let { this.androidId = it }
        this@toAndroidEventOptions.language?.let { this.language = it }
        this@toAndroidEventOptions.library?.let { this.library = it }
        this@toAndroidEventOptions.ip?.let { this.ip = it }
        this@toAndroidEventOptions.plan?.let { this.plan = it.toAndroidPlan() }
        this@toAndroidEventOptions.ingestionMetadata?.let { this.ingestionMetadata = it.toAndroidIngestionMetadata() }
        this@toAndroidEventOptions.revenue?.let { this.revenue = it }
        this@toAndroidEventOptions.price?.let { this.price = it }
        this@toAndroidEventOptions.quantity?.let { this.quantity = it }
        this@toAndroidEventOptions.productId?.let { this.productId = it }
        this@toAndroidEventOptions.revenueType?.let { this.revenueType = it }
        this@toAndroidEventOptions.currency?.let { this.currency = it }
        this@toAndroidEventOptions.extra?.let { this.extra = it.toMutableMap() }
        this@toAndroidEventOptions.callback?.let { this.callback = it.toAndroidCallback() }
        this@toAndroidEventOptions.partnerId?.let { this.partnerId = it }
    }
}

/**
 * Map KMP EventCallBack to Android EventCallBack.
 */
internal fun EventCallBack.toAndroidCallback(): AndroidEventCallBack {
    return { event, status, message ->
        // Convert Android BaseEvent back to KMP BaseEvent for callback
        val kmpEvent = BaseEvent(
            eventType = event.eventType,
            eventProperties = event.eventProperties,
            userProperties = event.userProperties,
            groups = event.groups,
            groupProperties = event.groupProperties
        ).apply {
            event.userId?.let { this.userId = it }
            event.deviceId?.let { this.deviceId = it }
            event.timestamp?.let { this.timestamp = it }
        }
        this(kmpEvent, status, message)
    }
}

/**
 * Map KMP Plan to Android Plan.
 */
internal fun Plan.toAndroidPlan(): AndroidPlan {
    return AndroidPlan(
        branch = branch,
        source = source,
        version = version,
        versionId = versionId
    )
}

/**
 * Map KMP IngestionMetadata to Android IngestionMetadata.
 */
internal fun IngestionMetadata.toAndroidIngestionMetadata(): AndroidIngestionMetadata {
    return AndroidIngestionMetadata(
        sourceName = sourceName,
        sourceVersion = sourceVersion
    )
}
