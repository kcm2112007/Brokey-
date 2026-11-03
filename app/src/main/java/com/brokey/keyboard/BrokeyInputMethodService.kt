package com.brokey.keyboard

import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodSubtype
import com.brokey.BrokeyApplication
import com.brokey.languages.LanguageManager
import com.brokey.transliteration.TransliterationEngine

/**
 * Native Android InputMethodService for BROKEY keyboard.
 * Directly handles system-wide text input across WhatsApp, Telegram, Chrome, Instagram, SMS, and Email.
 */
class BrokeyInputMethodService : InputMethodService() {

    private lateinit var keyboardController: KeyboardController
    private var keyboardView: KeyboardView? = null
    private var vibrator: Vibrator? = null

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(VIBRATOR_SERVICE) as? Vibrator

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
                triggerHapticFeedback()
                keyboardController.handleKeyPress(key, currentInputConnection)
            },
            onCandidateSelected = { candidate ->
                triggerHapticFeedback()
                keyboardController.selectCandidate(candidate, currentInputConnection)
            }
        )
        keyboardView = view
        return view
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        keyboardController.onStartInput(info)
        keyboardView?.refreshLayout()
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

    private fun triggerHapticFeedback() {
        val prefs = BrokeyApplication.instance.preferences
        if (!prefs.isHapticFeedbackEnabled()) return

        vibrator?.let { v ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(18, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(18)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardView = null
    }
}
