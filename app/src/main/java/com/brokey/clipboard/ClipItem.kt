package com.brokey.clipboard

/**
 * Data model for an individual clipboard entry.
 * Designed with strict privacy:
 * - isPinned: Prevents auto-clearing
 * - timestamp: Used for privacy auto-expiry (e.g. 60-second or 15-minute transient retention)
 * - isSensitive: Marked true for password-like strings to avoid long-term display
 */
data class ClipItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val isSensitive: Boolean = false
) {
    val previewText: String
        get() = if (text.length > 50) text.take(47) + "..." else text
}
