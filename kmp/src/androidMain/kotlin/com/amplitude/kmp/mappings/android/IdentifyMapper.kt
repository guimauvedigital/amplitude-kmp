package com.amplitude.kmp.mappings.android

import com.amplitude.core.events.Identify as AndroidIdentify
import com.amplitude.kmp.events.Identify

/**
 * Map KMP Identify to Android Identify.
 *
 * Converts all identify operations from KMP format to Android SDK format.
 */
internal fun Identify.toAndroidIdentify(): AndroidIdentify {
    val androidIdentify = AndroidIdentify()
    val operations = this.getOperations()

    operations.forEach { (operation, properties) ->
        when (operation) {
            "\$set" -> properties.forEach { (key, value) ->
                // Validation ensures value is non-null
                value?.let {
                    when (it) {
                        is Boolean -> androidIdentify.set(key, it)
                        is Double -> androidIdentify.set(key, it)
                        is Float -> androidIdentify.set(key, it)
                        is Int -> androidIdentify.set(key, it)
                        is Long -> androidIdentify.set(key, it)
                        is String -> androidIdentify.set(key, it)
                        is Map<*, *> -> androidIdentify.set(key, it as Map<String, Any>)
                        is List<*> -> androidIdentify.set(key, it as List<Any>)
                        else -> androidIdentify.set(key, it as Any)
                    }
                }
            }

            "\$setOnce" -> properties.forEach { (key, value) ->
                // Validation ensures value is non-null
                value?.let {
                    when (it) {
                        is Boolean -> androidIdentify.setOnce(key, it)
                        is Double -> androidIdentify.setOnce(key, it)
                        is Float -> androidIdentify.setOnce(key, it)
                        is Int -> androidIdentify.setOnce(key, it)
                        is Long -> androidIdentify.setOnce(key, it)
                        is String -> androidIdentify.setOnce(key, it)
                        is Map<*, *> -> androidIdentify.setOnce(key, it as Map<String, Any>)
                        is List<*> -> androidIdentify.setOnce(key, it as List<Any>)
                        else -> androidIdentify.setOnce(key, it as Any)
                    }
                }
            }

            "\$add" -> properties.forEach { (key, value) ->
                when (value) {
                    is Double -> androidIdentify.add(key, value)
                    is Float -> androidIdentify.add(key, value)
                    is Int -> androidIdentify.add(key, value)
                    is Long -> androidIdentify.add(key, value)
                }
            }

            "\$append" -> properties.forEach { (key, value) ->
                value?.let {
                    when (it) {
                        is Boolean -> androidIdentify.append(key, it)
                        is Double -> androidIdentify.append(key, it)
                        is Float -> androidIdentify.append(key, it)
                        is Int -> androidIdentify.append(key, it)
                        is Long -> androidIdentify.append(key, it)
                        is String -> androidIdentify.append(key, it)
                        is Map<*, *> -> androidIdentify.append(key, it as Map<String, Any>)
                        is List<*> -> androidIdentify.append(key, it as List<Any>)
                    }
                }
            }

            "\$prepend" -> properties.forEach { (key, value) ->
                value?.let {
                    when (it) {
                        is Boolean -> androidIdentify.prepend(key, it)
                        is Double -> androidIdentify.prepend(key, it)
                        is Float -> androidIdentify.prepend(key, it)
                        is Int -> androidIdentify.prepend(key, it)
                        is Long -> androidIdentify.prepend(key, it)
                        is String -> androidIdentify.prepend(key, it)
                        is Map<*, *> -> androidIdentify.prepend(key, it as Map<String, Any>)
                        is List<*> -> androidIdentify.prepend(key, it as List<Any>)
                    }
                }
            }

            "\$preInsert" -> properties.forEach { (key, value) ->
                value?.let {
                    when (it) {
                        is Boolean -> androidIdentify.preInsert(key, it)
                        is Double -> androidIdentify.preInsert(key, it)
                        is Float -> androidIdentify.preInsert(key, it)
                        is Int -> androidIdentify.preInsert(key, it)
                        is Long -> androidIdentify.preInsert(key, it)
                        is String -> androidIdentify.preInsert(key, it)
                        is Map<*, *> -> androidIdentify.preInsert(key, it as Map<String, Any>)
                        is List<*> -> androidIdentify.preInsert(key, it as List<Any>)
                    }
                }
            }

            "\$postInsert" -> properties.forEach { (key, value) ->
                value?.let {
                    when (it) {
                        is Boolean -> androidIdentify.postInsert(key, it)
                        is Double -> androidIdentify.postInsert(key, it)
                        is Float -> androidIdentify.postInsert(key, it)
                        is Int -> androidIdentify.postInsert(key, it)
                        is Long -> androidIdentify.postInsert(key, it)
                        is String -> androidIdentify.postInsert(key, it)
                        is Map<*, *> -> androidIdentify.postInsert(key, it as Map<String, Any>)
                        is List<*> -> androidIdentify.postInsert(key, it as List<Any>)
                    }
                }
            }

            "\$remove" -> properties.forEach { (key, value) ->
                value?.let {
                    when (it) {
                        is Boolean -> androidIdentify.remove(key, it)
                        is Double -> androidIdentify.remove(key, it)
                        is Float -> androidIdentify.remove(key, it)
                        is Int -> androidIdentify.remove(key, it)
                        is Long -> androidIdentify.remove(key, it)
                        is String -> androidIdentify.remove(key, it)
                        is Map<*, *> -> androidIdentify.remove(key, it as Map<String, Any>)
                        is List<*> -> androidIdentify.remove(key, it as List<Any>)
                    }
                }
            }

            "\$unset" -> properties.forEach { (key, _) ->
                androidIdentify.unset(key)
            }

            "\$clearAll" -> androidIdentify.clearAll()
        }
    }

    return androidIdentify
}
