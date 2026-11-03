package com.brokey.dictionaries

/**
 * High-frequency offline vocabulary lexicons for all major Indian language groups (Phase 3).
 * Zero-latency local candidate lookup across Dravidian, Western, Northern, Eastern, and Perso-Arabic scripts.
 */
object IndicDictionary {

    val HINDI_LEXICON: Map<String, List<String>> = mapOf(
        "namaste" to listOf("नमस्ते", "नमस्ते!", "नमस्कार"),
        "namaskar" to listOf("नमस्कार", "नमस्कार।", "नमस्ते"),
        "pranam" to listOf("प्रणाम", "चरण स्पर्श"),
        "shukriya" to listOf("शुक्रिया", "शुक्रिया!"),
        "dhanyawad" to listOf("धन्यवाद", "धन्यवाद!"),
        "dhanyavad" to listOf("धन्यवाद", "धन्यवाद!"),
        "swagat" to listOf("स्वागत", "स्वागतम्"),
        "kaise" to listOf("कैसे", "कैसी", "कैसा"),
        "hai" to listOf("है", "हैं"),
        "ho" to listOf("हो", "होंगे"),
        "bharat" to listOf("भारत", "भारतीय"),
        "shubh" to listOf("शुभ", "शुभकामनाएं"),
        "dost" to listOf("दोस्त", "मित्र"),
        "pyar" to listOf("प्यार", "प्रेम")
    )

    val BENGALI_LEXICON: Map<String, List<String>> = mapOf(
        "namaskar" to listOf("নমস্কার", "নমস্কার!"),
        "namaste" to listOf("নমস্কার", "নমস্তে"),
        "dhonnobad" to listOf("ধন্যবাদ", "ধন্যবাদ!"),
        "shagoto" to listOf("স্বাগতম", "স্বাগত"),
        "bhalo" to listOf("ভালো", "ভাল"),
        "kemon" to listOf("কেমন", "কেমন আছেন"),
        "achen" to listOf("আছেন", "আছেন?"),
        "bangla" to listOf("বাংলা", "বাঙালি"),
        "bharat" to listOf("ভারত", "ভারতীয়"),
        "shubho" to listOf("শুভ", "শুভকামনা"),
        "bondhu" to listOf("বন্ধু", "সখা"),
        "kolkata" to listOf("কলকাতা")
    )

    val MARATHI_LEXICON: Map<String, List<String>> = mapOf(
        "namaskar" to listOf("नमस्कार", "नमस्कार!"),
        "dhanyavad" to listOf("धन्यवाद", "धन्यवाद!"),
        "kase" to listOf("कसे", "कसे आहात"),
        "ahat" to listOf("आहात", "आहात?"),
        "chhan" to listOf("छान", "सुंदर"),
        "marathi" to listOf("मराठी", "महाराष्ट्र"),
        "maharashtra" to listOf("महाराष्ट्र"),
        "shubh" to listOf("शुभ", "शुभ प्रभात"),
        "mitra" to listOf("मित्र", "दोस्त"),
        "aapan" to listOf("आपण", "तुम्ही"),
        "krupaya" to listOf("कृपया")
    )

    val TAMIL_LEXICON: Map<String, List<String>> = mapOf(
        "vanakkam" to listOf("வணக்கம்", "வணக்கம்!"),
        "nandri" to listOf("நன்றி", "மிக்க நன்றி!"),
        "eppadi" to listOf("எப்படி", "எப்படி இருக்கீங்க"),
        "irukkinga" to listOf("இருக்கீங்க", "இருக்கிறீர்கள்"),
        "tamil" to listOf("தமிழ்", "தமிழ்நாடு"),
        "anbu" to listOf("அன்பு"),
        "nanba" to listOf("நண்பா", "நண்பன்"),
        "azhagu" to listOf("அழகு"),
        "nalam" to listOf("நலம்", "நலமா"),
        "kaalai" to listOf("காலை", "காலை வணக்கம்")
    )

    val TELUGU_LEXICON: Map<String, List<String>> = mapOf(
        "namaskaram" to listOf("నమస్కారం", "నమస్కారం!"),
        "dhanyavadalu" to listOf("ధన్యవాదాలు", "కృతజ్ఞతలు"),
        "ela" to listOf("ఎలా", "ఎలా ఉన్నారు"),
        "unnaru" to listOf("ఉన్నారు", "ఉన్నారా?"),
        "telugu" to listOf("తెలుగు", "ఆంధ్రప్రదేశ్", "తెలంగాణ"),
        "bagunara" to listOf("బాగున్నారా", "బాగున్నాను"),
        "sneham" to listOf("స్నేహం", "మిత్రుడు"),
        "prema" to listOf("ప్రేమ"),
        "shubhodhayam" to listOf("శుభోదయం"),
        "andaru" to listOf("అందరూ")
    )

    val KANNADA_LEXICON: Map<String, List<String>> = mapOf(
        "namaskara" to listOf("ನಮಸ್ಕಾರ", "ನಮಸ್ಕಾರ!"),
        "dhanyavada" to listOf("ಧನ್ಯವಾದ", "ಧನ್ಯವಾದಗಳು!"),
        "hegiddira" to listOf("ಹೇಗಿದ್ದೀರಾ", "ಹೇಗಿದ್ದೀರಿ"),
        "kannada" to listOf("ಕನ್ನಡ", "ಕರ್ನಾಟಕ"),
        "karnataka" to listOf("ಕರ್ನಾಟಕ"),
        "chennagiddira" to listOf("ಚೆನ್ನಾಗಿದ್ದೀರಾ"),
        "shubhadina" to listOf("ಶುಭದಿನ"),
        "sneha" to listOf("ಸ್ನೇಹ", "ಗೆಳೆಯ"),
        "preeti" to listOf("ಪ್ರೀತಿ")
    )

    val MALAYALAM_LEXICON: Map<String, List<String>> = mapOf(
        "namaskaram" to listOf("നമസ്കാരം", "നമസ്കാരം!"),
        "nandi" to listOf("നന്ദി", "വളരെ നന്ദി!"),
        "sukhamano" to listOf("സുഖമാണോ", "സുഖമാണോ?"),
        "malayalam" to listOf("മലയാളം", "കേരളം"),
        "keralam" to listOf("കേരളം"),
        "sneham" to listOf("സ്നേഹം"),
        "suprabhatham" to listOf("സുപ്രഭാതം"),
        "shubhadinam" to listOf("ശുഭദിനം"),
        "koottukaran" to listOf("കൂട്ടുകാരൻ")
    )

    val GUJARATI_LEXICON: Map<String, List<String>> = mapOf(
        "namaste" to listOf("નમસ્તે", "નમસ્તે!"),
        "kem cho" to listOf("કેમ છો", "કેમ છો?"),
        "aabhar" to listOf("આભાર", "ધન્યવાદ"),
        "gujarati" to listOf("ગુજરાતી", "ગુજરાત"),
        "majama" to listOf("મજામાં", "મજામાં છું"),
        "shubh" to listOf("શુભ", "શુભ સવાર"),
        "dost" to listOf("દોસ્ત", "મિત્ર"),
        "prem" to listOf("પ્રેમ")
    )

    val PUNJABI_LEXICON: Map<String, List<String>> = mapOf(
        "sat sri akal" to listOf("ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ", "ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ!"),
        "dhanwad" to listOf("ਧੰਨਵਾਦ", "ਬਹੁਤ ਧੰਨਵਾਦ!"),
        "ki haal hai" to listOf("ਕੀ ਹਾਲ ਹੈ", "ਕੀ ਹਾਲ ਹੈ?"),
        "punjabi" to listOf("ਪੰਜਾਬੀ", "ਪੰਜਾਬ"),
        "punjab" to listOf("ਪੰਜਾਬ"),
        "chardi kala" to listOf("ਚੜ੍ਹਦੀ ਕਲਾ"),
        "pyar" to listOf("ਪਿਆਰ"),
        "yaar" to listOf("ਯਾਰ", "ਦੋਸਤ")
    )

    val ODIA_LEXICON: Map<String, List<String>> = mapOf(
        "namaskara" to listOf("ନମସ୍କାର", "ନମସ୍କାର!"),
        "dhanyabada" to listOf("ଧନ୍ୟବାଦ", "ଧନ୍ୟବାଦ!"),
        "kemiti achhanti" to listOf("କେମିତି ଅଛନ୍ତି", "କେମିତି ଅଛନ୍ତି?"),
        "odia" to listOf("ଓଡ଼ିଆ", "ଓଡ଼ିଶା"),
        "odisha" to listOf("ଓଡ଼ିଶା"),
        "bhala" to listOf("ଭଲ", "ଭଲ ଅଛି"),
        "shubha" to listOf("ଶୁଭ", "ଶୁଭ ସକାଳ")
    )

    val URDU_LEXICON: Map<String, List<String>> = mapOf(
        "salaam" to listOf("سلام", "السلام علیکم"),
        "shukriya" to listOf("شکریہ", "بہت شکریہ!"),
        "adaab" to listOf("آداب", "آداب عرض ہے"),
        "kaise hain" to listOf("کیسے ہیں", "کیسے ہیں آپ"),
        "khuda hafiz" to listOf("خدا حافظ"),
        "urdu" to listOf("اردو"),
        "dost" to listOf("دوست", "یار"),
        "mohabbat" to listOf("محبت", "پیار")
    )
}
