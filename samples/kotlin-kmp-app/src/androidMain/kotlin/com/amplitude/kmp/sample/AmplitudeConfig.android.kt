package com.amplitude.kmp.sample

import android.annotation.SuppressLint
import android.content.Context

/**
 * Android-specific Amplitude API key.
 */
actual val amplitudeApiKey: String = BuildConfig.AMPLITUDE_API_KEY

/**
 * Android application context.
 * Set by MainApplication.
 */
@SuppressLint("StaticFieldLeak")
internal var androidContext: Context? = null

actual val platformContext: Any?
    get() = androidContext
