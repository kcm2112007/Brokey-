package com.brokey.languages

/**
 * Registry and manager for all 22 constitutionally scheduled Indian languages + English.
 * Provides lookup, active language state, and honest implementation status tracking.
 */
object LanguageManager {

    val ENGLISH = LanguageConfig(
        code = "en",
        englishName = "English",
        nativeName = "English",
        scriptType = ScriptType.LATIN,
        status = ImplementationStatus.PRODUCTION,
        sampleGreeting = "Hello"
    )

    // The 22 Constitutionally Scheduled Indian Languages (Eighth Schedule to the Constitution of India)
    val SCHEDULED_LANGUAGES: List<LanguageConfig> = listOf(
        LanguageConfig(
            code = "hi",
            englishName = "Hindi",
            nativeName = "हिंदी",
            scriptType = ScriptType.DEVANAGARI,
            status = ImplementationStatus.PRODUCTION,
            sampleGreeting = "नमस्ते",
            defaultTransliterationSample = "namaste"
        ),
        LanguageConfig(
            code = "bn",
            englishName = "Bengali",
            nativeName = "বাংলা",
            scriptType = ScriptType.BENGALI_ASSAMESE,
            status = ImplementationStatus.PRODUCTION,
            sampleGreeting = "নমস্কার",
            defaultTransliterationSample = "namaskar"
        ),
        LanguageConfig(
            code = "mr",
            englishName = "Marathi",
            nativeName = "मराठी",
            scriptType = ScriptType.DEVANAGARI,
            status = ImplementationStatus.BETA,
            sampleGreeting = "नमस्कार",
            defaultTransliterationSample = "namaskar"
        ),
        LanguageConfig(
            code = "gu",
            englishName = "Gujarati",
            nativeName = "ગુજરાતી",
            scriptType = ScriptType.GUJARATI,
            status = ImplementationStatus.BETA,
            sampleGreeting = "નમસ્તે",
            defaultTransliterationSample = "kem cho"
        ),
        LanguageConfig(
            code = "ta",
            englishName = "Tamil",
            nativeName = "தமிழ்",
            scriptType = ScriptType.TAMIL,
            status = ImplementationStatus.BETA,
            sampleGreeting = "வணக்கம்",
            defaultTransliterationSample = "vanakkam"
        ),
        LanguageConfig(
            code = "te",
            englishName = "Telugu",
            nativeName = "తెలుగు",
            scriptType = ScriptType.TELUGU,
            status = ImplementationStatus.BETA,
            sampleGreeting = "నమస్కారం",
            defaultTransliterationSample = "namaskaram"
        ),
        LanguageConfig(
            code = "pa",
            englishName = "Punjabi",
            nativeName = "ਪੰਜਾਬੀ",
            scriptType = ScriptType.GURMUKHI,
            status = ImplementationStatus.BETA,
            sampleGreeting = "ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ",
            defaultTransliterationSample = "sat sri akal"
        ),
        LanguageConfig(
            code = "kn",
            englishName = "Kannada",
            nativeName = "ಕನ್ನಡ",
            scriptType = ScriptType.KANNADA,
            status = ImplementationStatus.BETA,
            sampleGreeting = "ನಮಸ್ಕಾರ",
            defaultTransliterationSample = "namaskara"
        ),
        LanguageConfig(
            code = "ml",
            englishName = "Malayalam",
            nativeName = "മലയാളം",
            scriptType = ScriptType.MALAYALAM,
            status = ImplementationStatus.BETA,
            sampleGreeting = "നമസ്കാരം",
            defaultTransliterationSample = "namaskaram"
        ),
        LanguageConfig(
            code = "or",
            englishName = "Odia",
            nativeName = "ଓଡ଼ିଆ",
            scriptType = ScriptType.ODIA,
            status = ImplementationStatus.BETA,
            sampleGreeting = "ନମସ୍କାର",
            defaultTransliterationSample = "namaskara"
        ),
        LanguageConfig(
            code = "ur",
            englishName = "Urdu",
            nativeName = "اردو",
            scriptType = ScriptType.PERSO_ARABIC,
            status = ImplementationStatus.BETA,
            sampleGreeting = "السلام علیکم",
            defaultTransliterationSample = "adaab"
        ),
        LanguageConfig(
            code = "as",
            englishName = "Assamese",
            nativeName = "অসমীয়া",
            scriptType = ScriptType.BENGALI_ASSAMESE,
            status = ImplementationStatus.ALPHA,
            sampleGreeting = "নমস্কাৰ",
            defaultTransliterationSample = "nomoskar"
        ),
        LanguageConfig(
            code = "sa",
            englishName = "Sanskrit",
            nativeName = "संस्कृतम्",
            scriptType = ScriptType.DEVANAGARI,
            status = ImplementationStatus.ALPHA,
            sampleGreeting = "नमो नमः",
            defaultTransliterationSample = "namo namah"
        ),
        LanguageConfig(
            code = "ne",
            englishName = "Nepali",
            nativeName = "नेपाली",
            scriptType = ScriptType.DEVANAGARI,
            status = ImplementationStatus.ALPHA,
            sampleGreeting = "नमस्ते",
            defaultTransliterationSample = "namaste"
        ),
        LanguageConfig(
            code = "kok",
            englishName = "Konkani",
            nativeName = "कोंकणी",
            scriptType = ScriptType.DEVANAGARI,
            status = ImplementationStatus.ALPHA,
            sampleGreeting = "नमस्कार",
            defaultTransliterationSample = "namaskar"
        ),
        LanguageConfig(
            code = "ks",
            englishName = "Kashmiri",
            nativeName = "कॉशुर / كٲشُر",
            scriptType = ScriptType.PERSO_ARABIC,
            status = ImplementationStatus.ALPHA,
            sampleGreeting = "سلام",
            defaultTransliterationSample = "salaam"
        ),
        LanguageConfig(
            code = "sd",
            englishName = "Sindhi",
            nativeName = "سنڌي / सिन्धी",
            scriptType = ScriptType.PERSO_ARABIC,
            status = ImplementationStatus.ALPHA,
            sampleGreeting = "سلام",
            defaultTransliterationSample = "salaam"
        ),
        LanguageConfig(
            code = "mni",
            englishName = "Manipuri (Meitei)",
            nativeName = "ꯃꯤꯇꯩꯂꯣꯟ",
            scriptType = ScriptType.MEETEI_MAYEK,
            status = ImplementationStatus.ALPHA,
            sampleGreeting = "ꯈꯨꯔꯨꯝꯖꯔꯤ",
            defaultTransliterationSample = "khurumjari"
        ),
        LanguageConfig(
            code = "brx",
            englishName = "Bodo",
            nativeName = "बड़ो",
            scriptType = ScriptType.DEVANAGARI,
            status = ImplementationStatus.ALPHA,
            sampleGreeting = "खुलुमबाय",
            defaultTransliterationSample = "khulumbai"
        ),
        LanguageConfig(
            code = "doi",
            englishName = "Dogri",
            nativeName = "डोगरी",
            scriptType = ScriptType.DEVANAGARI,
            status = ImplementationStatus.ALPHA,
            sampleGreeting = "नमस्ते",
            defaultTransliterationSample = "namaste"
        ),
        LanguageConfig(
            code = "mai",
            englishName = "Maithili",
            nativeName = "मैथिली",
            scriptType = ScriptType.DEVANAGARI,
            status = ImplementationStatus.ALPHA,
            sampleGreeting = "प्रणाम",
            defaultTransliterationSample = "pranam"
        ),
        LanguageConfig(
            code = "sat",
            englishName = "Santali",
            nativeName = "ᱥᱟᱱᱛᱟᱲᱤ",
            scriptType = ScriptType.OL_CHIKI,
            status = ImplementationStatus.ALPHA,
            sampleGreeting = "ᱡᱚᱦᱟᱨ",
            defaultTransliterationSample = "johar"
        )
    )

    val ALL_LANGUAGES: List<LanguageConfig> = listOf(ENGLISH) + SCHEDULED_LANGUAGES

    private var activeLanguageCode: String = "en"
    private var isTransliterationEnabled: Boolean = false

    fun getActiveLanguage(): LanguageConfig {
        return ALL_LANGUAGES.find { it.code == activeLanguageCode } ?: ENGLISH
    }

    fun setActiveLanguage(code: String) {
        if (ALL_LANGUAGES.any { it.code == code }) {
            activeLanguageCode = code
        }
    }

    fun isTransliterationMode(): Boolean = isTransliterationEnabled

    fun setTransliterationMode(enabled: Boolean) {
        isTransliterationEnabled = enabled
    }

    fun getByCode(code: String): LanguageConfig? {
        return ALL_LANGUAGES.find { it.code == code }
    }
}
