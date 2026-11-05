package com.brokey.keyboard

import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodSubtype
import com.brokey.BrokeyApplication
import com.brokey.haptics.HapticFeedbackManager
import com.brokey.languages.LanguageManager
import com.brokey.settings.KeyboardSettingsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Native Android InputMethodService for BROKEY keyboard.
 * Directly handles system-wide text input across WhatsApp, Telegram, Chrome, Instagram, SMS, and Email.
 * Refactored to match Gboard's performance, UX, inline AI, clipboard manager, and strict memory safety.
 */
class BrokeyInputMethodService : InputMethodService() {

    private lateinit var keyboardController: KeyboardController
    private var keyboardView: KeyboardView? = null
    private lateinit var hapticManager: HapticFeedbackManager
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate() {
        super.onCreate()
        hapticManager = HapticFeedbackManager(this)

        keyboardController = KeyboardController(
            onLayoutChanged = {
                keyboardView?.refreshLayout()
            },
            onCandidatesChanged = { candidates ->
                keyboardView?.setCandidates(candidates)
            }
        )
    }

    override fun onCreateInputView(): View {
        val view = KeyboardView(
            context = this,
            controller = keyboardController,
            onKeyPressed = { key ->
                keyboardController.handleKeyPress(key, currentInputConnection)
            },
            onBackspaceTriggered = { deleteWord ->
                keyboardController.handleBackspace(currentInputConnection, deleteWord)
            },
            onCandidateSelected = { candidate ->
                keyboardController.selectCandidate(candidate, currentInputConnection)
            },
            onToolbarActionTriggered = { actionId ->
                handleToolbarAction(actionId)
            },
            onClipPasted = { clipText ->
                hapticManager.performHaptic(HapticFeedbackManager.HapticType.CLIPBOARD_PASTE)
                currentInputConnection?.commitText(clipText, 1)
            },
            onAiRewriteRequested = { style ->
                handleAiRewrite(style)
            },
            onGestureWordRecognized = { words ->
                hapticManager.performHaptic(HapticFeedbackManager.HapticType.KEY_SPECIAL)
                keyboardController.handleGestureCandidates(words, currentInputConnection)
            }
        )
        keyboardView = view
        return view
    }

    private fun handleToolbarAction(actionId: String) {
        hapticManager.performHaptic(HapticFeedbackManager.HapticType.KEY_SPECIAL)
        when (actionId) {
            "LANG" -> keyboardController.cycleNextLanguage()
            "TRANSLIT" -> keyboardController.toggleTransliteration()
            "SETTINGS" -> {
                val intent = Intent(this, KeyboardSettingsActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
            "THEME" -> {
                val prefs = BrokeyApplication.instance.preferences
                val current = prefs.getSelectedTheme()
                val nextTheme = if (current == "charcoal") "saffron" else "charcoal"
                prefs.setSelectedTheme(nextTheme)
                keyboardView?.refreshLayout()
            }
            "VOICE" -> {
                // Sideload voice typing intent
            }
        }
    }

    private fun handleAiRewrite(style: com.brokey.ai.InlineAiAssistant.RewriteStyle) {
        val ic = currentInputConnection ?: return
        if (!keyboardController.aiAssistant.isSafeForInput(keyboardController.currentEditorInfo)) {
            return
        }

        // Get selected text or last sentence before cursor
        val selectedText = ic.getSelectedText(0)?.toString()
        val textToProcess = if (!selectedText.isNullOrBlank()) {
            selectedText
        } else {
            ic.getTextBeforeCursor(180, 0)?.toString() ?: ""
        }

        if (textToProcess.isBlank()) return

        serviceScope.launch {
            val result = keyboardController.aiAssistant.rewriteText(textToProcess, style)
            result.onSuccess { rewritten ->
                hapticManager.performHaptic(HapticFeedbackManager.HapticType.AI_COMPLETE)
                keyboardController.applyAiText(rewritten, ic)
            }
        }
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        keyboardView?.stopAllRepeats()
        keyboardController.onStartInput(info)
        keyboardView?.refreshLayout()
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        keyboardView?.stopAllRepeats()
    }

    override fun onFinishInput() {
        super.onFinishInput()
        keyboardView?.stopAllRepeats()
    }

    override fun onCurrentInputMethodSubtypeChanged(newSubtype: InputMethodSubtype?) {
        super.onCurrentInputMethodSubtypeChanged(newSubtype)
        if (newSubtype != null) {
            val locale = newSubtype.languageTag.ifEmpty { newSubtype.locale }
            val langCode = locale.take(2)
            LanguageManager.getByCode(langCode)?.let {
                keyboardController.setLanguage(it)
            }
        }
    }

    /**
     * Strict Memory Management: Purge transient caches on background switch or low RAM
     */
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level >= TRIM_MEMORY_RUNNING_CRITICAL || level >= TRIM_MEMORY_COMPLETE) {
            keyboardController.trimMemory()
            keyboardView?.clearMemory()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        keyboardController.trimMemory()
        keyboardView?.clearMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardController.trimMemory()
        keyboardView?.clearMemory()
        keyboardView = null
    }
}
