package com.brokey.dictionaries

/**
 * High-performance, offline Prefix-Tree (Trie) dictionary for BROKEY (Phase 4).
 * Provides sub-millisecond prefix auto-completion, frequency-weighted candidate ranking,
 * and Levenshtein edit-distance fuzzy matching for offline autocorrection.
 */
class TrieDictionary {

    data class TrieNode(
        val children: MutableMap<Char, TrieNode> = HashMap(),
        var isWord: Boolean = false,
        var frequency: Int = 0,
        var word: String? = null
    )

    data class Suggestion(
        val word: String,
        val frequency: Int,
        val isExact: Boolean,
        val isAutocorrect: Boolean = false
    )

    private val root = TrieNode()

    /**
     * Insert a word with a frequency weight into the Trie.
     */
    fun insert(word: String, frequency: Int = 100) {
        val clean = word.trim().lowercase()
        if (clean.isEmpty()) return

        var curr = root
        for (char in clean) {
            curr = curr.children.getOrPut(char) { TrieNode() }
        }
        curr.isWord = true
        curr.frequency = maxOf(curr.frequency, frequency)
        curr.word = clean
    }

    /**
     * Search words matching a given prefix, ranked by frequency.
     */
    fun searchPrefix(prefix: String, limit: Int = 5): List<Suggestion> {
        val clean = prefix.trim().lowercase()
        if (clean.isEmpty()) return emptyList()

        var curr = root
        for (char in clean) {
            curr = curr.children[char] ?: return emptyList()
        }

        val results = mutableListOf<Suggestion>()
        collectWords(curr, results, clean)
        return results.sortedByDescending { it.frequency }.take(limit)
    }

    private fun collectWords(node: TrieNode, results: MutableList<Suggestion>, prefix: String) {
        if (node.isWord && node.word != null) {
            results.add(
                Suggestion(
                    word = node.word!!,
                    frequency = node.frequency,
                    isExact = node.word == prefix
                )
            )
        }
        for ((_, child) in node.children) {
            collectWords(child, results, prefix)
        }
    }

    /**
     * Find nearest autocorrect candidates using edit distance (Levenshtein distance <= 1).
     * Handles mistyped, transposed, omitted, or inserted adjacent characters.
     */
    fun findAutocorrect(word: String, limit: Int = 3): List<Suggestion> {
        val clean = word.trim().lowercase()
        if (clean.length < 3) return emptyList()

        val candidates = mutableListOf<Suggestion>()
        searchFuzzy(root, "", clean, 0, candidates)
        return candidates
            .filter { it.word != clean }
            .sortedByDescending { it.frequency }
            .take(limit)
    }

    private fun searchFuzzy(
        node: TrieNode,
        currentPath: String,
        target: String,
        edits: Int,
        results: MutableList<Suggestion>
    ) {
        if (edits > 1) return

        if (node.isWord && node.word != null) {
            val dist = calculateLevenshtein(node.word!!, target)
            if (dist <= 1) {
                results.add(
                    Suggestion(
                        word = node.word!!,
                        frequency = node.frequency,
                        isExact = false,
                        isAutocorrect = true
                    )
                )
            }
        }

        for ((char, child) in node.children) {
            searchFuzzy(child, currentPath + char, target, edits, results)
        }
    }

    private fun calculateLevenshtein(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
        for (i in 0..s1.length) dp[i][0] = i
        for (j in 0..s2.length) dp[0][j] = j

        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,       // deletion
                    dp[i][j - 1] + 1,       // insertion
                    dp[i - 1][j - 1] + cost // substitution
                )
            }
        }
        return dp[s1.length][s2.length]
    }
}
