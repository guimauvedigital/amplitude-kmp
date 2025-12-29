package com.amplitude.kmp.sample

/**
 * Amplitude API key - platform-specific implementation.
 * Each platform defines the actual value.
 */
expect val amplitudeApiKey: String

/**
 * Platform-specific context (Android Context or null for iOS).
 */
expect val platformContext: Any?
