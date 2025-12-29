package com.amplitude.kmp.plugins

import com.amplitude.kmp.Amplitude
import com.amplitude.kmp.events.BaseEvent

/**
 * Plugin interface for extending Amplitude SDK functionality.
 *
 * Maintains API compatibility with Android SDK's Plugin interface.
 *
 * Plugins allow you to extend SDK functionality by intercepting and modifying events
 * before they are sent to Amplitude servers.
 */
public interface Plugin {
    /**
     * Plugin type determines when the plugin is executed in the event pipeline
     */
    public val type: Type

    /**
     * Plugin types defining execution order
     */
    public enum class Type {
        /**
         * Executed before event enrichment
         */
        BEFORE,

        /**
         * Executed during event enrichment (add context data)
         */
        ENRICHMENT,

        /**
         * Executed after enrichment, responsible for sending events
         */
        DESTINATION,

        /**
         * Executed after all other plugins, for observation only
         */
        OBSERVE,

        /**
         * Utility plugins that don't fit other categories
         */
        UTILITY
    }

    /**
     * Setup method called when plugin is added to Amplitude instance
     *
     * @param amplitude The Amplitude instance this plugin is attached to
     */
    public fun setup(amplitude: Amplitude)

    /**
     * Execute method called for each event passing through the plugin
     *
     * @param event The event to process
     * @return The modified event, or null to drop the event
     */
    public fun execute(event: BaseEvent): BaseEvent?
}
