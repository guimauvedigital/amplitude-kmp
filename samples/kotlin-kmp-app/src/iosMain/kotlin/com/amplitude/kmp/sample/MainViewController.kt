package com.amplitude.kmp.sample

import androidx.compose.ui.window.ComposeUIViewController

/**
 * Main view controller for iOS.
 * Amplitude is initialized automatically when the App composable loads.
 */
fun MainViewController() = ComposeUIViewController {
    App()
}
