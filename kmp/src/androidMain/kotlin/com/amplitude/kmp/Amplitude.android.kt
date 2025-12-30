package com.amplitude.kmp

import android.content.Context
import com.amplitude.kmp.events.*
import com.amplitude.kmp.mappings.android.*
import com.amplitude.kmp.plugins.Plugin
import com.amplitude.android.Amplitude as AndroidAmplitude

/**
 * Android actual implementation of Amplitude SDK.
 *
 * Wraps the native Android SDK (com.amplitude:analytics-android) and delegates all calls.
 * Maintains API compatibility with KMP expect class.
 */
public actual class Amplitude internal constructor(
    private val androidAmplitude: AndroidAmplitude,
) {
    public actual companion object {
        private var _instance: Amplitude? = null

        public actual val START_SESSION_EVENT: String = AndroidAmplitude.START_SESSION_EVENT
        public actual val END_SESSION_EVENT: String = AndroidAmplitude.END_SESSION_EVENT

        public actual fun getInstance(): Amplitude {
            return _instance
                ?: throw IllegalStateException("Amplitude not initialized. Call Amplitude(apiKey, context) first")
        }

        public actual operator fun invoke(configuration: Configuration): Amplitude {
            val androidConfig = configuration.toAndroidConfiguration()
            val androidInstance = AndroidAmplitude(androidConfig)
            return Amplitude(androidInstance).also { _instance = it }
        }

        /**
         * Initialize Amplitude with Configuration and wait for build to complete.
         * This is the recommended way to initialize if you want to ensure the SDK
         * is fully ready before tracking events.
         */
        public suspend fun invokeAndAwait(configuration: Configuration): Amplitude {
            val androidConfig = configuration.toAndroidConfiguration()
            val androidInstance = AndroidAmplitude(androidConfig)

            // Wait for build to complete
            androidInstance.isBuilt.await()

            return Amplitude(androidInstance).also { _instance = it }
        }

        /**
         * Initialize Amplitude with DSL-style configuration.
         * Maintains API compatibility with Android SDK's factory function.
         */
        public operator fun invoke(
            apiKey: String,
            context: Context,
            configs: Configuration.() -> Unit = {},
        ): Amplitude {
            val config = Configuration(apiKey = apiKey, androidContext = context)
            configs(config)
            return invoke(config)
        }
    }

    public actual val sessionId: Long
        get() = androidAmplitude.sessionId

    // ========================================
    // Event Tracking
    // ========================================

    public actual fun track(
        event: BaseEvent,
        options: EventOptions?,
        callback: EventCallBack?,
    ): Amplitude {
        val androidEvent = event.toAndroidBaseEvent()
        val androidOptions = options?.toAndroidEventOptions()
        val androidCallback = callback?.toAndroidCallback()
        androidAmplitude.track(androidEvent, androidOptions, androidCallback)
        return this
    }

    public actual fun track(
        eventType: String,
        eventProperties: Map<String, Any?>?,
        options: EventOptions?,
    ): Amplitude {
        androidAmplitude.track(eventType, eventProperties, options?.toAndroidEventOptions())
        return this
    }

    // ========================================
    // User Identity
    // ========================================

    public actual fun identify(
        userProperties: Map<String, Any?>?,
        options: EventOptions?,
    ): Amplitude {
        androidAmplitude.identify(userProperties, options?.toAndroidEventOptions())
        return this
    }

    public actual fun identify(
        identify: Identify,
        options: EventOptions?,
    ): Amplitude {
        androidAmplitude.identify(identify.toAndroidIdentify(), options?.toAndroidEventOptions())
        return this
    }

    // ========================================
    // Revenue Tracking
    // ========================================

    public actual fun revenue(
        revenue: Revenue,
        options: EventOptions?,
    ): Amplitude {
        androidAmplitude.revenue(revenue.toAndroidRevenue(), options?.toAndroidEventOptions())
        return this
    }

    public actual fun revenue(event: RevenueEvent): Amplitude {
        // Convert RevenueEvent to Android BaseEvent and track it
        androidAmplitude.track(event.toAndroidBaseEvent())
        return this
    }

    // ========================================
    // User & Device Management
    // ========================================

    public actual fun setUserId(userId: String?): Amplitude {
        androidAmplitude.setUserId(userId)
        return this
    }

    public actual fun getUserId(): String? {
        return androidAmplitude.getUserId()
    }

    public actual fun setDeviceId(deviceId: String): Amplitude {
        androidAmplitude.setDeviceId(deviceId)
        return this
    }

    public actual fun getDeviceId(): String? {
        return androidAmplitude.getDeviceId()
    }

    public actual fun reset(): Amplitude {
        androidAmplitude.reset()
        return this
    }

    /**
     * Wait for the SDK to complete its asynchronous initialization.
     *
     * This ensures that the plugin pipeline (including AndroidContextPlugin)
     * is fully set up before tracking events.
     *
     * @return true if build completed successfully, false otherwise
     */
    public suspend fun awaitInitialization(): Boolean {
        return androidAmplitude.isBuilt.await()
    }

    /**
     * Check if the SDK has completed its asynchronous initialization.
     *
     * @return true if build is complete, false if still initializing
     */
    public fun isInitialized(): Boolean {
        return androidAmplitude.isBuilt.isCompleted
    }

    // ========================================
    // Group Operations
    // ========================================

    public actual fun setGroup(
        groupType: String,
        groupName: String,
        options: EventOptions?,
    ): Amplitude {
        androidAmplitude.setGroup(groupType, groupName, options?.toAndroidEventOptions())
        return this
    }

    public actual fun setGroup(
        groupType: String,
        groupName: Array<String>,
        options: EventOptions?,
    ): Amplitude {
        androidAmplitude.setGroup(groupType, groupName, options?.toAndroidEventOptions())
        return this
    }

    public actual fun groupIdentify(
        groupType: String,
        groupName: String,
        groupProperties: Map<String, Any?>?,
        options: EventOptions?,
    ): Amplitude {
        androidAmplitude.groupIdentify(groupType, groupName, groupProperties, options?.toAndroidEventOptions())
        return this
    }

    public actual fun groupIdentify(
        groupType: String,
        groupName: String,
        identify: Identify,
        options: EventOptions?,
    ): Amplitude {
        androidAmplitude.groupIdentify(
            groupType,
            groupName,
            identify.toAndroidIdentify(),
            options?.toAndroidEventOptions()
        )
        return this
    }

    // ========================================
    // Plugin Management
    // ========================================

    public actual fun add(plugin: Plugin): Amplitude {
        androidAmplitude.add(plugin.toAndroidPlugin())
        return this
    }

    public actual fun remove(plugin: Plugin): Amplitude {
        androidAmplitude.remove(plugin.toAndroidPlugin())
        return this
    }

    // ========================================
    // Flushing
    // ========================================

    public actual fun flush() {
        androidAmplitude.flush()
    }

    // ========================================
    // Lifecycle (Android-specific)
    // ========================================

    @OptIn(com.amplitude.android.GuardedAmplitudeFeature::class)
    public actual fun onEnterForeground(timestamp: Long) {
        androidAmplitude.onEnterForeground(timestamp)
    }

    @OptIn(com.amplitude.android.GuardedAmplitudeFeature::class)
    public actual fun onExitForeground(timestamp: Long) {
        androidAmplitude.onExitForeground(timestamp)
    }
}
