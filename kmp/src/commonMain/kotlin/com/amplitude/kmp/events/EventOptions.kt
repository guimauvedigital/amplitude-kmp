package com.amplitude.kmp.events

/**
 * Event options that can be set per event.
 *
 * Maintains API compatibility with Android SDK's EventOptions class.
 * Contains 45+ configurable properties for event customization.
 */
public open class EventOptions {
    // Identity Properties
    /**
     * User ID for this event
     */
    public var userId: String? = null

    /**
     * Device ID for this event
     */
    public var deviceId: String? = null

    // Timing Properties
    /**
     * Timestamp for this event (milliseconds since epoch)
     */
    public var timestamp: Long? = null

    /**
     * Event ID (auto-generated if not provided)
     */
    public var eventId: Long? = null

    /**
     * Session ID for this event
     */
    public var sessionId: Long? = null

    /**
     * Insert ID for event de-duplication
     */
    public var insertId: String? = null

    // Location Properties
    /**
     * Latitude coordinate
     */
    public var locationLat: Double? = null

    /**
     * Longitude coordinate
     */
    public var locationLng: Double? = null

    // App Properties
    /**
     * App version (e.g., "1.2.3")
     */
    public var appVersion: String? = null

    /**
     * Version name (human-readable version string)
     */
    public var versionName: String? = null

    /**
     * Platform identifier (e.g., "Android", "iOS")
     */
    public var platform: String? = null

    // OS Properties
    /**
     * Operating system name (e.g., "Android", "iOS")
     */
    public var osName: String? = null

    /**
     * Operating system version (e.g., "13.0")
     */
    public var osVersion: String? = null

    // Device Properties
    /**
     * Device brand (e.g., "Apple", "Samsung")
     */
    public var deviceBrand: String? = null

    /**
     * Device manufacturer (e.g., "Apple Inc.")
     */
    public var deviceManufacturer: String? = null

    /**
     * Device model (e.g., "iPhone 14 Pro")
     */
    public var deviceModel: String? = null

    /**
     * Mobile carrier name
     */
    public var carrier: String? = null

    // Geographic Properties
    /**
     * Country code (e.g., "US")
     */
    public var country: String? = null

    /**
     * Region/state code
     */
    public var region: String? = null

    /**
     * City name
     */
    public var city: String? = null

    /**
     * Designated Market Area code
     */
    public var dma: String? = null

    // Identifier Properties
    /**
     * iOS Identifier for Advertisers (IDFA)
     */
    public var idfa: String? = null

    /**
     * iOS Identifier for Vendors (IDFV)
     */
    public var idfv: String? = null

    /**
     * Android Advertising ID (ADID)
     */
    public var adid: String? = null

    /**
     * Android App Set ID
     */
    public var appSetId: String? = null

    /**
     * Android ID
     */
    public var androidId: String? = null

    // Other Properties
    /**
     * Language code (e.g., "en-US")
     */
    public var language: String? = null

    /**
     * Library identifier (SDK name/version)
     */
    public var library: String? = null

    /**
     * IP address of the user
     */
    public var ip: String? = null

    // Metadata Properties
    /**
     * Tracking plan configuration
     */
    public var plan: Plan? = null

    /**
     * Ingestion metadata
     */
    public var ingestionMetadata: IngestionMetadata? = null

    // Revenue Properties (for revenue events)
    /**
     * Revenue amount
     */
    public var revenue: Double? = null

    /**
     * Price of the product
     */
    public var price: Double? = null

    /**
     * Quantity of items
     */
    public var quantity: Int? = null

    /**
     * Product identifier
     */
    public var productId: String? = null

    /**
     * Revenue type (e.g., "purchase", "subscription")
     */
    public var revenueType: String? = null

    /**
     * Currency code (e.g., "USD")
     */
    public var currency: String? = null

    // Additional Properties
    /**
     * Extra properties for event-specific data
     */
    public var extra: Map<String, Any>? = null

    /**
     * Callback for this specific event
     */
    public var callback: EventCallBack? = null

    /**
     * Partner ID for attribution
     */
    public var partnerId: String? = null

    /**
     * Internal property for retry attempts (not user-configurable)
     */
    internal var attempts: Int = 0
}
