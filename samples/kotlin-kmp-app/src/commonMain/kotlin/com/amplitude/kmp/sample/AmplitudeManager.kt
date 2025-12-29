package com.amplitude.kmp.sample

import com.amplitude.kmp.Amplitude

/**
 * Singleton to hold Amplitude instance.
 * Initialized by platform-specific code.
 */
object AmplitudeManager {
    lateinit var amplitude: Amplitude
        private set

    fun initialize(amplitude: Amplitude) {
        this.amplitude = amplitude
    }
}
