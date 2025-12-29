package com.amplitude.kmp.mappings.ios

import cocoapods.AmplitudeSwift.*
import com.amplitude.kmp.events.*
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*

/**
 * iOS mapping utilities for Events.
 *
 * Maps KMP event classes to iOS SDK event objects using CocoaPods cinterop.
 */

/**
 * Map KMP BaseEvent to iOS BaseEvent.
 * Note: CocoaPods cinterop exposes properties as functions, so we use setter methods.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun BaseEvent.toIOSBaseEvent(): AMPBaseEvent {
    // Convert MutableMap<String, Any?> to Map<Any?, Any?> for iOS
    val iosEventProperties: Map<Any?, Any?>? = eventProperties?.toMap()

    // DEBUG: Log what we're creating
    println("🔍 toIOSBaseEvent - eventType: $eventType")
    println("🔍 toIOSBaseEvent - userId: $userId")
    println("🔍 toIOSBaseEvent - deviceId: $deviceId")
    println("🔍 toIOSBaseEvent - eventProperties: $eventProperties")

    val event = AMPBaseEvent(
        eventType = eventType,
        eventProperties = iosEventProperties
    )

    // Set EventOptions properties using setters (cinterop requires setter methods)
    userId?.let {
        println("🔍 Setting userId: $it")
        event.setUserId(it)
        println("🔍 VERIFY after setter - event.userId(): ${event.userId()}")
    } ?: println("⚠️ userId is null, not setting")

    deviceId?.let {
        println("🔍 Setting deviceId: $it")
        event.setDeviceId(it)
        println("🔍 VERIFY after setter - event.deviceId(): ${event.deviceId()}")
    } ?: println("⚠️ deviceId is null, not setting")
    timestamp?.let { event.setTimestamp(it) }
    eventId?.let { event.setEventId(it) }
    sessionId?.let { event.setSessionId(it) }
    insertId?.let { event.setInsertId(it) }
    locationLat?.let { event.setLocationLat(it) }
    locationLng?.let { event.setLocationLng(it) }
    appVersion?.let { event.setAppVersion(it) }
    versionName?.let { event.setVersionName(it) }
    platform?.let { event.setPlatform(it) }
    osName?.let { event.setOsName(it) }
    osVersion?.let { event.setOsVersion(it) }
    deviceBrand?.let { event.setDeviceBrand(it) }
    deviceManufacturer?.let { event.setDeviceManufacturer(it) }
    deviceModel?.let { event.setDeviceModel(it) }
    carrier?.let { event.setCarrier(it) }
    country?.let { event.setCountry(it) }
    region?.let { event.setRegion(it) }
    city?.let { event.setCity(it) }
    dma?.let { event.setDma(it) }
    idfa?.let { event.setIdfa(it) }
    idfv?.let { event.setIdfv(it) }
    adid?.let { event.setAdid(it) }
    language?.let { event.setLanguage(it) }
    library?.let { event.setLibrary(it) }
    ip?.let { event.setIp(it) }
    plan?.let { event.setPlan(it.toIOSPlan()) }
    ingestionMetadata?.let { event.setIngestionMetadata(it.toIOSIngestionMetadata()) }
    revenue?.let { event.setRevenue(it) }
    price?.let { event.setPrice(it) }
    quantity?.let { event.setQuantity(it.toLong()) }
    productId?.let { event.setProductId(it) }
    revenueType?.let { event.setRevenueType(it) }
    currency?.let { event.setCurrency(it) }
    // extra, callback, and partnerId may not have setters - skip for now
    callback?.let { event.setCallback(it.toIOSCallback()) }
    partnerId?.let { event.setPartnerId(it) }

    // BaseEvent properties (userProperties, groups, groupProperties) don't have setters
    // They are set via the constructor or are read-only
    // Skip setting them here

    return event
}

/**
 * Map KMP Identify to iOS Identify.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun Identify.toIOSIdentify(): AMPIdentify {
    val identify = AMPIdentify()

    // Apply all operations from the KMP Identify
    getOperations().forEach { (operation, properties) ->
        properties.forEach { (property, value) ->
            when (operation) {
                "\$set" -> identify.set(property, value?.toNSObject())
                "\$setOnce" -> identify.setOnce(property, value?.toNSObject())
                "\$add" -> {
                    // add() requires specific number types
                    when (value) {
                        is Int -> identify.add(property, valueInt64 = value.toLong())
                        is Long -> identify.add(property, valueInt64 = value)
                        is Float -> identify.add(property, valueFloat = value)
                        is Double -> identify.add(property, valueDouble = value)
                        else -> {} // Skip non-numeric values
                    }
                }

                "\$append" -> identify.append(property, value?.toNSObject())
                "\$prepend" -> identify.prepend(property, value?.toNSObject())
                "\$unset" -> identify.unset(property)
                "\$remove" -> identify.remove(property, value?.toNSObject())
                "\$preInsert" -> identify.preInsert(property, value?.toNSObject())
                "\$postInsert" -> identify.postInsert(property, value?.toNSObject())
                "\$clearAll" -> if (property == "-") identify.clearAll()
            }
        }
    }

    return identify
}

/**
 * Map KMP Revenue to iOS Revenue.
 * Note: iOS Revenue has custom setters with validation.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun Revenue.toIOSRevenue(): AMPRevenue {
    val revenue = AMPRevenue()

    // Set properties using setter methods
    productId?.let { revenue.setProductId(it) }
    revenue.setQuantity(quantity.toLong())
    price?.let { revenue.setPrice(it) }
    revenueType?.let { revenue.setRevenueType(it) }
    currency?.let { revenue.setCurrency(it) }
    this.revenue?.let { revenue.setRevenue(it) }
    receipt?.let { revenue.setReceipt(it) }

    // properties field doesn't have a setter - skip for now
    // TODO: Find the correct way to set properties on iOS Revenue

    return revenue
}

/**
 * Map KMP RevenueEvent to iOS BaseEvent.
 * Note: RevenueEvent is a BaseEvent, so we can use toIOSBaseEvent()
 */
@OptIn(ExperimentalForeignApi::class)
internal fun RevenueEvent.toIOSRevenueEvent(): AMPBaseEvent {
    // Just use the standard BaseEvent mapping since RevenueEvent extends BaseEvent
    return toIOSBaseEvent()
}

/**
 * Map KMP EventOptions to iOS EventOptions.
 * Note: CocoaPods cinterop exposes properties as functions, so we use setter methods.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun EventOptions.toIOSEventOptions(): AMPEventOptions {
    val options = AMPEventOptions()

    userId?.let { options.setUserId(it) }
    deviceId?.let { options.setDeviceId(it) }
    timestamp?.let { options.setTimestamp(it) }
    eventId?.let { options.setEventId(it) }
    sessionId?.let { options.setSessionId(it) }
    insertId?.let { options.setInsertId(it) }
    locationLat?.let { options.setLocationLat(it) }
    locationLng?.let { options.setLocationLng(it) }
    appVersion?.let { options.setAppVersion(it) }
    versionName?.let { options.setVersionName(it) }
    platform?.let { options.setPlatform(it) }
    osName?.let { options.setOsName(it) }
    osVersion?.let { options.setOsVersion(it) }
    deviceBrand?.let { options.setDeviceBrand(it) }
    deviceManufacturer?.let { options.setDeviceManufacturer(it) }
    deviceModel?.let { options.setDeviceModel(it) }
    carrier?.let { options.setCarrier(it) }
    country?.let { options.setCountry(it) }
    region?.let { options.setRegion(it) }
    city?.let { options.setCity(it) }
    dma?.let { options.setDma(it) }
    idfa?.let { options.setIdfa(it) }
    idfv?.let { options.setIdfv(it) }
    adid?.let { options.setAdid(it) }
    language?.let { options.setLanguage(it) }
    library?.let { options.setLibrary(it) }
    ip?.let { options.setIp(it) }
    plan?.let { options.setPlan(it.toIOSPlan()) }
    ingestionMetadata?.let { options.setIngestionMetadata(it.toIOSIngestionMetadata()) }
    revenue?.let { options.setRevenue(it) }
    price?.let { options.setPrice(it) }
    quantity?.let { options.setQuantity(it.toLong()) }
    productId?.let { options.setProductId(it) }
    revenueType?.let { options.setRevenueType(it) }
    currency?.let { options.setCurrency(it) }
    // extra and callback may not have setters - skip for now
    callback?.let { options.setCallback(it.toIOSCallback()) }
    partnerId?.let { options.setPartnerId(it) }

    return options
}

/**
 * Map KMP Plan to iOS AMPPlan.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun Plan.toIOSPlan(): AMPPlan = AMPPlan().apply {
    // Use apply scope to set properties on the iOS object
    this@toIOSPlan.branch?.let { setBranch(it) }
    this@toIOSPlan.source?.let { setSource(it) }
    this@toIOSPlan.version?.let { setVersion(it) }
    this@toIOSPlan.versionId?.let { setVersionId(it) }
}

/**
 * Map KMP IngestionMetadata to iOS AMPIngestionMetadata.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun IngestionMetadata.toIOSIngestionMetadata(): AMPIngestionMetadata = AMPIngestionMetadata().apply {
    // Use apply scope to set properties on the iOS object
    this@toIOSIngestionMetadata.sourceName?.let { setSourceName(it) }
    this@toIOSIngestionMetadata.sourceVersion?.let { setSourceVersion(it) }
}

/**
 * Map KMP EventCallBack to iOS EventCallback.
 * Note: iOS EventCallback is a function type exposed by cinterop.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun EventCallBack.toIOSCallback(): ((AMPBaseEvent?, Long, String?) -> Unit)? {
    // iOS EventCallback is a closure: (BaseEvent?, Int, String?) -> Void
    return { iosEvent: AMPBaseEvent?, status: Long, message: String? ->
        // Create a dummy BaseEvent since we can't easily convert iOS event back to KMP
        // The callback primarily cares about status and message
        val dummyEvent = BaseEvent(eventType = "callback_event")
        this(dummyEvent, status.toInt(), message ?: "")
    }
}

/**
 * Map KMP Plugin to iOS Plugin.
 * Note: Plugin mapping is implemented in PluginMapper.kt
 * This is just a forward reference for convenience.
 */

// ========================================
// Type Conversion Helpers
// ========================================

/**
 * Convert Kotlin Map to NSDictionary.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun Map<String, Any?>.toNSDictionary(): NSDictionary {
    val dict = NSMutableDictionary()
    forEach { (key, value) ->
        dict.setObject(value?.toNSObject() ?: NSNull(), forKey = key as NSString)
    }
    return dict
}

/**
 * Safely convert Map<*, *> to NSDictionary.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun Map<*, *>.toNSDictionarySafe(): NSDictionary {
    val dict = NSMutableDictionary()
    forEach { (key, value) ->
        val nsKey = key?.toString() ?: "null"
        dict.setObject(value?.toNSObject() ?: NSNull(), forKey = nsKey as NSString)
    }
    return dict
}

/**
 * Convert Kotlin Array to NSArray.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun Array<String>.toNSArray(): NSArray {
    val array = NSMutableArray()
    forEach { array.addObject(it) }
    return array
}

/**
 * Convert Any? to NSObject for iOS.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun Any?.toNSObject(): Any? = when (this) {
    null -> NSNull()
    is String -> this
    is Number -> this.toNSNumber()
    is Boolean -> NSNumber(bool = this)
    is Map<*, *> -> this.toNSDictionarySafe()
    is List<*> -> this.toNSArray()
    else -> this.toString()
}

/**
 * Convert Number to NSNumber.
 */
@OptIn(ExperimentalForeignApi::class)
private fun Number.toNSNumber(): NSNumber = when (this) {
    is Int -> NSNumber(int = this)
    is Long -> NSNumber(longLong = this)
    is Float -> NSNumber(float = this)
    is Double -> NSNumber(double = this)
    is Byte -> NSNumber(char = this)
    is Short -> NSNumber(short = this)
    else -> NSNumber(double = this.toDouble())
}

/**
 * Convert List to NSArray.
 */
@OptIn(ExperimentalForeignApi::class)
private fun List<*>.toNSArray(): NSArray {
    val array = NSMutableArray()
    forEach { array.addObject(it?.toNSObject() ?: NSNull()) }
    return array
}
