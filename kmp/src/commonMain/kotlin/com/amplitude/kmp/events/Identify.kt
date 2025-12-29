package com.amplitude.kmp.events

import com.amplitude.kmp.utils.Lock

/**
 * Identify class for user property operations.
 *
 * Thread-safe builder with 150+ overloaded methods for type-safe user property updates.
 * Maintains API compatibility with Android SDK's Identify class.
 *
 * Operations:
 * - set: Set or replace a user property value
 * - setOnce: Set a user property value only once
 * - add: Add a numeric value to a numeric user property
 * - append: Append a value to a user property array
 * - prepend: Prepend a value to a user property array
 * - preInsert: Insert a value at the beginning of a user property array (only if not present)
 * - postInsert: Insert a value at the end of a user property array (only if not present)
 * - remove: Remove a value from a user property array
 * - unset: Unset a user property
 * - clearAll: Clear all user properties
 */
@Suppress("TooManyFunctions")
public class Identify {
    private val lock = Lock()
    private val propertySet: MutableSet<String> = mutableSetOf()
    internal val operations: MutableMap<String, MutableMap<String, Any?>> = mutableMapOf()

    /**
     * Identify operation types
     */
    public enum class Operation(public val value: String) {
        SET("\$set"),
        SET_ONCE("\$setOnce"),
        ADD("\$add"),
        APPEND("\$append"),
        PREPEND("\$prepend"),
        PRE_INSERT("\$preInsert"),
        POST_INSERT("\$postInsert"),
        REMOVE("\$remove"),
        UNSET("\$unset"),
        CLEAR_ALL("\$clearAll")
    }

    // ========================================
    // SET Operations (15 overloads)
    // ========================================

    public fun set(property: String, value: Boolean): Identify = setUserProperty(Operation.SET, property, value)
    public fun set(property: String, value: Double): Identify = setUserProperty(Operation.SET, property, value)
    public fun set(property: String, value: Float): Identify = setUserProperty(Operation.SET, property, value)
    public fun set(property: String, value: Int): Identify = setUserProperty(Operation.SET, property, value)
    public fun set(property: String, value: Long): Identify = setUserProperty(Operation.SET, property, value)
    public fun set(property: String, value: String): Identify = setUserProperty(Operation.SET, property, value)
    public fun set(property: String, value: Map<String, Any>): Identify = setUserProperty(Operation.SET, property, value)
    public fun set(property: String, value: List<Any>): Identify = setUserProperty(Operation.SET, property, value)
    public fun set(property: String, value: Array<Boolean>): Identify = setUserProperty(Operation.SET, property, value.toList())
    public fun set(property: String, value: Array<Double>): Identify = setUserProperty(Operation.SET, property, value.toList())
    public fun set(property: String, value: Array<Float>): Identify = setUserProperty(Operation.SET, property, value.toList())
    public fun set(property: String, value: Array<Int>): Identify = setUserProperty(Operation.SET, property, value.toList())
    public fun set(property: String, value: Array<Long>): Identify = setUserProperty(Operation.SET, property, value.toList())
    public fun set(property: String, value: Array<String>): Identify = setUserProperty(Operation.SET, property, value.toList())
    public fun set(property: String, value: Any): Identify = setUserProperty(Operation.SET, property, value)

    // ========================================
    // SET_ONCE Operations (15 overloads)
    // ========================================

    public fun setOnce(property: String, value: Boolean): Identify = setUserProperty(Operation.SET_ONCE, property, value)
    public fun setOnce(property: String, value: Double): Identify = setUserProperty(Operation.SET_ONCE, property, value)
    public fun setOnce(property: String, value: Float): Identify = setUserProperty(Operation.SET_ONCE, property, value)
    public fun setOnce(property: String, value: Int): Identify = setUserProperty(Operation.SET_ONCE, property, value)
    public fun setOnce(property: String, value: Long): Identify = setUserProperty(Operation.SET_ONCE, property, value)
    public fun setOnce(property: String, value: String): Identify = setUserProperty(Operation.SET_ONCE, property, value)
    public fun setOnce(property: String, value: Map<String, Any>): Identify = setUserProperty(Operation.SET_ONCE, property, value)
    public fun setOnce(property: String, value: List<Any>): Identify = setUserProperty(Operation.SET_ONCE, property, value)
    public fun setOnce(property: String, value: Array<Boolean>): Identify = setUserProperty(Operation.SET_ONCE, property, value.toList())
    public fun setOnce(property: String, value: Array<Double>): Identify = setUserProperty(Operation.SET_ONCE, property, value.toList())
    public fun setOnce(property: String, value: Array<Float>): Identify = setUserProperty(Operation.SET_ONCE, property, value.toList())
    public fun setOnce(property: String, value: Array<Int>): Identify = setUserProperty(Operation.SET_ONCE, property, value.toList())
    public fun setOnce(property: String, value: Array<Long>): Identify = setUserProperty(Operation.SET_ONCE, property, value.toList())
    public fun setOnce(property: String, value: Array<String>): Identify = setUserProperty(Operation.SET_ONCE, property, value.toList())
    public fun setOnce(property: String, value: Any): Identify = setUserProperty(Operation.SET_ONCE, property, value)

    // ========================================
    // ADD Operations (4 numeric overloads)
    // ========================================

    public fun add(property: String, value: Double): Identify = setUserProperty(Operation.ADD, property, value)
    public fun add(property: String, value: Float): Identify = setUserProperty(Operation.ADD, property, value)
    public fun add(property: String, value: Int): Identify = setUserProperty(Operation.ADD, property, value)
    public fun add(property: String, value: Long): Identify = setUserProperty(Operation.ADD, property, value)

    // ========================================
    // APPEND Operations (15 overloads)
    // ========================================

    public fun append(property: String, value: Boolean): Identify = setUserProperty(Operation.APPEND, property, value)
    public fun append(property: String, value: Double): Identify = setUserProperty(Operation.APPEND, property, value)
    public fun append(property: String, value: Float): Identify = setUserProperty(Operation.APPEND, property, value)
    public fun append(property: String, value: Int): Identify = setUserProperty(Operation.APPEND, property, value)
    public fun append(property: String, value: Long): Identify = setUserProperty(Operation.APPEND, property, value)
    public fun append(property: String, value: String): Identify = setUserProperty(Operation.APPEND, property, value)
    public fun append(property: String, value: Map<String, Any>): Identify = setUserProperty(Operation.APPEND, property, value)
    public fun append(property: String, value: List<Any>): Identify = setUserProperty(Operation.APPEND, property, value)
    public fun append(property: String, value: Array<Boolean>): Identify = setUserProperty(Operation.APPEND, property, value.toList())
    public fun append(property: String, value: Array<Double>): Identify = setUserProperty(Operation.APPEND, property, value.toList())
    public fun append(property: String, value: Array<Float>): Identify = setUserProperty(Operation.APPEND, property, value.toList())
    public fun append(property: String, value: Array<Int>): Identify = setUserProperty(Operation.APPEND, property, value.toList())
    public fun append(property: String, value: Array<Long>): Identify = setUserProperty(Operation.APPEND, property, value.toList())
    public fun append(property: String, value: Array<String>): Identify = setUserProperty(Operation.APPEND, property, value.toList())

    // ========================================
    // PREPEND Operations (15 overloads)
    // ========================================

    public fun prepend(property: String, value: Boolean): Identify = setUserProperty(Operation.PREPEND, property, value)
    public fun prepend(property: String, value: Double): Identify = setUserProperty(Operation.PREPEND, property, value)
    public fun prepend(property: String, value: Float): Identify = setUserProperty(Operation.PREPEND, property, value)
    public fun prepend(property: String, value: Int): Identify = setUserProperty(Operation.PREPEND, property, value)
    public fun prepend(property: String, value: Long): Identify = setUserProperty(Operation.PREPEND, property, value)
    public fun prepend(property: String, value: String): Identify = setUserProperty(Operation.PREPEND, property, value)
    public fun prepend(property: String, value: Map<String, Any>): Identify = setUserProperty(Operation.PREPEND, property, value)
    public fun prepend(property: String, value: List<Any>): Identify = setUserProperty(Operation.PREPEND, property, value)
    public fun prepend(property: String, value: Array<Boolean>): Identify = setUserProperty(Operation.PREPEND, property, value.toList())
    public fun prepend(property: String, value: Array<Double>): Identify = setUserProperty(Operation.PREPEND, property, value.toList())
    public fun prepend(property: String, value: Array<Float>): Identify = setUserProperty(Operation.PREPEND, property, value.toList())
    public fun prepend(property: String, value: Array<Int>): Identify = setUserProperty(Operation.PREPEND, property, value.toList())
    public fun prepend(property: String, value: Array<Long>): Identify = setUserProperty(Operation.PREPEND, property, value.toList())
    public fun prepend(property: String, value: Array<String>): Identify = setUserProperty(Operation.PREPEND, property, value.toList())

    // ========================================
    // PRE_INSERT Operations (15 overloads)
    // ========================================

    public fun preInsert(property: String, value: Boolean): Identify = setUserProperty(Operation.PRE_INSERT, property, value)
    public fun preInsert(property: String, value: Double): Identify = setUserProperty(Operation.PRE_INSERT, property, value)
    public fun preInsert(property: String, value: Float): Identify = setUserProperty(Operation.PRE_INSERT, property, value)
    public fun preInsert(property: String, value: Int): Identify = setUserProperty(Operation.PRE_INSERT, property, value)
    public fun preInsert(property: String, value: Long): Identify = setUserProperty(Operation.PRE_INSERT, property, value)
    public fun preInsert(property: String, value: String): Identify = setUserProperty(Operation.PRE_INSERT, property, value)
    public fun preInsert(property: String, value: Map<String, Any>): Identify = setUserProperty(Operation.PRE_INSERT, property, value)
    public fun preInsert(property: String, value: List<Any>): Identify = setUserProperty(Operation.PRE_INSERT, property, value)
    public fun preInsert(property: String, value: Array<Boolean>): Identify = setUserProperty(Operation.PRE_INSERT, property, value.toList())
    public fun preInsert(property: String, value: Array<Double>): Identify = setUserProperty(Operation.PRE_INSERT, property, value.toList())
    public fun preInsert(property: String, value: Array<Float>): Identify = setUserProperty(Operation.PRE_INSERT, property, value.toList())
    public fun preInsert(property: String, value: Array<Int>): Identify = setUserProperty(Operation.PRE_INSERT, property, value.toList())
    public fun preInsert(property: String, value: Array<Long>): Identify = setUserProperty(Operation.PRE_INSERT, property, value.toList())
    public fun preInsert(property: String, value: Array<String>): Identify = setUserProperty(Operation.PRE_INSERT, property, value.toList())

    // ========================================
    // POST_INSERT Operations (15 overloads)
    // ========================================

    public fun postInsert(property: String, value: Boolean): Identify = setUserProperty(Operation.POST_INSERT, property, value)
    public fun postInsert(property: String, value: Double): Identify = setUserProperty(Operation.POST_INSERT, property, value)
    public fun postInsert(property: String, value: Float): Identify = setUserProperty(Operation.POST_INSERT, property, value)
    public fun postInsert(property: String, value: Int): Identify = setUserProperty(Operation.POST_INSERT, property, value)
    public fun postInsert(property: String, value: Long): Identify = setUserProperty(Operation.POST_INSERT, property, value)
    public fun postInsert(property: String, value: String): Identify = setUserProperty(Operation.POST_INSERT, property, value)
    public fun postInsert(property: String, value: Map<String, Any>): Identify = setUserProperty(Operation.POST_INSERT, property, value)
    public fun postInsert(property: String, value: List<Any>): Identify = setUserProperty(Operation.POST_INSERT, property, value)
    public fun postInsert(property: String, value: Array<Boolean>): Identify = setUserProperty(Operation.POST_INSERT, property, value.toList())
    public fun postInsert(property: String, value: Array<Double>): Identify = setUserProperty(Operation.POST_INSERT, property, value.toList())
    public fun postInsert(property: String, value: Array<Float>): Identify = setUserProperty(Operation.POST_INSERT, property, value.toList())
    public fun postInsert(property: String, value: Array<Int>): Identify = setUserProperty(Operation.POST_INSERT, property, value.toList())
    public fun postInsert(property: String, value: Array<Long>): Identify = setUserProperty(Operation.POST_INSERT, property, value.toList())
    public fun postInsert(property: String, value: Array<String>): Identify = setUserProperty(Operation.POST_INSERT, property, value.toList())

    // ========================================
    // REMOVE Operations (15 overloads)
    // ========================================

    public fun remove(property: String, value: Boolean): Identify = setUserProperty(Operation.REMOVE, property, value)
    public fun remove(property: String, value: Double): Identify = setUserProperty(Operation.REMOVE, property, value)
    public fun remove(property: String, value: Float): Identify = setUserProperty(Operation.REMOVE, property, value)
    public fun remove(property: String, value: Int): Identify = setUserProperty(Operation.REMOVE, property, value)
    public fun remove(property: String, value: Long): Identify = setUserProperty(Operation.REMOVE, property, value)
    public fun remove(property: String, value: String): Identify = setUserProperty(Operation.REMOVE, property, value)
    public fun remove(property: String, value: Map<String, Any>): Identify = setUserProperty(Operation.REMOVE, property, value)
    public fun remove(property: String, value: List<Any>): Identify = setUserProperty(Operation.REMOVE, property, value)
    public fun remove(property: String, value: Array<Boolean>): Identify = setUserProperty(Operation.REMOVE, property, value.toList())
    public fun remove(property: String, value: Array<Double>): Identify = setUserProperty(Operation.REMOVE, property, value.toList())
    public fun remove(property: String, value: Array<Float>): Identify = setUserProperty(Operation.REMOVE, property, value.toList())
    public fun remove(property: String, value: Array<Int>): Identify = setUserProperty(Operation.REMOVE, property, value.toList())
    public fun remove(property: String, value: Array<Long>): Identify = setUserProperty(Operation.REMOVE, property, value.toList())
    public fun remove(property: String, value: Array<String>): Identify = setUserProperty(Operation.REMOVE, property, value.toList())

    // ========================================
    // UNSET Operations
    // ========================================

    public fun unset(property: String): Identify = setUserProperty(Operation.UNSET, property, UNSET_VALUE)

    // ========================================
    // CLEAR_ALL Operation
    // ========================================

    public fun clearAll(): Identify = lock.withLock {
        operations.clear()
        propertySet.clear()
        operations[Operation.CLEAR_ALL.value] = mutableMapOf(UNSET_VALUE to UNSET_VALUE)
        this
    }

    // ========================================
    // Internal Methods
    // ========================================

    private fun setUserProperty(operation: Operation, property: String, value: Any?): Identify = lock.withLock {
        if (operation == Operation.CLEAR_ALL) {
            // ClearAll is special - handled separately
            return@withLock this
        }

        // Validation: Check for empty property name
        if (property.isEmpty()) {
            println("WARNING: Attempting to perform operation ${operation.value} with a null or empty string property, ignoring")
            return@withLock this
        }

        // Validation: Check for null value
        if (value == null) {
            println("WARNING: Attempting to perform operation ${operation.value} with null value for property $property, ignoring")
            return@withLock this
        }

        // Validation: Check that clearAll wasn't already used in this Identify
        if (operations.containsKey(Operation.CLEAR_ALL.value)) {
            println("WARNING: This Identify already contains a \$clearAll operation, ignoring operation ${operation.value}")
            return@withLock this
        }

        // Validation: Check if property already used in previous operation
        if (propertySet.contains(property)) {
            println("WARNING: Already used property $property in previous operation, ignoring operation ${operation.value}")
            return@withLock this
        }

        val operationMap = operations.getOrPut(operation.value) { mutableMapOf() }
        operationMap[property] = value
        propertySet.add(property)
        this
    }

    /**
     * Get a deep copy of operations for thread-safe access
     */
    public fun getOperations(): Map<String, Map<String, Any?>> = lock.withLock {
        operations.toMap().mapValues { it.value.toMap() }
    }

    /**
     * Convert to IdentifyEvent
     */
    internal fun toIdentifyEvent(): IdentifyEvent {
        return IdentifyEvent().apply {
            this@apply.userProperties = getOperations().toMutableMap()
        }
    }

    public companion object {
        internal const val UNSET_VALUE = "-"
    }
}
