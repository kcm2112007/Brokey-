package com.brokey.keyboard

import android.text.InputType
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import com.brokey.ai.InlineAiAssistant
import com.brokey.languages.LanguageConfig
import com.brokey.languages.LanguageManager
import com.brokey.layouts.KeyboardLayouts
import com.brokey.transliteration.TransliterationEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * High-performance state machine and event controller for BROKEY keyboard.
 * Manages typing modes, shift states, asynchronous candidates via Coroutines,
 * gesture trajectory commits, inline AI text substitution, and strict password isolation.
 */
class KeyboardController(
    private val onLayoutChanged: () -> Unit,
    private val onCandidatesChanged: (List<TransliterationEngine.Candidate>) -> Unit
) {
    enum class KeyboardMode {
        LETTERS,
        NUMBERS,
        SYMBOLS,
        EMOJI
    }

    var currentMode: KeyboardMode = KeyboardMode.LETTERS
        private set

    var isShifted: Boolean = false
        private set

    var isCapsLock: Boolean = false
        private set

    private val composingText = StringBuilder()
    private val transliterationEngine = TransliterationEngine()
    private val suggestionEngine = com.brokey.suggestions.SuggestionEngine()
    private var lastCommittedWord: String = ""

    var activeLanguage: LanguageConfig = LanguageManager.getActiveLanguage()
        private set

    var isTransliterationEnabled: Boolean = false
        private set

    var currentEditorInfo: EditorInfo? = null
        private set

    var isPasswordField: Boolean = false
        private set

    val aiAssistant = InlineAiAssistant()

    // Non-blocking Coroutine Scope for Asynchronous Candidates
    private val controllerScope = CoroutineScope(Dispatchers.Main + Job())
    private var candidateDebounceJob: Job? = null

    fun onStartInput(info: EditorInfo?) {
        currentEditorInfo = info
        isPasswordField = checkIsPasswordField(info)

        composingText.clear()
        isShifted = false
        isCapsLock = false
        currentMode = KeyboardMode.LETTERS

        candidateDebounceJob?.cancel()
        if (isPasswordField) {
            onCandidatesChanged(emptyList())
        } else {
            updateCandidatesAsync()
        }
        onLayoutChanged()
    }

    private fun checkIsPasswordField(info: EditorInfo?): Boolean {
        if (info == null) return false
        val inputType = info.inputType
        val variation = inputType and InputType.TYPE_MASK_VARIATION
        return variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
                variation == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD ||
                (inputType and InputType.TYPE_CLASS_NUMBER != 0 && variation == InputType.TYPE_NUMBER_VARIATION_PASSWORD)
    }

    fun handleKeyPress(key: KeyModel, ic: InputConnection?) {
        if (ic == null) return

        when (key.type) {
            KeyModel.KeyType.SHIFT -> {
                if (isShifted && !isCapsLock) {
                    isCapsLock = true
                } else if (isCapsLock) {
                    isShifted = false
                    isCapsLock = false
                } else {
                    isShifted = true
                }
                onLayoutChanged()
            }

            KeyModel.KeyType.BACKSPACE -> {
                handleBackspace(ic, deleteWholeWord = false)
            }

            KeyModel.KeyType.SPACE -> {
                if (composingText.isNotEmpty()) {
                    val candidates = transliterationEngine.getCandidates(
                        composingText.toString(),
                        activeLanguage.scriptType
                    )
                    val commitWord = candidates.firstOrNull()?.word ?: composingText.toString()
                    ic.commitText("$commitWord ", 1)
                    lastCommittedWord = commitWord
                    composingText.clear()
                    onCandidatesChanged(emptyList())
                } else {
                    ic.commitText(" ", 1)
                }
            }

            KeyModel.KeyType.ENTER -> {
                if (composingText.isNotEmpty()) {
                    ic.commitText(composingText.toString(), 1)
                    composingText.clear()
                    onCandidatesChanged(emptyList())
                }
                ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
                ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
            }

            KeyModel.KeyType.MODE_CHANGE -> {
                currentMode = when (key.code) {
                    KeyModel.CODE_MODE_NUMBERS -> KeyboardMode.NUMBERS
                    KeyModel.CODE_MODE_SYMBOLS -> KeyboardMode.SYMBOLS
                    KeyModel.CODE_MODE_LETTERS -> KeyboardMode.LETTERS
                    else -> KeyboardMode.LETTERS
                }
                onLayoutChanged()
            }

            KeyModel.KeyType.LANGUAGE_SWITCH -> {
                cycleNextLanguage()
            }

            KeyModel.KeyType.EMOJI -> {
                currentMode = KeyboardMode.EMOJI
                onLayoutChanged()
            }

            KeyModel.KeyType.CHARACTER -> {
                val charToInsert = if (isShifted || isCapsLock) {
                    key.shiftedLabel ?: key.primaryLabel
                } else {
                    key.primaryLabel
                }

                if (isTransliterationEnabled && activeLanguage.code != "en" && !isPasswordField) {
                    composingText.append(charToInsert)
                    ic.setComposingText(composingText.toString(), 1)
                    updateCandidatesAsync()
                } else {
                    ic.commitText(charToInsert, 1)
                }

                if (isShifted && !isCapsLock) {
                    isShifted = false
                    onLayoutChanged()
                }
            }

            else -> {
                ic.commitText(key.primaryLabel, 1)
            }
        }
    }

    /**
     * Non-blocking candidate retrieval executed on Dispatchers.Default with rapid typing debounce.
     */
    private fun updateCandidatesAsync() {
        if (isPasswordField || composingText.isEmpty()) {
            onCandidatesChanged(emptyList())
            return
        }

        candidateDebounceJob?.cancel()
        candidateDebounceJob = controllerScope.launch {
            val query = composingText.toString()
            val script = activeLanguage.scriptType

            // Run transliteration search and Trie indexing off the main UI thread
            val candidates = withContext(Dispatchers.Default) {
                val results = transliterationEngine.getCandidates(query, script).toMutableList()
                val typoSuggestions = suggestionEngine.getComposingSuggestions(query)
                typoSuggestions.filter { it.isAutocorrect }.forEach { typo ->
                    if (results.none { it.word == typo.word }) {
                        results.add(
                            1.coerceAtMost(results.size),
                            TransliterationEngine.Candidate(
                                word = typo.word,
                                isExactMatch = false,
                                frequency = 850,
                                origin = "autocorrect"
                            )
                        )
                    }
                }
                results
            }

            onCandidatesChanged(candidates)
        }
    }

    fun selectCandidate(candidate: TransliterationEngine.Candidate, ic: InputConnection?) {
        if (ic == null) return
        ic.commitText(candidate.word + " ", 1)
        lastCommittedWord = candidate.word
        composingText.clear()

        if (!isPasswordField) {
            controllerScope.launch {
                val nextWords = withContext(Dispatchers.Default) {
                    suggestionEngine.getNextWordPredictions(lastCommittedWord)
                }
                if (nextWords.isNotEmpty()) {
                    onCandidatesChanged(nextWords.map {
                        TransliterationEngine.Candidate(word = it, isExactMatch = false, frequency = 400, origin = "prediction")
                    })
                } else {
                    onCandidatesChanged(emptyList())
                }
            }
        }
    }

    /**
     * Handles words decoded from Glide / Gesture typing swipe trajectory.
     */
    fun handleGestureCandidates(words: List<String>, ic: InputConnection?) {
        if (words.isEmpty() || ic == null || isPasswordField) return

        val topWord = words.first()
        if (isTransliterationEnabled && activeLanguage.code != "en") {
            val candidates = transliterationEngine.getCandidates(topWord, activeLanguage.scriptType)
            val commitWord = candidates.firstOrNull()?.word ?: topWord
            ic.commitText("$commitWord ", 1)
        } else {
            ic.commitText("$topWord ", 1)
        }
        lastCommittedWord = topWord
        composingText.clear()
        onCandidatesChanged(emptyList())
    }

    /**
     * Replace current text/selection with AI rewritten or generated response.
     */
    fun applyAiText(newText: String, ic: InputConnection?) {
        if (ic == null || newText.isBlank() || isPasswordField) return

        // If composing, finish it first
        if (composingText.isNotEmpty()) {
            ic.finishComposingText()
            composingText.clear()
        }

        // Commit AI text directly into active input field
        ic.commitText(newText, 1)
        onCandidatesChanged(emptyList())
    }

    /**
     * Handles character or word deletion with Unicode surrogate pair safety,
     * in-flight composing buffer cancellation, and Indic word boundary respect.
     */
    fun handleBackspace(ic: InputConnection?, deleteWholeWord: Boolean = false) {
        if (ic == null) return

        // 1. If actively transliterating/composing, delete from composing text
        if (composingText.isNotEmpty()) {
            if (deleteWholeWord) {
                composingText.clear()
                ic.setComposingText("", 1)
                ic.finishComposingText()
                onCandidatesChanged(emptyList())
            } else {
                composingText.deleteCharAt(composingText.length - 1)
                if (composingText.isNotEmpty() && !isPasswordField) {
                    updateCandidatesAsync()
                } else {
                    ic.setComposingText("", 1)
                    ic.finishComposingText()
                    onCandidatesChanged(emptyList())
                }
            }
            return
        }

        // 2. Intelligent Word Deletion (triggered after sustained hold)
        if (deleteWholeWord) {
            val textBefore = ic.getTextBeforeCursor(80, 0)?.toString()
            if (!textBefore.isNullOrEmpty()) {
                val trimmed = textBefore.trimEnd()
                val trailingSpaces = textBefore.length - trimmed.length
                if (trailingSpaces > 0) {
                    // Delete trailing whitespace first
                    ic.deleteSurroundingText(trailingSpaces, 0)
                } else {
                    var lastWordLen = 0
                    for (i in textBefore.length - 1 downTo 0) {
                        val c = textBefore[i]
                        // Indic purna viram '।', deergh viram '॥', standard punctuation, or whitespace
                        if (c.isWhitespace() || c == '.' || c == ',' || c == '!' || c == '?' || c == '।' || c == '॥' || c == '-' || c == '_') {
                            if (lastWordLen > 0) break
                        } else {
                            lastWordLen++
                        }
                    }
                    val charsToDelete = if (lastWordLen > 0) lastWordLen else 1
                    ic.deleteSurroundingText(charsToDelete, 0)
                }
            } else {
                ic.deleteSurroundingText(1, 0)
            }
        } else {
            // 3. Single Character / Unicode Grapheme Cluster Deletion
            val textBefore = ic.getTextBeforeCursor(2, 0)?.toString()
            if (!textBefore.isNullOrEmpty() && textBefore.length >= 2 &&
                Character.isSurrogatePair(textBefore[textBefore.length - 2], textBefore[textBefore.length - 1])) {
                ic.deleteSurroundingText(2, 0)
            } else {
                ic.deleteSurroundingText(1, 0)
            }
        }
    }

    fun getCurrentKeyLayout(): List<List<KeyModel>> {
        return when (currentMode) {
            KeyboardMode.NUMBERS -> KeyboardLayouts.getNumbersAndSymbolsLayout()
            KeyboardMode.SYMBOLS -> KeyboardLayouts.getNumbersAndSymbolsLayout()
            KeyboardMode.EMOJI,
            KeyboardMode.LETTERS -> {
                if (isTransliterationEnabled || activeLanguage.code == "en") {
                    KeyboardLayouts.getEnglishLayout()
                } else {
                    when (activeLanguage.code) {
                        "hi", "mr", "sa", "ne", "kok", "brx", "doi", "mai" ->
                            KeyboardLayouts.getHindiDevanagariLayout()
                        "bn", "as" ->
                            KeyboardLayouts.getBengaliLayout()
                        "ta" ->
                            KeyboardLayouts.getTamilLayout()
                        "te" ->
                            KeyboardLayouts.getTeluguLayout()
                        "kn" ->
                            KeyboardLayouts.getKannadaLayout()
                        "ml" ->
                            KeyboardLayouts.getMalayalamLayout()
                        "gu" ->
                            KeyboardLayouts.getGujaratiLayout()
                        "pa" ->
                            KeyboardLayouts.getPunjabiGurmukhiLayout()
                        "or" ->
                            KeyboardLayouts.getOdiaLayout()
                        "ur", "ks", "sd" ->
                            KeyboardLayouts.getUrduLayout()
                        else ->
                            KeyboardLayouts.getEnglishLayout()
                    }
                }
            }
        }
    }

    fun toggleTransliteration() {
        isTransliterationEnabled = !isTransliterationEnabled
        composingText.clear()
        onCandidatesChanged(emptyList())
        onLayoutChanged()
    }

    fun setLanguage(language: LanguageConfig) {
        activeLanguage = language
        LanguageManager.setActiveLanguage(language.code)
        composingText.clear()
        onCandidatesChanged(emptyList())
        onLayoutChanged()
    }

    fun cycleNextLanguage() {
        val all = LanguageManager.ALL_LANGUAGES
        val currentIndex = all.indexOfFirst { it.code == activeLanguage.code }
        val nextIndex = if (currentIndex >= 0 && currentIndex + 1 < all.size) currentIndex + 1 else 0
        setLanguage(all[nextIndex])
    }

    fun trimMemory() {
        candidateDebounceJob?.cancel()
        controllerScope.coroutineContext.cancelChildren()
        composingText.clear()
        onCandidatesChanged(emptyList())
    }
}
