package com.brokey.suggestions

import com.brokey.dictionaries.IndicDictionary
import com.brokey.dictionaries.TrieDictionary

/**
 * Intelligent Offline Suggestion & Autocorrection Engine for BROKEY (Phase 4).
 * Combines Trie prefix searches, Levenshtein typo autocorrection,
 * and bigram contextual next-word predictions.
 */
class SuggestionEngine {

    private val trie = TrieDictionary()

    // Bigram next-word prediction associations
    private val nextWordModels = mapOf(
        "namaste" to listOf("जी", "आप", "दोस्त"),
        "namaskar" to listOf("जी", "मित्र", "मित्रांनो"),
        "kaise" to listOf("हो", "हैं", "हो?"),
        "kaisa" to listOf("है", "चल", "रहा"),
        "kya" to listOf("हाल", "बात", "कर"),
        "aap" to listOf("कैसे", "कहाँ", "क्या"),
        "tum" to listOf("कहाँ", "कैसे", "क्या"),
        "main" to listOf("ठीक", "अच्छा", "घर"),
        "hum" to listOf("सब", "लोग", "साथ"),
        "shubh" to listOf("प्रभात", "कामनाएं", "दिन"),
        "dhanyawad" to listOf("आपका", "बहुत", "जी"),
        "shukriya" to listOf("आपका", "बहुत", "दोस्त"),
        "kemon" to listOf("আছেন", "আছো", "আছো?"),
        "ami" to listOf("ভালো", "আছি", "ভাত"),
        "tumi" to listOf("কেমন", "কোথায়", "কী"),
        "shubho" to listOf("সকাল", "রাত্রি", "কামনা"),
        "dhonnobad" to listOf("আপনাকে", "তোমায়", "অনেক"),
        "vanakkam" to listOf("நண்பா", "வணக்கம்!", "காலை"),
        "nandri" to listOf("நண்பா", "வணக்கம்", "மிக்க"),
        "eppadi" to listOf("இருக்கீங்க", "இருக்கிறீர்கள்"),
        "namaskaram" to listOf("అండి", "మిత్రమా", "గారు"),
        "ela" to listOf("ఉన్నారు", "ఉన్నావు"),
        "sat" to listOf("ਸ੍ਰੀ", "ਅਕਾਲ"),
        "kem" to listOf("છો", "છો?"),
        "how" to listOf("are", "is", "about"),
        "thank" to listOf("you", "so", "very"),
        "good" to listOf("morning", "evening", "night"),
        "what" to listOf("is", "are", "about"),
        "where" to listOf("are", "is", "were"),
        "see" to listOf("you", "later", "soon")
    )

    init {
        // Seed Trie with English & Indic vocabularies
        val baseWords = listOf(
            "the", "be", "to", "of", "and", "a", "in", "that", "have", "i", "it", "for", "not", "on", "with",
            "he", "as", "you", "do", "at", "this", "but", "his", "by", "from", "they", "we", "say", "her",
            "she", "or", "an", "will", "my", "one", "all", "would", "there", "their", "what", "so", "up",
            "out", "if", "about", "who", "get", "which", "go", "me", "when", "make", "can", "like", "time",
            "no", "just", "him", "know", "take", "people", "into", "year", "your", "good", "some", "could",
            "them", "see", "other", "than", "then", "now", "look", "only", "come", "its", "over", "think",
            "also", "back", "after", "use", "two", "how", "our", "work", "first", "well", "way", "even",
            "new", "want", "because", "any", "these", "give", "day", "most", "us"
        )
        baseWords.forEach { trie.insert(it, 200) }

        // Seed Indic words
        IndicDictionary.HINDI_LEXICON.forEach { (rom, indicWords) ->
            trie.insert(rom, 500)
            indicWords.forEach { trie.insert(it, 600) }
        }
        IndicDictionary.BENGALI_LEXICON.forEach { (rom, indicWords) ->
            trie.insert(rom, 500)
            indicWords.forEach { trie.insert(it, 600) }
        }
        IndicDictionary.TAMIL_LEXICON.forEach { (rom, indicWords) ->
            trie.insert(rom, 500)
            indicWords.forEach { trie.insert(it, 600) }
        }
        IndicDictionary.TELUGU_LEXICON.forEach { (rom, indicWords) ->
            trie.insert(rom, 500)
            indicWords.forEach { trie.insert(it, 600) }
        }
    }

    /**
     * Get completions and autocorrect candidates for the actively typed word.
     */
    fun getComposingSuggestions(query: String): List<TrieDictionary.Suggestion> {
        val clean = query.trim().lowercase()
        if (clean.isEmpty()) return emptyList()

        val prefixMatches = trie.searchPrefix(clean, limit = 4)
        if (prefixMatches.isNotEmpty() && prefixMatches.any { it.isExact }) {
            return prefixMatches
        }

        // If no exact match, look for nearest autocorrect typo
        val autocorrectMatches = trie.findAutocorrect(clean, limit = 2)
        val combined = mutableListOf<TrieDictionary.Suggestion>()
        combined.addAll(prefixMatches)
        autocorrectMatches.forEach { auto ->
            if (combined.none { it.word == auto.word }) {
                combined.add(auto)
            }
        }
        return combined.sortedByDescending { it.frequency }
    }

    /**
     * Get proactive next-word predictions based on the previously committed word.
     */
    fun getNextWordPredictions(lastWord: String): List<String> {
        val clean = lastWord.trim().lowercase()
        return nextWordModels[clean] ?: emptyList()
    }
}
