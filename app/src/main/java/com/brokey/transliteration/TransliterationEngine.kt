package com.brokey.transliteration

import com.brokey.dictionaries.IndicDictionary
import com.brokey.languages.ScriptType

/**
 * Production-grade modular transliteration engine for Indic scripts (Phase 2).
 * Converts Roman/English phonetic typing into accurate Indic Unicode sequences,
 * handling independent vowels, dependent matras, consonants, virama/halant conjuncts,
 * nuktas, anusvara, and complex ligatures (संव्युक्ताक्षर & যুক্তাক্ষর).
 */
class TransliterationEngine {

    data class Candidate(
        val word: String,
        val isExactMatch: Boolean = false,
        val frequency: Int = 100,
        val origin: String = "rule" // "dictionary", "phonetic", "roman"
    )

    /**
     * Generate transliteration candidates for a given input word.
     */
    fun getCandidates(input: String, scriptType: ScriptType): List<Candidate> {
        val cleanInput = input.trim().lowercase()
        if (cleanInput.isEmpty()) return emptyList()

        val results = mutableListOf<Candidate>()

        // 1. Check offline high-frequency dictionary first
        val dictMatches = when (scriptType) {
            ScriptType.DEVANAGARI -> IndicDictionary.HINDI_LEXICON[cleanInput] ?: IndicDictionary.MARATHI_LEXICON[cleanInput]
            ScriptType.BENGALI_ASSAMESE -> IndicDictionary.BENGALI_LEXICON[cleanInput]
            ScriptType.TAMIL -> IndicDictionary.TAMIL_LEXICON[cleanInput]
            ScriptType.TELUGU -> IndicDictionary.TELUGU_LEXICON[cleanInput]
            ScriptType.KANNADA -> IndicDictionary.KANNADA_LEXICON[cleanInput]
            ScriptType.MALAYALAM -> IndicDictionary.MALAYALAM_LEXICON[cleanInput]
            ScriptType.GUJARATI -> IndicDictionary.GUJARATI_LEXICON[cleanInput]
            ScriptType.GURMUKHI -> IndicDictionary.PUNJABI_LEXICON[cleanInput]
            ScriptType.ODIA -> IndicDictionary.ODIA_LEXICON[cleanInput]
            ScriptType.PERSO_ARABIC -> IndicDictionary.URDU_LEXICON[cleanInput]
            else -> null
        }

        if (dictMatches != null) {
            dictMatches.forEachIndexed { index, w ->
                results.add(
                    Candidate(
                        word = w,
                        isExactMatch = index == 0,
                        frequency = 1000 - index,
                        origin = "dictionary"
                    )
                )
            }
        }

        // 2. Run rule-based phonological conversion
        val phonological = phonologicalTransliterate(cleanInput, scriptType)
        if (phonological.isNotEmpty() && results.none { it.word == phonological }) {
            results.add(
                Candidate(
                    word = phonological,
                    isExactMatch = results.isEmpty(),
                    frequency = 500,
                    origin = "phonetic"
                )
            )
        }

        // 3. Prefix matching from dictionary for proactive auto-completion
        if (cleanInput.length >= 3) {
            val lexicon = when (scriptType) {
                ScriptType.DEVANAGARI -> IndicDictionary.HINDI_LEXICON
                ScriptType.BENGALI_ASSAMESE -> IndicDictionary.BENGALI_LEXICON
                else -> null
            }
            lexicon?.forEach { (key, words) ->
                if (key.startsWith(cleanInput) && key != cleanInput && results.size < 6) {
                    val candidateWord = words.firstOrNull()
                    if (candidateWord != null && results.none { it.word == candidateWord }) {
                        results.add(Candidate(word = candidateWord, isExactMatch = false, frequency = 250, origin = "dictionary"))
                    }
                }
            }
        }

        // 4. Fallback Roman entry
        results.add(Candidate(word = input, isExactMatch = false, frequency = 50, origin = "roman"))

        return results
    }

    /**
     * Core phonological transliteration parser.
     */
    fun phonologicalTransliterate(raw: String, scriptType: ScriptType): String {
        return when (scriptType) {
            ScriptType.DEVANAGARI -> transliterateDevanagari(raw)
            ScriptType.BENGALI_ASSAMESE -> transliterateBengali(raw)
            else -> transliterateDevanagari(raw)
        }
    }

    private fun transliterateDevanagari(text: String): String {
        val sb = StringBuilder()
        var i = 0
        val len = text.length

        while (i < len) {
            // 4-char cluster (e.g. shhr)
            if (i + 4 <= len && text.substring(i, i + 4) == "shhr") {
                sb.append("ष्")
                sb.append("र्")
                i += 4
                continue
            }

            // 3-char clusters (ksh, tra, gya, shr, chh, etc.)
            if (i + 3 <= len) {
                val sub = text.substring(i, i + 3)
                when (sub) {
                    "ksh" -> { sb.append("क्ष्"); i += 3; continue }
                    "tra" -> { sb.append("त्र्"); i += 3; continue }
                    "gya" -> { sb.append("ज्ञ्"); i += 3; continue }
                    "shr" -> { sb.append("श्र्"); i += 3; continue }
                    "chh" -> { sb.append("छ्"); i += 3; continue }
                    "shh" -> { sb.append("ष्"); i += 3; continue }
                    "ddh" -> { sb.append("द्ध्"); i += 3; continue }
                    "dhy" -> { sb.append("ध्य्"); i += 3; continue }
                }
            }

            // 2-char clusters (kh, gh, ch, jh, th, dh, ph, bh, sh, zh, ng, ny, aa, ee, oo, ai, au, ri)
            if (i + 2 <= len) {
                val sub = text.substring(i, i + 2)
                val matchedConsonant = when (sub) {
                    "kh" -> "ख्"
                    "gh" -> "घ्"
                    "ch" -> "च्"
                    "jh" -> "झ्"
                    "th" -> "थ्"
                    "dh" -> "ध्"
                    "ph" -> "फ्"
                    "bh" -> "भ्"
                    "sh" -> "श्"
                    "zh" -> "ज़्"
                    "ng" -> "ङ्"
                    "ny" -> "ञ्"
                    else -> null
                }
                if (matchedConsonant != null) {
                    appendDevanagariToken(sb, matchedConsonant)
                    i += 2
                    continue
                }

                // Vowel combinations
                val matchedVowel = when (sub) {
                    "aa" -> if (sb.isEmpty() || isAfterVowelOrIndependent(sb)) "आ" else "ा"
                    "ee", "ii" -> if (sb.isEmpty() || isAfterVowelOrIndependent(sb)) "ई" else "ी"
                    "oo", "uu" -> if (sb.isEmpty() || isAfterVowelOrIndependent(sb)) "ऊ" else "ू"
                    "ai" -> if (sb.isEmpty() || isAfterVowelOrIndependent(sb)) "ऐ" else "ै"
                    "au" -> if (sb.isEmpty() || isAfterVowelOrIndependent(sb)) "औ" else "ौ"
                    "ri" -> if (sb.isEmpty()) "ऋ" else "ृ"
                    else -> null
                }
                if (matchedVowel != null) {
                    appendDevanagariToken(sb, matchedVowel)
                    i += 2
                    continue
                }
            }

            // Single character parsing
            val c = text[i]
            val token = when (c) {
                'a' -> if (sb.isEmpty()) "अ" else "a_inherent"
                'i' -> if (sb.isEmpty() || isAfterVowelOrIndependent(sb)) "इ" else "ि"
                'u' -> if (sb.isEmpty() || isAfterVowelOrIndependent(sb)) "उ" else "ु"
                'e' -> if (sb.isEmpty() || isAfterVowelOrIndependent(sb)) "ए" else "े"
                'o' -> if (sb.isEmpty() || isAfterVowelOrIndependent(sb)) "ओ" else "ो"
                'k' -> "क्"
                'g' -> "ग्"
                'c' -> "च्"
                'j' -> "ज्"
                't' -> "त्"
                'd' -> "द्"
                'n' -> "न्"
                'p' -> "प्"
                'b' -> "ब्"
                'm' -> "म्"
                'y' -> "य्"
                'r' -> "र्"
                'l' -> "ल्"
                'v', 'w' -> "व्"
                's' -> "स्"
                'h' -> "ह्"
                'z' -> "ज़्" // Nukta Za
                'f' -> "फ़्" // Nukta Fa
                'q' -> "क़्" // Nukta Qa
                else -> c.toString()
            }

            appendDevanagariToken(sb, token)
            i++
        }

        return cleanTrailingHalant(sb.toString(), '्')
    }

    private fun transliterateBengali(text: String): String {
        val sb = StringBuilder()
        var i = 0
        val len = text.length

        while (i < len) {
            // 3-char clusters
            if (i + 3 <= len) {
                val sub = text.substring(i, i + 3)
                when (sub) {
                    "ksh" -> { sb.append("ক্ষ্"); i += 3; continue }
                    "gya" -> { sb.append("জ্ঞ্"); i += 3; continue }
                    "chh" -> { sb.append("ছ্"); i += 3; continue }
                    "shh" -> { sb.append("ষ্"); i += 3; continue }
                    "tra" -> { sb.append("ত্র্"); i += 3; continue }
                }
            }

            // 2-char clusters
            if (i + 2 <= len) {
                val sub = text.substring(i, i + 2)
                val matched = when (sub) {
                    "kh" -> "খ্"
                    "gh" -> "ঘ্"
                    "ch" -> "চ্"
                    "jh" -> "ঝ্"
                    "th" -> "থ্"
                    "dh" -> "ধ্"
                    "ph" -> "ফ্"
                    "bh" -> "ভ্"
                    "sh" -> "শ্"
                    "ng" -> "ঙ্"
                    "aa" -> if (sb.isEmpty()) "আ" else "া"
                    "ee", "ii" -> if (sb.isEmpty()) "ঈ" else "ী"
                    "oo", "uu" -> if (sb.isEmpty()) "ঊ" else "ূ"
                    "ai" -> if (sb.isEmpty()) "ঐ" else "ৈ"
                    "au" -> if (sb.isEmpty()) "ঔ" else "ৌ"
                    "ri" -> if (sb.isEmpty()) "ঋ" else "ৃ"
                    else -> null
                }
                if (matched != null) {
                    appendBengaliToken(sb, matched)
                    i += 2
                    continue
                }
            }

            val c = text[i]
            val token = when (c) {
                'a' -> if (sb.isEmpty()) "অ" else "a_inherent"
                'i' -> if (sb.isEmpty()) "ই" else "ি"
                'u' -> if (sb.isEmpty()) "উ" else "ু"
                'e' -> if (sb.isEmpty()) "এ" else "ে"
                'o' -> if (sb.isEmpty()) "ও" else "ো"
                'k' -> "ক্"
                'g' -> "গ্"
                'j' -> "জ্"
                't' -> "ত্"
                'd' -> "দ্"
                'n' -> "ন্"
                'p' -> "প্"
                'b' -> "ব্"
                'm' -> "ম্"
                'r' -> "র্"
                'l' -> "ল্"
                's' -> "স্"
                'h' -> "হ্"
                'y' -> "য়্"
                else -> c.toString()
            }
            appendBengaliToken(sb, token)
            i++
        }

        return cleanTrailingHalant(sb.toString(), '্')
    }

    private fun appendDevanagariToken(sb: StringBuilder, token: String) {
        if (token == "a_inherent") {
            if (sb.isNotEmpty() && sb.last() == '्') {
                sb.deleteCharAt(sb.length - 1)
            }
            return
        }

        if (token.length == 1 && isDevanagariMatra(token[0])) {
            if (sb.isNotEmpty() && sb.last() == '्') {
                sb.deleteCharAt(sb.length - 1)
            }
            sb.append(token)
            return
        }

        sb.append(token)
    }

    private fun appendBengaliToken(sb: StringBuilder, token: String) {
        if (token == "a_inherent") {
            if (sb.isNotEmpty() && sb.last() == '্') {
                sb.deleteCharAt(sb.length - 1)
            }
            return
        }
        if (token.length == 1 && isBengaliMatra(token[0])) {
            if (sb.isNotEmpty() && sb.last() == '্') {
                sb.deleteCharAt(sb.length - 1)
            }
            sb.append(token)
            return
        }
        sb.append(token)
    }

    private fun isAfterVowelOrIndependent(sb: StringBuilder): Boolean {
        if (sb.isEmpty()) return true
        val last = sb.last()
        return isDevanagariMatra(last) || isDevanagariIndependentVowel(last)
    }

    private fun isDevanagariMatra(c: Char): Boolean = c in '\u093E'..'\u094C' || c == 'ृ'
    private fun isDevanagariIndependentVowel(c: Char): Boolean = c in '\u0904'..'\u0914'
    private fun isBengaliMatra(c: Char): Boolean = c in '\u09BE'..'\u09CC' || c == 'ৃ'

    private fun cleanTrailingHalant(str: String, halant: Char): String {
        return if (str.isNotEmpty() && str.last() == halant) {
            str.substring(0, str.length - 1)
        } else {
            str
        }
    }
}
