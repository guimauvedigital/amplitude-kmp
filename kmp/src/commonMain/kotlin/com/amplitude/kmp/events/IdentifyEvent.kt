package com.amplitude.kmp.events

/**
 * Identify event class.
 *
 * Extends BaseEvent with identify event type.
 * Maintains API compatibility with Android SDK's IdentifyEvent class.
 */
public class IdentifyEvent : BaseEvent() {
    init {
        eventType = AmplitudeEventConstants.IDENTIFY_EVENT
    }
}
