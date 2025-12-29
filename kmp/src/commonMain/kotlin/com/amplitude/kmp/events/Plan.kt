package com.amplitude.kmp.events

/**
 * Tracking plan configuration for Amplitude events.
 *
 * Maintains API compatibility with Android SDK's Plan class.
 */
public data class Plan(
    /**
     * Git branch name for the tracking plan
     */
    public val branch: String? = null,

    /**
     * Source of the tracking plan (e.g., "web", "mobile")
     */
    public val source: String? = null,

    /**
     * Version string for the tracking plan
     */
    public val version: String? = null,

    /**
     * Version ID for the tracking plan
     */
    public val versionId: String? = null,
)
