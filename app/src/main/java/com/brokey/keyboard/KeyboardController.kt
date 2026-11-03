package com.brokey.keyboard

import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import com.brokey.languages.LanguageConfig
import com.brokey.languages.LanguageManager
import com.brokey.layouts.KeyboardLayouts
import com.brokey.transliteration.TransliterationEngine

/**
 * State machine and event controller for BROKEY keyboard.
 * Manages typing modes, shift states, composing buffer, candidates, and commits to InputConnection.
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

    fun onStartInput(info: EditorInfo?) {
        composingText.clear()
        isShifted = false
        isCapsLock = false
        currentMode = KeyboardMode.LETTERS
        updateCandidates()
        onLayoutChanged()
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
                if (composingText.isNotEmpty()) {
                    composingText.deleteCharAt(composingText.length - 1)
                    if (composingText.isNotEmpty()) {
                        updateCandidates()
                    } else {
                        ic.setComposingText("", 1)
                        ic.finishComposingText()
                        onCandidatesChanged(emptyList())
                    }
                } else {
                    // Safe unicode deletion
                    ic.deleteSurroundingText(1, 0)
                }
            }

            KeyModel.KeyType.SPACE -> {
                if (composingText.isNotEmpty()) {
                    // Commit top candidate or current buffer
                    val candidates = transliterationEngine.getCandidates(
                        composingText.toString(),
                        activeLanguage.scriptType
                    )
                    val commitWord = candidates.firstOrNull()?.word ?: composingText.toString()
                    ic.commitText("$commitWord ", 1)
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
                ic.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER))
                ic.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_ENTER))
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

                if (isTransliterationEnabled && activeLanguage.code != "en") {
                    // Accumulate in composing text
                    composingText.append(charToInsert)
                    ic.setComposingText(composingText.toString(), 1)
                    updateCandidates()
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

    fun selectCandidate(candidate: TransliterationEngine.Candidate, ic: InputConnection?) {
        if (ic == null) return
        ic.commitText(candidate.word + " ", 1)
        lastCommittedWord = candidate.word
        composingText.clear()
        
        // Show next-word predictions
        val nextWords = suggestionEngine.getNextWordPredictions(lastCommittedWord)
        if (nextWords.isNotEmpty()) {
            onCandidatesChanged(nextWords.map { 
                TransliterationEngine.Candidate(word = it, isExactMatch = false, frequency = 400, origin = "prediction") 
            })
        } else {
            onCandidatesChanged(emptyList())
        }
    }

    private fun updateCandidates() {
        if (composingText.isEmpty()) {
            onCandidatesChanged(emptyList())
            return
        }

        val candidates = transliterationEngine.getCandidates(
            composingText.toString(),
            activeLanguage.scriptType
        ).toMutableList()

        // Check autocorrect typos
        val typoSuggestions = suggestionEngine.getComposingSuggestions(composingText.toString())
        typoSuggestions.filter { it.isAutocorrect }.forEach { typo ->
            if (candidates.none { it.word == typo.word }) {
                candidates.add(1.coerceAtMost(candidates.size), TransliterationEngine.Candidate(
                    word = typo.word,
                    isExactMatch = false,
                    frequency = 800,
                    origin = "autocorrect"
                ))
            }
        }

        onCandidatesChanged(candidates)
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
}
