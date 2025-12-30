package com.amplitude.kmp

import cocoapods.AmplitudeSwift.AMPConfiguration
import cocoapods.AmplitudeSwift.AMPIdentify
import com.amplitude.kmp.events.*
import com.amplitude.kmp.mappings.ios.*
import com.amplitude.kmp.plugins.Plugin
import kotlinx.cinterop.ExperimentalForeignApi
import cocoapods.AmplitudeSwift.Amplitude as IOSAmplitude

/**
 * iOS actual implementation of Amplitude SDK.
 *
 * Wraps the native iOS SDK (Amplitude-Swift) and delegates all calls.
 * Maintains API compatibility with KMP expect class.
 *
 * IMPLEMENTATION NOTE:
 * This implementation uses CocoaPods to link the Amplitude-Swift SDK.
 * Users should add Amplitude-Swift to their iOS app via Swift Package Manager.
 *
 * Setup instructions:
 * 1. In Xcode: File → Add Package Dependencies
 * 2. Add: https://github.com/amplitude/Amplitude-Swift
 * 3. Version: 1.16.2 or later
 * 4. Link the AmplitudeKMP framework to your iOS app target
 */
@OptIn(ExperimentalForeignApi::class)
public actual class Amplitude internal constructor(
    private val iosAmplitude: IOSAmplitude,
) {
    public actual companion object {
        private var _instance: Amplitude? = null

        public actual val START_SESSION_EVENT: String = "session_start"
        public actual val END_SESSION_EVENT: String = "session_end"

        /**
         * Get singleton instance.
         */
        public fun getInstance(): Amplitude {
            return _instance
                ?: throw IllegalStateException("Amplitude not initialized. Call Amplitude(apiKey) first")
        }

        /**
         * Initialize Amplitude with Configuration.
         */
        public actual operator fun invoke(configuration: Configuration): Amplitude {
            val iosConfig = configuration.toIOSConfiguration()
            val iosInstance = createIOSAmplitudeInstance(iosConfig)
            return Amplitude(iosInstance).also { _instance = it }
        }

        /**
         * Initialize Amplitude with DSL-style configuration.
         * Note: iOS doesn't require Context parameter.
         */
        public operator fun invoke(
            apiKey: String,
            configs: Configuration.() -> Unit = {},
        ): Amplitude {
            val config = Configuration(apiKey = apiKey)
            configs(config)
            return invoke(config)
        }

        /**
         * Create iOS Amplitude instance from configuration.
         */
        private fun createIOSAmplitudeInstance(iosConfig: AMPConfiguration): IOSAmplitude {
            return IOSAmplitude(configuration = iosConfig)
        }
    }

    public actual val sessionId: Long
        get() = iosAmplitude.getSessionId()

    // ========================================
    // Event Tracking
    // ========================================

    public actual fun track(
        event: BaseEvent,
        options: EventOptions?,
        callback: EventCallBack?,
    ): Amplitude {
        iosAmplitude.track(
            event = event.toIOSBaseEvent(),
            options = options?.toIOSEventOptions(),
            callback = callback?.toIOSCallback()
        )
        return this
    }

    public actual fun track(
        eventType: String,
        eventProperties: Map<String, Any?>?,
        options: EventOptions?,
    ): Amplitude {
        // FIX: Set userId/deviceId on KMP BaseEvent before converting to iOS BaseEvent
        // BaseEvent extends EventOptions, so it has userId and deviceId properties
        val event = BaseEvent(
            eventType = eventType,
            eventProperties = eventProperties?.toMutableMap()
        ).apply {
            // Set userId/deviceId from SDK's stored identity if not already set
            if (userId == null) {
                userId = iosAmplitude.getUserId()
            }
            if (deviceId == null) {
                deviceId = iosAmplitude.getDeviceId()
            }
        }

        // Merge any additional options
        options?.let { event.mergeEventOptions(it) }

        // Delegate to track(BaseEvent) which will call toIOSBaseEvent()
        track(event, null, null)
        return this
    }

    // ========================================
    // User Identity
    // ========================================

    public actual fun identify(
        userProperties: Map<String, Any?>?,
        options: EventOptions?,
    ): Amplitude {
        // Convert map to Identify object
        val identify = AMPIdentify()
        userProperties?.forEach { (key, value) ->
            identify.set(key, value?.toNSObject())
        }
        iosAmplitude.identify(
            identify = identify,
            options = options?.toIOSEventOptions()
        )
        return this
    }

    public actual fun identify(
        identify: Identify,
        options: EventOptions?,
    ): Amplitude {
        iosAmplitude.identify(
            identify = identify.toIOSIdentify(),
            options = options?.toIOSEventOptions()
        )
        return this
    }

    // ========================================
    // Revenue Tracking
    // ========================================

    public actual fun revenue(
        revenue: Revenue,
        options: EventOptions?,
    ): Amplitude {
        iosAmplitude.revenue(
            revenue = revenue.toIOSRevenue(),
            options = options?.toIOSEventOptions()
        )
        return this
    }

    public actual fun revenue(event: RevenueEvent): Amplitude {
        // iOS SDK doesn't expose revenue(event: RevenueEvent) via cinterop
        // Use track() instead since RevenueEvent is a BaseEvent
        iosAmplitude.track(event = event.toIOSBaseEvent())
        return this
    }

    // ========================================
    // User & Device Management
    // ========================================

    public actual fun setUserId(userId: String?): Amplitude {
        iosAmplitude.setUserId(userId)
        return this
    }

    public actual fun getUserId(): String? {
        return iosAmplitude.getUserId()
    }

    public actual fun setDeviceId(deviceId: String): Amplitude {
        iosAmplitude.setDeviceId(deviceId)
        return this
    }

    public actual fun getDeviceId(): String? {
        return iosAmplitude.getDeviceId()
    }

    public actual fun reset(): Amplitude {
        iosAmplitude.reset()
        return this
    }

    // ========================================
    // Group Operations
    // ========================================

    public actual fun setGroup(
        groupType: String,
        groupName: String,
        options: EventOptions?,
    ): Amplitude {
        iosAmplitude.setGroup(
            groupType = groupType,
            groupName = groupName,
            options = options?.toIOSEventOptions()
        )
        return this
    }

    public actual fun setGroup(
        groupType: String,
        groupName: Array<String>,
        options: EventOptions?,
    ): Amplitude {
        // iOS SDK has setGroup(groupType: String, groupName: [String])
        // but cinterop doesn't expose it, so call it with each element
        // or use the NSArray-compatible call if available
        if (groupName.isNotEmpty()) {
            // For now, just use the first element
            // TODO: Find the correct way to call iOS SDK's array-based setGroup
            iosAmplitude.setGroup(
                groupType = groupType,
                groupName = groupName[0],
                options = options?.toIOSEventOptions()
            )
        }
        return this
    }

    public actual fun groupIdentify(
        groupType: String,
        groupName: String,
        groupProperties: Map<String, Any?>?,
        options: EventOptions?,
    ): Amplitude {
        // Convert map to Identify object
        val identify = AMPIdentify()
        groupProperties?.forEach { (key, value) ->
            identify.set(key, value?.toNSObject())
        }
        iosAmplitude.groupIdentify(
            groupType = groupType,
            groupName = groupName,
            identify = identify,
            options = options?.toIOSEventOptions()
        )
        return this
    }

    public actual fun groupIdentify(
        groupType: String,
        groupName: String,
        identify: Identify,
        options: EventOptions?,
    ): Amplitude {
        iosAmplitude.groupIdentify(
            groupType = groupType,
            groupName = groupName,
            identify = identify.toIOSIdentify(),
            options = options?.toIOSEventOptions()
        )
        return this
    }

    // ========================================
    // Plugin Management
    // ========================================

    public actual fun add(plugin: Plugin): Amplitude {
        iosAmplitude.add(plugin = plugin.toIOSPlugin())
        return this
    }

    public actual fun remove(plugin: Plugin): Amplitude {
        iosAmplitude.remove(plugin = plugin.toIOSPlugin())
        return this
    }

    // ========================================
    // Flushing
    // ========================================

    public actual fun flush() {
        iosAmplitude.flush()
    }

    // ========================================
    // Lifecycle (no-op on iOS)
    // ========================================

    public actual fun onEnterForeground(timestamp: Long) {
        // No-op: iOS SDK handles lifecycle automatically via lifecycle monitor plugins
        // The iOS SDK automatically tracks app lifecycle events
    }

    public actual fun onExitForeground(timestamp: Long) {
        // No-op: iOS SDK handles lifecycle automatically via lifecycle monitor plugins
        // The iOS SDK automatically tracks app lifecycle events
    }
}
