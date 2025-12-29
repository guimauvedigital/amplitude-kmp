package com.amplitude.kmp.utils

/**
 * Android implementation of Lock using synchronized blocks.
 */
internal actual class Lock {
    private val lock = Any()

    actual inline fun <T> withLock(block: () -> T): T = synchronized(lock) {
        block()
    }
}
