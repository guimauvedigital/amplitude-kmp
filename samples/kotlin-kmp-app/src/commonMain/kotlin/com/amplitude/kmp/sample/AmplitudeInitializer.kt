package com.amplitude.kmp.sample

import com.amplitude.kmp.Amplitude
import com.amplitude.kmp.Configuration
import com.amplitude.kmp.AutocaptureOption
import com.amplitude.kmp.events.BaseEvent
import com.amplitude.kmp.plugins.Plugin

/**
 * Shared Amplitude initialization logic.
 * This is the single source of truth for SDK configuration across all platforms.
 */
object AmplitudeInitializer {

    /**
     * Initialize Amplitude with the given API key and optional platform context.
     * This function handles all platform-specific configuration internally.
     *
     * @param apiKey The Amplitude API key
     * @param platformContext Platform-specific context (Android Context or null for iOS)
     * @param userId Optional user ID to set immediately
     */
    fun initialize(
        apiKey: String,
        platformContext: Any? = null,
        userId: String? = null
    ): Amplitude {
        val configuration = Configuration(
            apiKey = apiKey,
            androidContext = platformContext  // Only used on Android, ignored on iOS
        ).apply {
            // Enable all autocapture features
            autocapture = setOf(
                AutocaptureOption.SESSIONS,
                AutocaptureOption.APP_LIFECYCLES,
                AutocaptureOption.SCREEN_VIEWS
            )

            // Set flush configuration
            flushQueueSize = 30
            flushIntervalMillis = 30000

            // Enable location listening (Android only, ignored on iOS)
            locationListening = true
        }

        val amplitude = Amplitude(configuration)

        // Add a sample enrichment plugin
        amplitude.add(object : Plugin {
            override val type: Plugin.Type = Plugin.Type.ENRICHMENT

            override fun setup(amplitude: Amplitude) {
                // Setup if needed
            }

            override fun execute(event: BaseEvent): BaseEvent {
                // Add custom property to all events
                event.eventProperties = event.eventProperties?.toMutableMap() ?: mutableMapOf()
                event.eventProperties?.put("sdk_version", "kmp-1.0")
                event.eventProperties?.put("platform", getPlatformName())
                return event
            }
        })

        // Set initial user ID if provided
        if (userId != null) {
            amplitude.setUserId(userId)
        }

        // Initialize AmplitudeManager singleton
        AmplitudeManager.initialize(amplitude)

        return amplitude
    }

    /**
     * Suspend version for platforms that need async initialization (like Android).
     */
    suspend fun initializeAndAwait(
        apiKey: String,
        platformContext: Any? = null,
        userId: String? = null
    ): Amplitude {
        val configuration = Configuration(
            apiKey = apiKey,
            androidContext = platformContext
        ).apply {
            autocapture = setOf(
                AutocaptureOption.SESSIONS,
                AutocaptureOption.APP_LIFECYCLES,
                AutocaptureOption.SCREEN_VIEWS
            )
            flushQueueSize = 30
            flushIntervalMillis = 30000
            locationListening = true
        }

        val amplitude = Amplitude(configuration)

        // Add enrichment plugin
        amplitude.add(object : Plugin {
            override val type: Plugin.Type = Plugin.Type.ENRICHMENT

            override fun setup(amplitude: Amplitude) {}

            override fun execute(event: BaseEvent): BaseEvent {
                event.eventProperties = event.eventProperties?.toMutableMap() ?: mutableMapOf()
                event.eventProperties?.put("sdk_version", "kmp-1.0")
                event.eventProperties?.put("platform", getPlatformName())
                return event
            }
        })

        if (userId != null) {
            amplitude.setUserId(userId)
        }

        AmplitudeManager.initialize(amplitude)

        return amplitude
    }
}
