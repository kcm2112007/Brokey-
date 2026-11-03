package com.brokey.themes

data class KeyboardTheme(
    val id: String,
    val name: String,
    val description: String,
    val isPremium: Boolean = false,
    val backgroundHex: String,
    val surfaceHex: String,
    val keyNormalHex: String,
    val keyActionHex: String,
    val keyPressedHex: String,
    val textPrimaryHex: String,
    val textSecondaryHex: String,
    val accentHex: String
)

object ThemeManager {
    val THEMES: List<KeyboardTheme> = listOf(
        KeyboardTheme(
            id = "charcoal_dark",
            name = "Charcoal Dark (Default)",
            description = "Ergonomic deep charcoal design optimized for low-light typing",
            isPremium = false,
            backgroundHex = "#16181C",
            surfaceHex = "#20242A",
            keyNormalHex = "#2B3038",
            keyActionHex = "#373E48",
            keyPressedHex = "#444C59",
            textPrimaryHex = "#F8FAFC",
            textSecondaryHex = "#94A3B8",
            accentHex = "#FF9933"
        ),
        KeyboardTheme(
            id = "classic_light",
            name = "Classic Light",
            description = "Clean, high-contrast crisp daytime layout",
            isPremium = false,
            backgroundHex = "#ECEFF1",
            surfaceHex = "#FFFFFF",
            keyNormalHex = "#FFFFFF",
            keyActionHex = "#D7DDE3",
            keyPressedHex = "#CBD5E1",
            textPrimaryHex = "#0F172A",
            textSecondaryHex = "#64748B",
            accentHex = "#D97706"
        ),
        KeyboardTheme(
            id = "deep_amoled",
            name = "Pitch Black AMOLED",
            description = "True #000000 black surface engineered for OLED battery conservation",
            isPremium = false,
            backgroundHex = "#000000",
            surfaceHex = "#121212",
            keyNormalHex = "#1A1A1A",
            keyActionHex = "#262626",
            keyPressedHex = "#333333",
            textPrimaryHex = "#FFFFFF",
            textSecondaryHex = "#71717A",
            accentHex = "#38BDF8"
        ),
        KeyboardTheme(
            id = "royal_saffron",
            name = "Royal Saffron (Bharat)",
            description = "Celebratory warm Indian saffron gold and deep terracotta hues",
            isPremium = false,
            backgroundHex = "#1A120B",
            surfaceHex = "#2B1E12",
            keyNormalHex = "#3C2A1A",
            keyActionHex = "#4D3622",
            keyPressedHex = "#63472C",
            textPrimaryHex = "#FFFBEB",
            textSecondaryHex = "#D6BCA7",
            accentHex = "#F59E0B"
        ),
        KeyboardTheme(
            id = "emerald_forest",
            name = "Emerald Forest",
            description = "Soothing deep pine and jade tones inspired by Western Ghats",
            isPremium = false,
            backgroundHex = "#0B1A14",
            surfaceHex = "#122A20",
            keyNormalHex = "#1C3C2E",
            keyActionHex = "#264F3D",
            keyPressedHex = "#32664E",
            textPrimaryHex = "#ECFDF5",
            textSecondaryHex = "#8CCBB1",
            accentHex = "#10B981"
        ),
        KeyboardTheme(
            id = "cyberpunk_neon",
            name = "Cyberpunk Neon",
            description = "Vibrant electric cyan and magenta accents on pitch dark slate",
            isPremium = true,
            backgroundHex = "#0F0C20",
            surfaceHex = "#1B163B",
            keyNormalHex = "#261F54",
            keyActionHex = "#362C75",
            keyPressedHex = "#4B3D9E",
            textPrimaryHex = "#FDF4FF",
            textSecondaryHex = "#C084FC",
            accentHex = "#06B6D4"
        ),
        KeyboardTheme(
            id = "terracotta_sunset",
            name = "Terracotta Sunset",
            description = "Earthy red-clay and dusty rose palettes of Rajasthan architecture",
            isPremium = true,
            backgroundHex = "#1F1111",
            surfaceHex = "#2F1B1B",
            keyNormalHex = "#452828",
            keyActionHex = "#5C3636",
            keyPressedHex = "#734545",
            textPrimaryHex = "#FFF1F2",
            textSecondaryHex = "#FDA4AF",
            accentHex = "#FB7185"
        ),
        KeyboardTheme(
            id = "midnight_indigo",
            name = "Midnight Indigo",
            description = "Deep celestial sapphire with silver accents",
            isPremium = true,
            backgroundHex = "#0B1120",
            surfaceHex = "#111C35",
            keyNormalHex = "#1E2E54",
            keyActionHex = "#2A3F70",
            keyPressedHex = "#39528F",
            textPrimaryHex = "#F0F9FF",
            textSecondaryHex = "#93C5FD",
            accentHex = "#6366F1"
        )
    )

    fun getThemeById(id: String): KeyboardTheme {
        return THEMES.find { it.id == id } ?: THEMES[0]
    }
}
