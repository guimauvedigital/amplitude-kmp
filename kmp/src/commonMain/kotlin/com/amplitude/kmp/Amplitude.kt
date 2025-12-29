package com.amplitude.kmp

import com.amplitude.kmp.events.BaseEvent
import com.amplitude.kmp.events.EventCallBack
import com.amplitude.kmp.events.EventOptions
import com.amplitude.kmp.events.Identify
import com.amplitude.kmp.events.Revenue
import com.amplitude.kmp.events.RevenueEvent
import com.amplitude.kmp.plugins.Plugin

/**
 * Main Amplitude SDK class.
 *
 * Maintains API compatibility with Android SDK's Amplitude class.
 * Uses expect/actual pattern for platform-specific implementations.
 *
 * All methods return the Amplitude instance for method chaining except where noted.
 */
public expect class Amplitude {
    public companion object {
        /**
         * Session start event constant
         */
        public val START_SESSION_EVENT: String

        /**
         * Session end event constant
         */
        public val END_SESSION_EVENT: String
    }

    /**
     * Current session ID
     */
    public val sessionId: Long

    // ========================================
    // Event Tracking
    // ========================================

    /**
     * Track an event with optional EventOptions and callback.
     *
     * @param event The event to track
     * @param options Optional event-specific options
     * @param callback Optional callback for event completion
     * @return this Amplitude instance for method chaining
     */
    public fun track(
        event: BaseEvent,
        options: EventOptions? = null,
        callback: EventCallBack? = null
    ): Amplitude

    /**
     * Track an event by type and properties.
     *
     * @param eventType The type of event (e.g., "Button Clicked")
     * @param eventProperties Optional event properties
     * @param options Optional event-specific options
     * @return this Amplitude instance for method chaining
     */
    public fun track(
        eventType: String,
        eventProperties: Map<String, Any?>? = null,
        options: EventOptions? = null
    ): Amplitude

    // ========================================
    // User Identity
    // ========================================

    /**
     * Identify user with properties map.
     *
     * @param userProperties User properties to set
     * @param options Optional event-specific options
     * @return this Amplitude instance for method chaining
     */
    public fun identify(
        userProperties: Map<String, Any?>?,
        options: EventOptions? = null
    ): Amplitude

    /**
     * Identify user with Identify builder.
     *
     * @param identify Identify object with user property operations
     * @param options Optional event-specific options
     * @return this Amplitude instance for method chaining
     */
    public fun identify(
        identify: Identify,
        options: EventOptions? = null
    ): Amplitude

    // ========================================
    // Revenue Tracking
    // ========================================

    /**
     * Track revenue with Revenue builder.
     *
     * @param revenue Revenue object with revenue data
     * @param options Optional event-specific options
     * @return this Amplitude instance for method chaining
     */
    public fun revenue(
        revenue: Revenue,
        options: EventOptions? = null
    ): Amplitude

    /**
     * Track revenue with RevenueEvent.
     *
     * @param event RevenueEvent to track
     * @return this Amplitude instance for method chaining
     */
    public fun revenue(event: RevenueEvent): Amplitude

    // ========================================
    // User & Device Management
    // ========================================

    /**
     * Set the user ID.
     *
     * @param userId User ID to set (null to clear)
     * @return this Amplitude instance for method chaining
     */
    public fun setUserId(userId: String?): Amplitude

    /**
     * Get the current user ID.
     *
     * @return Current user ID or null
     */
    public fun getUserId(): String?

    /**
     * Set the device ID.
     *
     * @param deviceId Device ID to set
     * @return this Amplitude instance for method chaining
     */
    public fun setDeviceId(deviceId: String): Amplitude

    /**
     * Get the current device ID.
     *
     * @return Current device ID or null
     */
    public fun getDeviceId(): String?

    /**
     * Reset user identity (clears userId and generates new deviceId).
     *
     * @return this Amplitude instance for method chaining
     */
    public fun reset(): Amplitude

    // ========================================
    // Group Operations
    // ========================================

    /**
     * Set a single group value.
     *
     * @param groupType Group type (e.g., "company")
     * @param groupName Group name/value
     * @param options Optional event-specific options
     * @return this Amplitude instance for method chaining
     */
    public fun setGroup(
        groupType: String,
        groupName: String,
        options: EventOptions? = null
    ): Amplitude

    /**
     * Set multiple group values.
     *
     * @param groupType Group type (e.g., "company")
     * @param groupName Array of group names/values
     * @param options Optional event-specific options
     * @return this Amplitude instance for method chaining
     */
    public fun setGroup(
        groupType: String,
        groupName: Array<String>,
        options: EventOptions? = null
    ): Amplitude

    /**
     * Identify a group with properties map.
     *
     * @param groupType Group type
     * @param groupName Group name
     * @param groupProperties Group properties to set
     * @param options Optional event-specific options
     * @return this Amplitude instance for method chaining
     */
    public fun groupIdentify(
        groupType: String,
        groupName: String,
        groupProperties: Map<String, Any?>?,
        options: EventOptions? = null
    ): Amplitude

    /**
     * Identify a group with Identify builder.
     *
     * @param groupType Group type
     * @param groupName Group name
     * @param identify Identify object with group property operations
     * @param options Optional event-specific options
     * @return this Amplitude instance for method chaining
     */
    public fun groupIdentify(
        groupType: String,
        groupName: String,
        identify: Identify,
        options: EventOptions? = null
    ): Amplitude

    // ========================================
    // Plugin Management
    // ========================================

    /**
     * Add a plugin to the SDK.
     *
     * @param plugin Plugin to add
     * @return this Amplitude instance for method chaining
     */
    public fun add(plugin: Plugin): Amplitude

    /**
     * Remove a plugin from the SDK.
     *
     * @param plugin Plugin to remove
     * @return this Amplitude instance for method chaining
     */
    public fun remove(plugin: Plugin): Amplitude

    // ========================================
    // Flushing
    // ========================================

    /**
     * Flush all pending events immediately.
     */
    public fun flush()

    // ========================================
    // Lifecycle (Android-specific, no-op on iOS)
    // ========================================

    /**
     * Notify SDK that app entered foreground.
     * Android-specific, no-op on iOS.
     *
     * @param timestamp Timestamp in milliseconds
     */
    public fun onEnterForeground(timestamp: Long)

    /**
     * Notify SDK that app exited foreground.
     * Android-specific, no-op on iOS.
     *
     * @param timestamp Timestamp in milliseconds
     */
    public fun onExitForeground(timestamp: Long)
}
