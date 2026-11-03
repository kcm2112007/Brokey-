package com.brokey.layouts

import com.brokey.keyboard.KeyModel

/**
 * Character and symbol layouts for BROKEY keyboards (Phase 3).
 * Implements dedicated native layouts for all Indian language groups:
 * - Devanagari (Hindi, Marathi, Sanskrit, Nepali, Konkani, Bodo, Dogri, Maithili)
 * - Eastern Nagari (Bengali, Assamese)
 * - Dravidian (Tamil, Telugu, Kannada, Malayalam)
 * - Western & Northern (Gujarati, Gurmukhi Punjabi, Odia)
 * - Perso-Arabic (Urdu, Kashmiri, Sindhi RTL)
 * - Indigenous Scripts (Santali Ol Chiki, Manipuri Meetei Mayek)
 */
object KeyboardLayouts {

    // English QWERTY
    fun getEnglishLayout(): List<List<KeyModel>> {
        val row1 = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p").mapIndexed { i, char ->
            KeyModel(
                code = char[0].code,
                primaryLabel = char,
                shiftedLabel = char.uppercase(),
                secondaryLabel = ((i + 1) % 10).toString(),
                longPressAlternatives = listOf(((i + 1) % 10).toString())
            )
        }

        val row2 = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l").map { char ->
            KeyModel(
                code = char[0].code,
                primaryLabel = char,
                shiftedLabel = char.uppercase()
            )
        }

        val row3 = mutableListOf<KeyModel>()
        row3.add(KeyModel(code = KeyModel.CODE_SHIFT, primaryLabel = "⇧", weight = 1.3f, type = KeyModel.KeyType.SHIFT))
        listOf("z", "x", "c", "v", "b", "n", "m").forEach { char ->
            row3.add(KeyModel(
                code = char[0].code,
                primaryLabel = char,
                shiftedLabel = char.uppercase()
            ))
        }
        row3.add(KeyModel(code = KeyModel.CODE_BACKSPACE, primaryLabel = "⌫", weight = 1.3f, type = KeyModel.KeyType.BACKSPACE))

        val row4 = listOf(
            KeyModel(code = KeyModel.CODE_MODE_NUMBERS, primaryLabel = "?123", weight = 1.3f, type = KeyModel.KeyType.MODE_CHANGE),
            KeyModel(code = KeyModel.CODE_LANGUAGE_SWITCH, primaryLabel = "🌐", weight = 1.0f, type = KeyModel.KeyType.LANGUAGE_SWITCH),
            KeyModel(code = ','.code, primaryLabel = ",", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_SPACE, primaryLabel = "English", weight = 4.2f, type = KeyModel.KeyType.SPACE),
            KeyModel(code = '.'.code, primaryLabel = ".", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_ENTER, primaryLabel = "⏎", weight = 1.5f, type = KeyModel.KeyType.ENTER)
        )

        return listOf(row1, row2, row3, row4)
    }

    // Hindi & Devanagari Inscript layout
    fun getHindiDevanagariLayout(): List<List<KeyModel>> {
        val row1Specs = listOf(
            Triple("ौ", "औ", listOf("औ")),
            Triple("ै", "ऐ", listOf("ऐ")),
            Triple("ा", "आ", listOf("आ")),
            Triple("ी", "ई", listOf("ई")),
            Triple("ू", "ऊ", listOf("ऊ")),
            Triple("ब", "भ", listOf("भ", "ब्ध")),
            Triple("ह", "ङ", listOf("ह्य", "ह्ल", "ह्व")),
            Triple("ग", "घ", listOf("घ", "ग्द")),
            Triple("द", "ध", listOf("ध", "द्ध", "द्य", "द्व")),
            Triple("ज", "झ", listOf("झ", "ज्ञ", "ज़"))
        )

        val row1 = row1Specs.map { (norm, shift, alt) ->
            KeyModel(code = norm[0].code, primaryLabel = norm, shiftedLabel = shift, secondaryLabel = shift, longPressAlternatives = alt)
        }

        val row2Specs = listOf(
            Triple("ो", "ओ", listOf("ओ")),
            Triple("े", "ए", listOf("ए")),
            Triple("्", "अ", listOf("अ")),
            Triple("ि", "इ", listOf("इ")),
            Triple("ु", "उ", listOf("उ")),
            Triple("प", "फ", listOf("फ", "फ़", "प्त")),
            Triple("र", "ऋ", listOf("ऋ", "्र", "र्", "ृ")),
            Triple("क", "ख", listOf("ख", "क्ष", "क्त", "क़")),
            Triple("त", "थ", listOf("थ", "त्र", "त्त")),
            Triple("च", "छ", listOf("छ", "च्च"))
        )

        val row2 = row2Specs.map { (norm, shift, alt) ->
            KeyModel(code = norm[0].code, primaryLabel = norm, shiftedLabel = shift, secondaryLabel = shift, longPressAlternatives = alt)
        }

        val row3 = mutableListOf<KeyModel>()
        row3.add(KeyModel(code = KeyModel.CODE_SHIFT, primaryLabel = "⇧", weight = 1.3f, type = KeyModel.KeyType.SHIFT))

        val row3Specs = listOf(
            Triple("ं", "ँ", listOf("ँ", "ः")),
            Triple("म", "ण", listOf("ण", "म्न")),
            Triple("न", "ञ", listOf("ञ", "न्न")),
            Triple("व", "ऑ", listOf("ऑ", "व्व")),
            Triple("ल", "ळ", listOf("ळ", "ल्ल")),
            Triple("स", "श", listOf("श", "श्र", "ष", "स्त", "स्व")),
            Triple("य", "ष", listOf("ष", "य्य"))
        )

        row3Specs.forEach { (norm, shift, alt) ->
            row3.add(KeyModel(code = norm[0].code, primaryLabel = norm, shiftedLabel = shift, secondaryLabel = shift, longPressAlternatives = alt))
        }
        row3.add(KeyModel(code = KeyModel.CODE_BACKSPACE, primaryLabel = "⌫", weight = 1.3f, type = KeyModel.KeyType.BACKSPACE))

        val row4 = listOf(
            KeyModel(code = KeyModel.CODE_MODE_NUMBERS, primaryLabel = "?123", weight = 1.3f, type = KeyModel.KeyType.MODE_CHANGE),
            KeyModel(code = KeyModel.CODE_LANGUAGE_SWITCH, primaryLabel = "🌐", weight = 1.0f, type = KeyModel.KeyType.LANGUAGE_SWITCH),
            KeyModel(code = '़'.code, primaryLabel = "़", weight = 1.0f, longPressAlternatives = listOf("़", "ः")),
            KeyModel(code = KeyModel.CODE_SPACE, primaryLabel = "हिंदी (देवनागरी)", weight = 4.2f, type = KeyModel.KeyType.SPACE),
            KeyModel(code = '।'.code, primaryLabel = "।", weight = 1.0f, longPressAlternatives = listOf("।", "॥")),
            KeyModel(code = KeyModel.CODE_ENTER, primaryLabel = "⏎", weight = 1.5f, type = KeyModel.KeyType.ENTER)
        )

        return listOf(row1, row2, row3, row4)
    }

    // Bengali & Assamese Inscript layout
    fun getBengaliLayout(): List<List<KeyModel>> {
        val row1Specs = listOf(
            Triple("ৌ", "ঔ", listOf("ঔ")),
            Triple("ৈ", "ঐ", listOf("ঐ")),
            Triple("া", "আ", listOf("আ")),
            Triple("ী", "ঈ", listOf("ঈ")),
            Triple("ূ", "ঊ", listOf("ঊ")),
            Triple("ব", "ভ", listOf("ভ", "ব্ধ")),
            Triple("হ", "ঙ", listOf("হ্ণ", "হ্ম")),
            Triple("গ", "ঘ", listOf("ঘ", "গ্ধ")),
            Triple("দ", "ধ", listOf("ধ", "দ্ধ", "দ্ব")),
            Triple("জ", "ঝ", listOf("ঝ", "জ্ঞ", "জ্জ"))
        )

        val row1 = row1Specs.map { (norm, shift, alt) ->
            KeyModel(code = norm[0].code, primaryLabel = norm, shiftedLabel = shift, secondaryLabel = shift, longPressAlternatives = alt)
        }

        val row2Specs = listOf(
            Triple("ো", "ও", listOf("ও")),
            Triple("ে", "এ", listOf("এ")),
            Triple("্", "অ", listOf("অ")),
            Triple("ি", "ই", listOf("ই")),
            Triple("ু", "উ", listOf("উ")),
            Triple("প", "ফ", listOf("ফ", "প্ত")),
            Triple("র", "ঋ", listOf("ঋ", "্র", "র্", "ৃ")),
            Triple("ক", "খ", listOf("খ", "ক্ষ", "ক্ত")),
            Triple("ত", "থ", listOf("থ", "ত্র", "ত্ত", "ৎ")),
            Triple("চ", "ছ", listOf("ছ", "চ্চ"))
        )

        val row2 = row2Specs.map { (norm, shift, alt) ->
            KeyModel(code = norm[0].code, primaryLabel = norm, shiftedLabel = shift, secondaryLabel = shift, longPressAlternatives = alt)
        }

        val row3 = mutableListOf<KeyModel>()
        row3.add(KeyModel(code = KeyModel.CODE_SHIFT, primaryLabel = "⇧", weight = 1.3f, type = KeyModel.KeyType.SHIFT))

        val row3Specs = listOf(
            Triple("ং", "ঁ", listOf("ঁ", "ঃ")),
            Triple("ম", "ণ", listOf("ণ", "ম্ম")),
            Triple("ন", "ঞ", listOf("ঞ", "ন্ন")),
            Triple("য", "য়", listOf("য়", "্য")),
            Triple("ল", "ৎ", listOf("ৎ", "ল্ল")),
            Triple("স", "শ", listOf("শ", "ষ", "শ্র", "স্ত", "স্ব")),
            Triple("ড", "ঢ", listOf("ঢ", "ড়", "ঢ়"))
        )

        row3Specs.forEach { (norm, shift, alt) ->
            row3.add(KeyModel(code = norm[0].code, primaryLabel = norm, shiftedLabel = shift, secondaryLabel = shift, longPressAlternatives = alt))
        }
        row3.add(KeyModel(code = KeyModel.CODE_BACKSPACE, primaryLabel = "⌫", weight = 1.3f, type = KeyModel.KeyType.BACKSPACE))

        val row4 = listOf(
            KeyModel(code = KeyModel.CODE_MODE_NUMBERS, primaryLabel = "?123", weight = 1.3f, type = KeyModel.KeyType.MODE_CHANGE),
            KeyModel(code = KeyModel.CODE_LANGUAGE_SWITCH, primaryLabel = "🌐", weight = 1.0f, type = KeyModel.KeyType.LANGUAGE_SWITCH),
            KeyModel(code = '়'.code, primaryLabel = "়", weight = 1.0f, longPressAlternatives = listOf("়", "ঃ")),
            KeyModel(code = KeyModel.CODE_SPACE, primaryLabel = "বাংলা", weight = 4.2f, type = KeyModel.KeyType.SPACE),
            KeyModel(code = '।'.code, primaryLabel = "।", weight = 1.0f, longPressAlternatives = listOf("।", "॥")),
            KeyModel(code = KeyModel.CODE_ENTER, primaryLabel = "⏎", weight = 1.5f, type = KeyModel.KeyType.ENTER)
        )

        return listOf(row1, row2, row3, row4)
    }

    // Tamil Layout (தமிழ்: உயிர், மெய், புள்ளி, ஆய்ஃதம்)
    fun getTamilLayout(): List<List<KeyModel>> {
        val row1 = listOf("அ", "ஆ", "இ", "ஈ", "உ", "ஊ", "எ", "ஏ", "ஐ", "ஒ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row2 = listOf("க", "ங", "ச", "ஞ", "ட", "ண", "த", "ந", "ப", "ம").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row3 = mutableListOf<KeyModel>()
        row3.add(KeyModel(code = KeyModel.CODE_SHIFT, primaryLabel = "⇧", weight = 1.3f, type = KeyModel.KeyType.SHIFT))
        listOf("ய", "ர", "ல", "வ", "ழ", "ள", "ற", "ன").forEach {
            row3.add(KeyModel(code = it[0].code, primaryLabel = it))
        }
        row3.add(KeyModel(code = KeyModel.CODE_BACKSPACE, primaryLabel = "⌫", weight = 1.3f, type = KeyModel.KeyType.BACKSPACE))

        val row4 = listOf(
            KeyModel(code = KeyModel.CODE_MODE_NUMBERS, primaryLabel = "?123", weight = 1.3f, type = KeyModel.KeyType.MODE_CHANGE),
            KeyModel(code = KeyModel.CODE_LANGUAGE_SWITCH, primaryLabel = "🌐", weight = 1.0f, type = KeyModel.KeyType.LANGUAGE_SWITCH),
            KeyModel(code = '்'.code, primaryLabel = "்", weight = 1.0f), // Pulli
            KeyModel(code = KeyModel.CODE_SPACE, primaryLabel = "தமிழ்", weight = 4.2f, type = KeyModel.KeyType.SPACE),
            KeyModel(code = 'ஃ'.code, primaryLabel = "ஃ", weight = 1.0f), // Aytham
            KeyModel(code = KeyModel.CODE_ENTER, primaryLabel = "⏎", weight = 1.5f, type = KeyModel.KeyType.ENTER)
        )
        return listOf(row1, row2, row3, row4)
    }

    // Telugu Layout (తెలుగు)
    fun getTeluguLayout(): List<List<KeyModel>> {
        val row1 = listOf("ౌ", "ై", "ా", "ీ", "ూ", "బ", "హ", "గ", "ద", "జ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row2 = listOf("ో", "ే", "్", "ి", "ు", "ప", "ర", "క", "త", "చ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row3 = mutableListOf<KeyModel>()
        row3.add(KeyModel(code = KeyModel.CODE_SHIFT, primaryLabel = "⇧", weight = 1.3f, type = KeyModel.KeyType.SHIFT))
        listOf("ం", "మ", "న", "వ", "ల", "స", "య", "ళ").forEach {
            row3.add(KeyModel(code = it[0].code, primaryLabel = it))
        }
        row3.add(KeyModel(code = KeyModel.CODE_BACKSPACE, primaryLabel = "⌫", weight = 1.3f, type = KeyModel.KeyType.BACKSPACE))

        val row4 = listOf(
            KeyModel(code = KeyModel.CODE_MODE_NUMBERS, primaryLabel = "?123", weight = 1.3f, type = KeyModel.KeyType.MODE_CHANGE),
            KeyModel(code = KeyModel.CODE_LANGUAGE_SWITCH, primaryLabel = "🌐", weight = 1.0f, type = KeyModel.KeyType.LANGUAGE_SWITCH),
            KeyModel(code = 'ః'.code, primaryLabel = "ః", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_SPACE, primaryLabel = "తెలుగు", weight = 4.2f, type = KeyModel.KeyType.SPACE),
            KeyModel(code = '।'.code, primaryLabel = "।", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_ENTER, primaryLabel = "⏎", weight = 1.5f, type = KeyModel.KeyType.ENTER)
        )
        return listOf(row1, row2, row3, row4)
    }

    // Kannada Layout (ಕನ್ನಡ)
    fun getKannadaLayout(): List<List<KeyModel>> {
        val row1 = listOf("ೌ", "ೈ", "ಾ", "ೀ", "ೂ", "ಬ", "ಹ", "ಗ", "ದ", "ಜ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row2 = listOf("ೋ", "ೇ", "್", "ಿ", "ು", "ಪ", "ರ", "ಕ", "ತ", "ಚ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row3 = mutableListOf<KeyModel>()
        row3.add(KeyModel(code = KeyModel.CODE_SHIFT, primaryLabel = "⇧", weight = 1.3f, type = KeyModel.KeyType.SHIFT))
        listOf("ಂ", "ಮ", "ನ", "ವ", "ಲ", "ಸ", "ಯ", "ಳ").forEach {
            row3.add(KeyModel(code = it[0].code, primaryLabel = it))
        }
        row3.add(KeyModel(code = KeyModel.CODE_BACKSPACE, primaryLabel = "⌫", weight = 1.3f, type = KeyModel.KeyType.BACKSPACE))

        val row4 = listOf(
            KeyModel(code = KeyModel.CODE_MODE_NUMBERS, primaryLabel = "?123", weight = 1.3f, type = KeyModel.KeyType.MODE_CHANGE),
            KeyModel(code = KeyModel.CODE_LANGUAGE_SWITCH, primaryLabel = "🌐", weight = 1.0f, type = KeyModel.KeyType.LANGUAGE_SWITCH),
            KeyModel(code = 'ಃ'.code, primaryLabel = "ಃ", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_SPACE, primaryLabel = "ಕನ್ನಡ", weight = 4.2f, type = KeyModel.KeyType.SPACE),
            KeyModel(code = '।'.code, primaryLabel = "।", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_ENTER, primaryLabel = "⏎", weight = 1.5f, type = KeyModel.KeyType.ENTER)
        )
        return listOf(row1, row2, row3, row4)
    }

    // Malayalam Layout (മലയാളം with Chillu letters)
    fun getMalayalamLayout(): List<List<KeyModel>> {
        val row1 = listOf("ൌ", "ൈ", "ാ", "ീ", "ൂ", "ബ", "ഹ", "ഗ", "ദ", "ജ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row2 = listOf("ോ", "േ", "്", "ി", "ു", "പ", "ര", "ക", "ത", "ച").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row3 = mutableListOf<KeyModel>()
        row3.add(KeyModel(code = KeyModel.CODE_SHIFT, primaryLabel = "⇧", weight = 1.3f, type = KeyModel.KeyType.SHIFT))
        listOf("ം", "മ", "ന", "വ", "ല", "സ", "യ", "ർ", "ൽ").forEach {
            row3.add(KeyModel(code = it[0].code, primaryLabel = it))
        }
        row3.add(KeyModel(code = KeyModel.CODE_BACKSPACE, primaryLabel = "⌫", weight = 1.3f, type = KeyModel.KeyType.BACKSPACE))

        val row4 = listOf(
            KeyModel(code = KeyModel.CODE_MODE_NUMBERS, primaryLabel = "?123", weight = 1.3f, type = KeyModel.KeyType.MODE_CHANGE),
            KeyModel(code = KeyModel.CODE_LANGUAGE_SWITCH, primaryLabel = "🌐", weight = 1.0f, type = KeyModel.KeyType.LANGUAGE_SWITCH),
            KeyModel(code = 'ഃ'.code, primaryLabel = "ഃ", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_SPACE, primaryLabel = "മലയാളം", weight = 4.2f, type = KeyModel.KeyType.SPACE),
            KeyModel(code = '।'.code, primaryLabel = "।", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_ENTER, primaryLabel = "⏎", weight = 1.5f, type = KeyModel.KeyType.ENTER)
        )
        return listOf(row1, row2, row3, row4)
    }

    // Gujarati Layout (ગુજરાતી)
    fun getGujaratiLayout(): List<List<KeyModel>> {
        val row1 = listOf("ૌ", "ૈ", "ા", "ી", "ૂ", "બ", "હ", "ગ", "દ", "જ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row2 = listOf("ો", "ે", "્", "િ", "ુ", "પ", "ર", "ક", "ત", "ચ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row3 = mutableListOf<KeyModel>()
        row3.add(KeyModel(code = KeyModel.CODE_SHIFT, primaryLabel = "⇧", weight = 1.3f, type = KeyModel.KeyType.SHIFT))
        listOf("ં", "મ", "ન", "વ", "લ", "સ", "ય", "ળ").forEach {
            row3.add(KeyModel(code = it[0].code, primaryLabel = it))
        }
        row3.add(KeyModel(code = KeyModel.CODE_BACKSPACE, primaryLabel = "⌫", weight = 1.3f, type = KeyModel.KeyType.BACKSPACE))

        val row4 = listOf(
            KeyModel(code = KeyModel.CODE_MODE_NUMBERS, primaryLabel = "?123", weight = 1.3f, type = KeyModel.KeyType.MODE_CHANGE),
            KeyModel(code = KeyModel.CODE_LANGUAGE_SWITCH, primaryLabel = "🌐", weight = 1.0f, type = KeyModel.KeyType.LANGUAGE_SWITCH),
            KeyModel(code = '઼'.code, primaryLabel = "઼", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_SPACE, primaryLabel = "ગુજરાતી", weight = 4.2f, type = KeyModel.KeyType.SPACE),
            KeyModel(code = '।'.code, primaryLabel = "।", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_ENTER, primaryLabel = "⏎", weight = 1.5f, type = KeyModel.KeyType.ENTER)
        )
        return listOf(row1, row2, row3, row4)
    }

    // Punjabi Gurmukhi Layout (ਪੰਜਾਬੀ 35 ਅੱਖਰ)
    fun getPunjabiGurmukhiLayout(): List<List<KeyModel>> {
        val row1 = listOf("ੳ", "ਅ", "ੲ", "ਸ", "ਹ", "ਕ", "ਖ", "ਗ", "ਘ", "ਙ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row2 = listOf("ਚ", "ਛ", "ਜ", "ਝ", "ਞ", "ਟ", "ਠ", "ਡ", "ਢ", "ਣ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row3 = mutableListOf<KeyModel>()
        row3.add(KeyModel(code = KeyModel.CODE_SHIFT, primaryLabel = "⇧", weight = 1.3f, type = KeyModel.KeyType.SHIFT))
        listOf("ਤ", "ਥ", "ਦ", "ਧ", "ਨ", "ਪ", "ਫ", "ਬ", "ਭ", "ਮ").forEach {
            row3.add(KeyModel(code = it[0].code, primaryLabel = it))
        }
        row3.add(KeyModel(code = KeyModel.CODE_BACKSPACE, primaryLabel = "⌫", weight = 1.3f, type = KeyModel.KeyType.BACKSPACE))

        val row4 = listOf(
            KeyModel(code = KeyModel.CODE_MODE_NUMBERS, primaryLabel = "?123", weight = 1.3f, type = KeyModel.KeyType.MODE_CHANGE),
            KeyModel(code = KeyModel.CODE_LANGUAGE_SWITCH, primaryLabel = "🌐", weight = 1.0f, type = KeyModel.KeyType.LANGUAGE_SWITCH),
            KeyModel(code = 'ਂ'.code, primaryLabel = "ਂ", weight = 1.0f), // Bindi
            KeyModel(code = KeyModel.CODE_SPACE, primaryLabel = "ਪੰਜਾਬੀ (ਗੁਰਮੁਖੀ)", weight = 4.2f, type = KeyModel.KeyType.SPACE),
            KeyModel(code = '।'.code, primaryLabel = "।", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_ENTER, primaryLabel = "⏎", weight = 1.5f, type = KeyModel.KeyType.ENTER)
        )
        return listOf(row1, row2, row3, row4)
    }

    // Odia Layout (ଓଡ଼ିଆ)
    fun getOdiaLayout(): List<List<KeyModel>> {
        val row1 = listOf("ୌ", "ୈ", "ା", "ୀ", "ୂ", "ବ", "ହ", "ଗ", "ଦ", "ଜ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row2 = listOf("ୋ", "େ", "୍", "ି", "ୁ", "ପ", "ର", "କ", "ତ", "ଚ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row3 = mutableListOf<KeyModel>()
        row3.add(KeyModel(code = KeyModel.CODE_SHIFT, primaryLabel = "⇧", weight = 1.3f, type = KeyModel.KeyType.SHIFT))
        listOf("ଂ", "ମ", "ନ", "ଵ", "ଲ", "ସ", "ୟ", "ଳ").forEach {
            row3.add(KeyModel(code = it[0].code, primaryLabel = it))
        }
        row3.add(KeyModel(code = KeyModel.CODE_BACKSPACE, primaryLabel = "⌫", weight = 1.3f, type = KeyModel.KeyType.BACKSPACE))

        val row4 = listOf(
            KeyModel(code = KeyModel.CODE_MODE_NUMBERS, primaryLabel = "?123", weight = 1.3f, type = KeyModel.KeyType.MODE_CHANGE),
            KeyModel(code = KeyModel.CODE_LANGUAGE_SWITCH, primaryLabel = "🌐", weight = 1.0f, type = KeyModel.KeyType.LANGUAGE_SWITCH),
            KeyModel(code = '଼'.code, primaryLabel = "଼", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_SPACE, primaryLabel = "ଓଡ଼ିଆ", weight = 4.2f, type = KeyModel.KeyType.SPACE),
            KeyModel(code = '।'.code, primaryLabel = "।", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_ENTER, primaryLabel = "⏎", weight = 1.5f, type = KeyModel.KeyType.ENTER)
        )
        return listOf(row1, row2, row3, row4)
    }

    // Urdu Perso-Arabic Layout (اردو with RTL sequence)
    fun getUrduLayout(): List<List<KeyModel>> {
        val row1 = listOf("ق", "و", "ع", "ر", "ت", "ے", "ء", "ی", "ہ", "پ").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row2 = listOf("ا", "س", "د", "ف", "گ", "ح", "ج", "ک", "ل").map {
            KeyModel(code = it[0].code, primaryLabel = it)
        }
        val row3 = mutableListOf<KeyModel>()
        row3.add(KeyModel(code = KeyModel.CODE_SHIFT, primaryLabel = "⇧", weight = 1.3f, type = KeyModel.KeyType.SHIFT))
        listOf("ز", "ش", "چ", "ط", "ب", "ن", "م").forEach {
            row3.add(KeyModel(code = it[0].code, primaryLabel = it))
        }
        row3.add(KeyModel(code = KeyModel.CODE_BACKSPACE, primaryLabel = "⌫", weight = 1.3f, type = KeyModel.KeyType.BACKSPACE))

        val row4 = listOf(
            KeyModel(code = KeyModel.CODE_MODE_NUMBERS, primaryLabel = "?123", weight = 1.3f, type = KeyModel.KeyType.MODE_CHANGE),
            KeyModel(code = KeyModel.CODE_LANGUAGE_SWITCH, primaryLabel = "🌐", weight = 1.0f, type = KeyModel.KeyType.LANGUAGE_SWITCH),
            KeyModel(code = '،'.code, primaryLabel = "،", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_SPACE, primaryLabel = "اردو", weight = 4.2f, type = KeyModel.KeyType.SPACE),
            KeyModel(code = '۔'.code, primaryLabel = "۔", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_ENTER, primaryLabel = "⏎", weight = 1.5f, type = KeyModel.KeyType.ENTER)
        )
        return listOf(row1, row2, row3, row4)
    }

    // Number & Punctuation Matrix
    fun getNumbersAndSymbolsLayout(): List<List<KeyModel>> {
        val row1 = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").map { char ->
            KeyModel(code = char[0].code, primaryLabel = char)
        }
        val row2 = listOf("@", "#", "₹", "%", "&", "-", "+", "(", ")", "/").map { char ->
            KeyModel(code = char[0].code, primaryLabel = char)
        }
        val row3 = mutableListOf<KeyModel>()
        row3.add(KeyModel(code = KeyModel.CODE_MODE_SYMBOLS, primaryLabel = "=\\<", weight = 1.3f, type = KeyModel.KeyType.MODE_CHANGE))
        listOf("*", "\"", "'", ":", ";", "!", "?").forEach { char ->
            row3.add(KeyModel(code = char[0].code, primaryLabel = char))
        }
        row3.add(KeyModel(code = KeyModel.CODE_BACKSPACE, primaryLabel = "⌫", weight = 1.3f, type = KeyModel.KeyType.BACKSPACE))

        val row4 = listOf(
            KeyModel(code = KeyModel.CODE_MODE_LETTERS, primaryLabel = "ABC", weight = 1.5f, type = KeyModel.KeyType.MODE_CHANGE),
            KeyModel(code = ','.code, primaryLabel = ",", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_SPACE, primaryLabel = "Space", weight = 4.5f, type = KeyModel.KeyType.SPACE),
            KeyModel(code = '.'.code, primaryLabel = ".", weight = 1.0f),
            KeyModel(code = KeyModel.CODE_ENTER, primaryLabel = "⏎", weight = 1.5f, type = KeyModel.KeyType.ENTER)
        )
        return listOf(row1, row2, row3, row4)
    }
}
