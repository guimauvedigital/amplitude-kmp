package com.amplitude.kmp.ktx

import com.amplitude.kmp.Amplitude
import com.amplitude.kmp.events.BaseEvent
import com.amplitude.kmp.events.EventOptions
import com.amplitude.kmp.events.Identify
import com.amplitude.kmp.events.Revenue
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Coroutines extensions for Amplitude SDK.
 *
 * Provides suspend functions that convert callback-based API to coroutines.
 */

/**
 * Track an event and suspend until completion.
 *
 * @param event The event to track
 * @param options Optional event options
 * @return Result with Unit on success or exception on failure
 */
public suspend fun Amplitude.awaitTrack(
    event: BaseEvent,
    options: EventOptions? = null
): Result<Unit> = suspendCancellableCoroutine { continuation ->
    track(event, options) { _, status, message ->
        if (status == 200) {
            continuation.resume(Result.success(Unit))
        } else {
            continuation.resume(Result.failure(Exception("Track failed with status $status: $message")))
        }
    }
}

/**
 * Track an event by type and suspend until completion.
 *
 * @param eventType Event type
 * @param eventProperties Optional event properties
 * @param options Optional event options
 * @return Result with Unit on success or exception on failure
 */
public suspend fun Amplitude.awaitTrack(
    eventType: String,
    eventProperties: Map<String, Any?>? = null,
    options: EventOptions? = null
): Result<Unit> {
    val event = BaseEvent(eventType, eventProperties?.toMutableMap())
    return awaitTrack(event, options)
}

/**
 * Identify user with Identify builder and suspend until completion.
 *
 * @param identify Identify object with user property operations
 * @param options Optional event options
 * @return Result with Unit on success or exception on failure
 */
public suspend fun Amplitude.awaitIdentify(
    identify: Identify,
    options: EventOptions? = null
): Result<Unit> {
    val event = identify.toIdentifyEvent()
    return awaitTrack(event, options)
}

/**
 * Identify user with properties map and suspend until completion.
 *
 * @param userProperties User properties to set
 * @param options Optional event options
 * @return Result with Unit on success or exception on failure
 */
public suspend fun Amplitude.awaitIdentify(
    userProperties: Map<String, Any?>?,
    options: EventOptions? = null
): Result<Unit> {
    val identify = Identify().apply {
        userProperties?.forEach { (key, value) ->
            set(key, value ?: "")
        }
    }
    return awaitIdentify(identify, options)
}

/**
 * Track revenue and suspend until completion.
 *
 * @param revenue Revenue object with revenue data
 * @param options Optional event options
 * @return Result with Unit on success or exception on failure
 */
public suspend fun Amplitude.awaitRevenue(
    revenue: Revenue,
    options: EventOptions? = null
): Result<Unit> {
    val event = revenue.toRevenueEvent()
    return awaitTrack(event, options)
}

/**
 * Flush all pending events and suspend until completion.
 *
 * Note: The flush() method doesn't provide a callback, so this implementation
 * calls flush() and immediately returns success. For reliable flush completion,
 * consider using awaitTrack with a final tracking event.
 *
 * @return Result with Unit (always success)
 */
public suspend fun Amplitude.awaitFlush(): Result<Unit> {
    flush()
    return Result.success(Unit)
}

/**
 * Set user ID with coroutine support.
 *
 * @param userId User ID to set
 * @return this Amplitude instance for method chaining
 */
public suspend fun Amplitude.awaitSetUserId(userId: String?): Amplitude {
    return setUserId(userId)
}

/**
 * Set device ID with coroutine support.
 *
 * @param deviceId Device ID to set
 * @return this Amplitude instance for method chaining
 */
public suspend fun Amplitude.awaitSetDeviceId(deviceId: String): Amplitude {
    return setDeviceId(deviceId)
}

/**
 * Reset user identity with coroutine support.
 *
 * @return this Amplitude instance for method chaining
 */
public suspend fun Amplitude.awaitReset(): Amplitude {
    return reset()
}
