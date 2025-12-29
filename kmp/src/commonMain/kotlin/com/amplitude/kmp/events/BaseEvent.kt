package com.amplitude.kmp.events

/**
 * Base event class for all Amplitude events.
 *
 * Extends EventOptions to inherit all configurable properties.
 * Maintains API compatibility with Android SDK's BaseEvent class.
 */
public open class BaseEvent(
    /**
     * Type of the event (required)
     */
    public var eventType: String = "",

    /**
     * Event-specific properties
     */
    public var eventProperties: MutableMap<String, Any?>? = null,

    /**
     * User properties to update with this event
     */
    public var userProperties: MutableMap<String, Any?>? = null,

    /**
     * Group assignments for this event
     */
    public var groups: MutableMap<String, Any?>? = null,

    /**
     * Group properties to update with this event
     */
    public var groupProperties: MutableMap<String, Any?>? = null,
) : EventOptions() {

    /**
     * Merge EventOptions into this event
     */
    public fun mergeEventOptions(options: EventOptions) {
        options.userId?.let { this.userId = it }
        options.deviceId?.let { this.deviceId = it }
        options.timestamp?.let { this.timestamp = it }
        options.eventId?.let { this.eventId = it }
        options.sessionId?.let { this.sessionId = it }
        options.insertId?.let { this.insertId = it }
        options.locationLat?.let { this.locationLat = it }
        options.locationLng?.let { this.locationLng = it }
        options.appVersion?.let { this.appVersion = it }
        options.versionName?.let { this.versionName = it }
        options.platform?.let { this.platform = it }
        options.osName?.let { this.osName = it }
        options.osVersion?.let { this.osVersion = it }
        options.deviceBrand?.let { this.deviceBrand = it }
        options.deviceManufacturer?.let { this.deviceManufacturer = it }
        options.deviceModel?.let { this.deviceModel = it }
        options.carrier?.let { this.carrier = it }
        options.country?.let { this.country = it }
        options.region?.let { this.region = it }
        options.city?.let { this.city = it }
        options.dma?.let { this.dma = it }
        options.idfa?.let { this.idfa = it }
        options.idfv?.let { this.idfv = it }
        options.adid?.let { this.adid = it }
        options.appSetId?.let { this.appSetId = it }
        options.androidId?.let { this.androidId = it }
        options.language?.let { this.language = it }
        options.library?.let { this.library = it }
        options.ip?.let { this.ip = it }
        options.plan?.let { this.plan = it }
        options.ingestionMetadata?.let { this.ingestionMetadata = it }
        options.revenue?.let { this.revenue = it }
        options.price?.let { this.price = it }
        options.quantity?.let { this.quantity = it }
        options.productId?.let { this.productId = it }
        options.revenueType?.let { this.revenueType = it }
        options.currency?.let { this.currency = it }
        options.extra?.let { this.extra = it }
        options.callback?.let { this.callback = it }
        options.partnerId?.let { this.partnerId = it }
    }

    /**
     * Validate the event
     * @return true if event has userId or deviceId
     */
    public open fun isValid(): Boolean {
        return !userId.isNullOrBlank() || !deviceId.isNullOrBlank()
    }
}

/**
 * Constants for special event types
 */
public object AmplitudeEventConstants {
    /**
     * Identify event type
     */
    public const val IDENTIFY_EVENT: String = "\$identify"

    /**
     * Group identify event type
     */
    public const val GROUP_IDENTIFY_EVENT: String = "\$groupidentify"

    /**
     * Revenue event type
     */
    public const val REVENUE_EVENT: String = "revenue_amount"

    /**
     * Session start event type (Android SDK constant)
     */
    public const val START_SESSION_EVENT: String = "session_start"

    /**
     * Session end event type (Android SDK constant)
     */
    public const val END_SESSION_EVENT: String = "session_end"
}
