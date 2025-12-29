package com.amplitude.kmp.events

/**
 * Revenue tracking class for Amplitude.
 *
 * Maintains 100% API compatibility with Android SDK's Revenue class.
 * Uses public vars with custom setters for validation, matching Android SDK exactly.
 */
public open class Revenue {
    /**
     * Product identifier
     */
    public var productId: String? = null
        set(value) {
            if (!value.isNullOrEmpty()) {
                field = value
            }
        }

    /**
     * Quantity of items (default: 1, must be > 0)
     */
    public var quantity: Int = 1
        set(value) {
            if (value > 0) {
                field = value
            }
        }

    /**
     * Price per item (required for valid revenue event)
     */
    public var price: Double? = null
        set(value) {
            value?.let {
                field = value
            }
        }

    /**
     * Revenue type (e.g., "purchase", "subscription")
     */
    public var revenueType: String? = null

    /**
     * Currency code (e.g., "USD", "EUR")
     */
    public var currency: String? = null

    /**
     * Receipt data for validation (iOS/Android)
     */
    public var receipt: String? = null

    /**
     * Receipt signature for validation (Android)
     */
    public var receiptSig: String? = null

    /**
     * Additional event properties for this revenue event
     */
    public var properties: MutableMap<String, Any?>? = null

    /**
     * Total revenue amount (calculated or explicit)
     */
    public var revenue: Double? = null
        set(value) {
            value?.let {
                field = value
            }
        }

    /**
     * Set the receipt and receipt signature.
     * Both fields are required to verify the revenue event.
     */
    public fun setReceipt(
        receipt: String,
        receiptSignature: String,
    ): Revenue {
        this.receipt = receipt
        receiptSig = receiptSignature
        return this
    }

    /**
     * Verifies that revenue object is valid and contains the required fields
     */
    public fun isValid(): Boolean {
        if (price == null) {
            return false
        }
        return true
    }

    /**
     * Convert to RevenueEvent for tracking
     */
    public fun toRevenueEvent(): RevenueEvent {
        val event = RevenueEvent()
        val eventProperties = properties ?: mutableMapOf<String, Any?>()
        productId?.let { eventProperties.put(REVENUE_PRODUCT_ID, it) }
        eventProperties.put(REVENUE_QUANTITY, quantity)
        price?.let { eventProperties.put(REVENUE_PRICE, it) }
        revenueType?.let { eventProperties.put(REVENUE_TYPE, it) }
        currency?.let { eventProperties.put(REVENUE_CURRENCY, it) }
        receipt?.let { eventProperties.put(REVENUE_RECEIPT, it) }
        receiptSig?.let { eventProperties.put(REVENUE_RECEIPT_SIG, it) }
        revenue?.let { eventProperties.put(REVENUE, it) }
        event.eventProperties = eventProperties
        return event
    }

    public companion object {
        internal const val REVENUE_PRODUCT_ID = "\$productId"
        internal const val REVENUE_QUANTITY = "\$quantity"
        internal const val REVENUE_PRICE = "\$price"
        internal const val REVENUE_TYPE = "\$revenueType"
        internal const val REVENUE_CURRENCY = "\$currency"
        internal const val REVENUE_RECEIPT = "\$receipt"
        internal const val REVENUE_RECEIPT_SIG = "\$receiptSig"
        internal const val REVENUE = "\$revenue"
    }
}
