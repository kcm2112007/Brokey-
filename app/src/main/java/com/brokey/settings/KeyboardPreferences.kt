package com.brokey.settings

import android.content.Context
import android.content.SharedPreferences

/**
 * Local persistent settings for BROKEY keyboard.
 * Offline-only; stores haptic preferences, sound, theme, height, clipboard retention, and voice typing.
 */
class KeyboardPreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isHapticFeedbackEnabled(): Boolean = prefs.getBoolean(KEY_HAPTIC, true)
    fun setHapticFeedback(enabled: Boolean) = prefs.edit().putBoolean(KEY_HAPTIC, enabled).apply()

    fun isKeySoundEnabled(): Boolean = prefs.getBoolean(KEY_SOUND, false)
    fun setKeySound(enabled: Boolean) = prefs.edit().putBoolean(KEY_SOUND, enabled).apply()

    fun isAutoCapitalizationEnabled(): Boolean = prefs.getBoolean(KEY_AUTO_CAPS, true)
    fun setAutoCapitalization(enabled: Boolean) = prefs.edit().putBoolean(KEY_AUTO_CAPS, enabled).apply()

    fun isNumberRowEnabled(): Boolean = prefs.getBoolean(KEY_NUMBER_ROW, true)
    fun setNumberRowEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_NUMBER_ROW, enabled).apply()

    fun isClipboardEnabled(): Boolean = prefs.getBoolean(KEY_CLIPBOARD_ENABLED, true)
    fun setClipboardEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_CLIPBOARD_ENABLED, enabled).apply()

    fun isVoiceTypingEnabled(): Boolean = prefs.getBoolean(KEY_VOICE_TYPING, true)
    fun setVoiceTypingEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_VOICE_TYPING, enabled).apply()

    fun getSelectedTheme(): String = prefs.getString(KEY_THEME, "charcoal_dark") ?: "charcoal_dark"
    fun setSelectedTheme(themeId: String) = prefs.edit().putString(KEY_THEME, themeId).apply()

    fun getKeyboardHeightScale(): Float = prefs.getFloat(KEY_HEIGHT, 1.0f)
    fun setKeyboardHeightScale(scale: Float) = prefs.edit().putFloat(KEY_HEIGHT, scale).apply()

    companion object {
        private const val PREFS_NAME = "brokey_preferences"
        private const val KEY_HAPTIC = "pref_haptic"
        private const val KEY_SOUND = "pref_sound"
        private const val KEY_AUTO_CAPS = "pref_auto_caps"
        private const val KEY_NUMBER_ROW = "pref_number_row"
        private const val KEY_CLIPBOARD_ENABLED = "pref_clipboard_enabled"
        private const val KEY_VOICE_TYPING = "pref_voice_typing"
        private const val KEY_THEME = "pref_theme"
        private const val KEY_HEIGHT = "pref_height"
    }
}
