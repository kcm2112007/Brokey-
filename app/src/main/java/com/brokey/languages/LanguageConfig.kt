package com.brokey.languages

/**
 * Status of language implementation in BROKEY.
 * Follows the core requirement: Maintain a truthful language implementation status tracker.
 */
enum class ImplementationStatus {
    PRODUCTION, // Fully functional native layout + transliteration + dictionary
    BETA,       // Native layout + transliteration engine rule parser
    ALPHA,      // Native script layout configured, basic transliteration
    ROADMAP     // Script specifications drafted, scheduled for upcoming release
}

enum class ScriptType(val displayName: String, val isRtl: Boolean = false) {
    LATIN("Latin (English)"),
    DEVANAGARI("Devanagari (देवनागरी)"),
    BENGALI_ASSAMESE("Eastern Nagari (বাংলা-অসমীয়া)"),
    GURMUKHI("Gurmukhi (ਗੁਰਮੁਖੀ)"),
    GUJARATI("Gujarati (ગુજરાતી)"),
    ODIA("Odia (ଓଡ଼ିଆ)"),
    TAMIL("Tamil (தமிழ்)"),
    TELUGU("Telugu (తెలుగు)"),
    KANNADA("Kannada (ಕನ್ನಡ)"),
    MALAYALAM("Malayalam (മലയാളം)"),
    PERSO_ARABIC("Perso-Arabic (اردو / نستعلیق)", isRtl = true),
    MEETEI_MAYEK("Meetei Mayek (ꯃꯤꯇꯩ ꯃꯌꯦꯛ)"),
    OL_CHIKI("Ol Chiki (ᱚᱞ ᱪᱤᱠᱤ)")
}

data class LanguageConfig(
    val code: String,
    val englishName: String,
    val nativeName: String,
    val scriptType: ScriptType,
    val status: ImplementationStatus,
    val supportsTransliteration: Boolean = true,
    val supportsNativeLayout: Boolean = true,
    val sampleGreeting: String,
    val defaultTransliterationSample: String = "namaste"
)
