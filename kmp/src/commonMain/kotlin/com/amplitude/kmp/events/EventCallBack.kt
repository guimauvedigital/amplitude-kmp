package com.amplitude.kmp.events

/**
 * Callback function type for event tracking.
 *
 * @param event The event that was tracked
 * @param status HTTP status code (200 for success, error codes for failures)
 * @param message Response message or error description
 */
public typealias EventCallBack = (event: BaseEvent, status: Int, message: String) -> Unit
