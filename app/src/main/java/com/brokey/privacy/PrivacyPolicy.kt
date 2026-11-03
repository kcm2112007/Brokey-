package com.brokey.privacy

/**
 * Transparent, legally compliant privacy architecture pledge for BROKEY.
 */
object PrivacyPolicy {
    const val PLEDGE_TITLE = "BROKEY Zero-Telemetry Privacy Commitment"
    
    val COMMITMENTS = listOf(
        "100% Offline Core: BROKEY operates without requiring Android INTERNET permission for typing.",
        "Zero Keystroke Logging: We do not log, buffer, store, or transmit what you type, ever.",
        "Password & Sensitive Field Immunity: In password, PIN, and sensitive text fields, transliteration, learning, and clipboard suggestions are automatically disabled.",
        "Local-Only Transliteration: All 22 Indian language phonetic rules and dictionaries are bundled locally on your device.",
        "Protected Clipboard: Clipboard history is transient and can be permanently wiped with a single tap.",
        "No Ad Trackers or Telemetry SDKs: The application contains zero third-party analytics trackers."
    )

    fun getSummaryText(): String = COMMITMENTS.joinToString("\n\n")
}
