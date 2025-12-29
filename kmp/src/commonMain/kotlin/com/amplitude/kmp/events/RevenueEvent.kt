package com.amplitude.kmp.events

/**
 * Revenue event class.
 *
 * Extends BaseEvent with revenue event type.
 * Maintains API compatibility with Android SDK's RevenueEvent class.
 */
public class RevenueEvent : BaseEvent() {
    init {
        eventType = AmplitudeEventConstants.REVENUE_EVENT
    }
}
