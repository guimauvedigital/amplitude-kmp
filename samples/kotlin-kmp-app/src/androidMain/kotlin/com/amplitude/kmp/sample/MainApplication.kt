package com.amplitude.kmp.sample

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.amplitude.kmp.Amplitude
import com.amplitude.kmp.AutocaptureOption
import com.amplitude.kmp.Configuration
import com.amplitude.kmp.events.BaseEvent
import com.amplitude.kmp.plugins.Plugin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainApplication : Application() {
    companion object {
        const val AMPLITUDE_API_KEY = BuildConfig.AMPLITUDE_API_KEY
    }

    override fun onCreate() {
        super.onCreate()

        // Store Android context for Amplitude initialization
        androidContext = applicationContext
    }
}
