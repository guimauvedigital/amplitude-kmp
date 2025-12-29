package com.amplitude.kmp.utils

import platform.Foundation.NSRecursiveLock

/**
 * iOS implementation of Lock using NSRecursiveLock.
 */
internal actual class Lock {
    private val lock = NSRecursiveLock()

    actual inline fun <T> withLock(block: () -> T): T {
        lock.lock()
        try {
            return block()
        } finally {
            lock.unlock()
        }
    }
}
