package com.amplitude.kmp.utils

/**
 * Platform-agnostic lock for thread synchronization.
 *
 * Uses expect/actual pattern to provide platform-specific implementations:
 * - Android: synchronized blocks
 * - iOS: NSRecursiveLock
 */
internal expect class Lock() {
    /**
     * Execute a block while holding the lock
     */
    inline fun <T> withLock(block: () -> T): T
}
