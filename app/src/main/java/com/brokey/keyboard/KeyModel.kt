package com.brokey.keyboard

/**
 * Model defining an individual key on the BROKEY keyboard.
 */
data class KeyModel(
    val code: Int,
    val primaryLabel: String,
    val shiftedLabel: String? = null,
    val secondaryLabel: String? = null,
    val longPressAlternatives: List<String> = emptyList(),
    val weight: Float = 1.0f,
    val type: KeyType = KeyType.CHARACTER
) {
    enum class KeyType {
        CHARACTER,
        SHIFT,
        BACKSPACE,
        ENTER,
        SPACE,
        MODE_CHANGE,     // ?123 or ABC
        LANGUAGE_SWITCH, // Globe icon
        EMOJI,
        NAV_PREV,
        NAV_NEXT
    }

    companion object {
        const val CODE_SHIFT = -1
        const val CODE_BACKSPACE = -2
        const val CODE_ENTER = -3
        const val CODE_SPACE = 32
        const val CODE_MODE_NUMBERS = -4
        const val CODE_MODE_LETTERS = -5
        const val CODE_MODE_SYMBOLS = -6
        const val CODE_LANGUAGE_SWITCH = -7
        const val CODE_EMOJI = -8
    }
}
