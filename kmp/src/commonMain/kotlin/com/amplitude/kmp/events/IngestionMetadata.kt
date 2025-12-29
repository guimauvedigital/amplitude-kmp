package com.amplitude.kmp.events

/**
 * Ingestion metadata for event enrichment.
 *
 * Maintains API compatibility with Android SDK's IngestionMetadata class.
 */
public data class IngestionMetadata(
    /**
     * Source name for the event (e.g., "amplitude-kmp")
     */
    public val sourceName: String? = null,

    /**
     * Source version for the event (SDK version)
     */
    public val sourceVersion: String? = null,
)
