package com.amplitude.kmp

import com.amplitude.kmp.events.EventCallBack
import com.amplitude.kmp.events.IngestionMetadata
import com.amplitude.kmp.events.Plan

/**
 * Configuration class for Amplitude SDK initialization.
 *
 * Maintains API compatibility with Android SDK's Configuration.
 * Android-specific parameters are platform-agnostic here and handled in platform implementations.
 */
public data class Configuration(
    /**
     * Amplitude API key (required)
     */
    public val apiKey: String,

    /**
     * Platform-specific context (android.content.Context on Android, ignored on iOS)
     */
    public var androidContext: Any? = null,

    // Core Configuration Options
    /**
     * Number of events to batch before flushing (default: 30)
     */
    public var flushQueueSize: Int = FLUSH_QUEUE_SIZE,

    /**
     * Auto-flush interval in milliseconds (default: 30000 = 30 seconds)
     */
    public var flushIntervalMillis: Int = FLUSH_INTERVAL_MILLIS,

    /**
     * Instance name for multiple SDK instances (default: "\$default_instance")
     */
    public var instanceName: String = DEFAULT_INSTANCE,

    /**
     * Opt out of tracking (default: false)
     */
    public var optOut: Boolean = false,

    /**
     * Logging level (default: WARN)
     */
    public var logLevel: LogLevel = LogLevel.WARN,

    /**
     * Server zone: US or EU (default: US)
     */
    public var serverZone: ServerZone = ServerZone.US,

    /**
     * Custom server URL (overrides serverZone if provided)
     */
    public var serverUrl: String? = null,

    /**
     * Use batch API endpoint (default: false)
     */
    public var useBatch: Boolean = false,

    /**
     * Minimum length for user ID and device ID
     */
    public var minIdLength: Int? = null,

    /**
     * Partner ID for attribution
     */
    public var partnerId: String? = null,

    /**
     * Global callback for all events
     */
    public var callback: EventCallBack? = null,

    /**
     * Maximum number of flush retries on failure (default: 5)
     */
    public var flushMaxRetries: Int = FLUSH_MAX_RETRIES,

    /**
     * Minimum time between sessions in milliseconds (default: 300000 = 5 minutes)
     */
    public var minTimeBetweenSessionsMillis: Long = MIN_TIME_BETWEEN_SESSIONS_MILLIS,

    /**
     * Track session events automatically (default: true)
     */
    public var trackingSessionEvents: Boolean = true,

    /**
     * Default tracking options for auto-capture
     */
    public var defaultTracking: DefaultTrackingOptions = DefaultTrackingOptions(),

    /**
     * Identify batch interval in milliseconds (default: 30000 = 30 seconds)
     */
    public var identifyBatchIntervalMillis: Long = IDENTIFY_BATCH_INTERVAL_MILLIS,

    /**
     * Flush events when SDK is closed (default: true)
     */
    public var flushEventsOnClose: Boolean = true,

    /**
     * Offline mode - prevent events from being sent (default: false)
     */
    public var offline: Boolean = false,

    /**
     * Custom device ID (overrides SDK-generated ID)
     */
    public var deviceId: String? = null,

    /**
     * Custom session ID (overrides SDK-generated ID)
     */
    public var sessionId: Long? = null,

    /**
     * Tracking plan configuration
     */
    public var plan: Plan? = null,

    /**
     * Ingestion metadata for event enrichment
     */
    public var ingestionMetadata: IngestionMetadata? = null,

    // Android-Specific Options (ignored on iOS)
    /**
     * Use Android Advertising ID for device ID (Android only, default: false)
     */
    public var useAdvertisingIdForDeviceId: Boolean = false,

    /**
     * Use Android App Set ID for device ID (Android only, default: false)
     */
    public var useAppSetIdForDeviceId: Boolean = false,

    /**
     * Generate new device ID per install (Android only, default: false)
     */
    public var newDeviceIdPerInstall: Boolean = false,

    /**
     * Listen for device location changes (Android only, default: false)
     */
    public var locationListening: Boolean = false,

    /**
     * Migrate legacy data from previous SDK versions (Android only, default: true)
     */
    public var migrateLegacyData: Boolean = true,

    /**
     * Autocapture options (platform-specific implementation)
     */
    public var autocapture: Set<AutocaptureOption> = setOf(AutocaptureOption.SESSIONS),

    /**
     * Tracking options for opt-out of specific fields
     */
    public var trackingOptions: TrackingOptions = TrackingOptions(),

    /**
     * Enable COPPA (Children's Online Privacy Protection Act) control (Android only, default: false)
     */
    public var enableCoppaControl: Boolean = false,

    /**
     * Interaction tracking options (Android only)
     */
    public var interactionsOptions: InteractionsOptions = InteractionsOptions(),
) {
    public companion object {
        public const val FLUSH_QUEUE_SIZE: Int = 30
        public const val FLUSH_INTERVAL_MILLIS: Int = 30 * 1000  // 30 seconds
        public const val FLUSH_MAX_RETRIES: Int = 5
        public const val DEFAULT_INSTANCE: String = "\$default_instance"
        public const val IDENTIFY_BATCH_INTERVAL_MILLIS: Long = 30 * 1000L  // 30 seconds
        public const val MIN_TIME_BETWEEN_SESSIONS_MILLIS: Long = 5 * 60 * 1000L  // 5 minutes
    }
}

/**
 * Server zone for Amplitude API
 */
public enum class ServerZone {
    /**
     * United States data center
     */
    US,

    /**
     * European Union data center
     */
    EU
}

/**
 * Logging level for SDK
 */
public enum class LogLevel {
    /**
     * No logging
     */
    NONE,

    /**
     * Error logs only
     */
    ERROR,

    /**
     * Warning and error logs
     */
    WARN,

    /**
     * Info, warning, and error logs
     */
    LOG,

    /**
     * Debug and all other logs
     */
    DEBUG,

    /**
     * All logs including verbose
     */
    VERBOSE
}

/**
 * Autocapture options for automatic event tracking
 */
public enum class AutocaptureOption {
    /**
     * Track session start/end events
     */
    SESSIONS,

    /**
     * Track app lifecycle events (Android: foreground/background, iOS: similar)
     */
    APP_LIFECYCLES,

    /**
     * Track deep link opens
     */
    DEEP_LINKS,

    /**
     * Track screen views
     */
    SCREEN_VIEWS,

    /**
     * Track UI element interactions (clicks, taps)
     */
    ELEMENT_INTERACTIONS,

    /**
     * Track frustration interactions (rage clicks, dead clicks)
     */
    FRUSTRATION_INTERACTIONS;

    public companion object {
        /**
         * All autocapture options
         */
        public val ALL: Set<AutocaptureOption> = setOf(
            SESSIONS,
            APP_LIFECYCLES,
            DEEP_LINKS,
            SCREEN_VIEWS,
            ELEMENT_INTERACTIONS,
            FRUSTRATION_INTERACTIONS
        )
    }
}

/**
 * Default tracking options for auto-capture features
 */
public data class DefaultTrackingOptions(
    /**
     * Enable session tracking
     */
    public var sessions: Boolean = true,

    /**
     * Enable app lifecycle tracking
     */
    public var appLifecycles: Boolean = false,

    /**
     * Enable deep link tracking
     */
    public var deepLinks: Boolean = false,

    /**
     * Enable screen view tracking
     */
    public var screenViews: Boolean = false,

    /**
     * Enable element interaction tracking
     */
    public var elementInteractions: Boolean = false,

    /**
     * Enable frustration interaction tracking
     */
    public var frustrationInteractions: Boolean = false,
)

/**
 * Tracking options for opting out of specific fields
 */
public data class TrackingOptions(
    /**
     * Fields to disable tracking for
     */
    public var disabledFields: MutableSet<String> = mutableSetOf()
) {
    // Disable/enable methods for each field
    public fun disableAdid(): TrackingOptions = apply { disabledFields.add("adid") }
    public fun shouldTrackAdid(): Boolean = !disabledFields.contains("adid")

    public fun disableAppSetId(): TrackingOptions = apply { disabledFields.add("appSetId") }
    public fun shouldTrackAppSetId(): Boolean = !disabledFields.contains("appSetId")

    public fun disableCarrier(): TrackingOptions = apply { disabledFields.add("carrier") }
    public fun shouldTrackCarrier(): Boolean = !disabledFields.contains("carrier")

    public fun disableCity(): TrackingOptions = apply { disabledFields.add("city") }
    public fun shouldTrackCity(): Boolean = !disabledFields.contains("city")

    public fun disableCountry(): TrackingOptions = apply { disabledFields.add("country") }
    public fun shouldTrackCountry(): Boolean = !disabledFields.contains("country")

    public fun disableDeviceBrand(): TrackingOptions = apply { disabledFields.add("device_brand") }
    public fun shouldTrackDeviceBrand(): Boolean = !disabledFields.contains("device_brand")

    public fun disableDeviceManufacturer(): TrackingOptions = apply { disabledFields.add("device_manufacturer") }
    public fun shouldTrackDeviceManufacturer(): Boolean = !disabledFields.contains("device_manufacturer")

    public fun disableDeviceModel(): TrackingOptions = apply { disabledFields.add("device_model") }
    public fun shouldTrackDeviceModel(): Boolean = !disabledFields.contains("device_model")

    public fun disableDma(): TrackingOptions = apply { disabledFields.add("dma") }
    public fun shouldTrackDma(): Boolean = !disabledFields.contains("dma")

    public fun disableIpAddress(): TrackingOptions = apply { disabledFields.add("ip_address") }
    public fun shouldTrackIpAddress(): Boolean = !disabledFields.contains("ip_address")

    public fun disableLanguage(): TrackingOptions = apply { disabledFields.add("language") }
    public fun shouldTrackLanguage(): Boolean = !disabledFields.contains("language")

    public fun disableLatLng(): TrackingOptions = apply { disabledFields.add("lat_lng") }
    public fun shouldTrackLatLng(): Boolean = !disabledFields.contains("lat_lng")

    public fun disableOsName(): TrackingOptions = apply { disabledFields.add("os_name") }
    public fun shouldTrackOsName(): Boolean = !disabledFields.contains("os_name")

    public fun disableOsVersion(): TrackingOptions = apply { disabledFields.add("os_version") }
    public fun shouldTrackOsVersion(): Boolean = !disabledFields.contains("os_version")

    public fun disablePlatform(): TrackingOptions = apply { disabledFields.add("platform") }
    public fun shouldTrackPlatform(): Boolean = !disabledFields.contains("platform")

    public fun disableRegion(): TrackingOptions = apply { disabledFields.add("region") }
    public fun shouldTrackRegion(): Boolean = !disabledFields.contains("region")

    public fun disableVersionName(): TrackingOptions = apply { disabledFields.add("version_name") }
    public fun shouldTrackVersionName(): Boolean = !disabledFields.contains("version_name")

    /**
     * Merge another TrackingOptions into this one
     */
    public fun mergeIn(other: TrackingOptions): TrackingOptions = apply {
        disabledFields.addAll(other.disabledFields)
    }

    public companion object {
        /**
         * Create a copy of TrackingOptions
         */
        public fun copyOf(other: TrackingOptions): TrackingOptions =
            TrackingOptions(other.disabledFields.toMutableSet())

        /**
         * Create TrackingOptions for COPPA compliance
         */
        public fun forCoppaControl(): TrackingOptions = TrackingOptions().apply {
            disableCity()
            disableIpAddress()
            disableLatLng()
        }
    }
}

/**
 * Interaction tracking options (Android only)
 */
public data class InteractionsOptions(
    /**
     * Rage click detection options
     */
    public val rageClick: RageClickOptions = RageClickOptions(),

    /**
     * Dead click detection options
     */
    public val deadClick: DeadClickOptions = DeadClickOptions(),
)

/**
 * Rage click detection options
 */
public data class RageClickOptions(
    /**
     * Enable rage click detection (default: true)
     */
    public val enabled: Boolean = true,
)

/**
 * Dead click detection options
 */
public data class DeadClickOptions(
    /**
     * Enable dead click detection (default: true)
     */
    public val enabled: Boolean = true,
)
