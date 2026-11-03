package com.brokey.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

/**
 * Handles Native Android Voice Typing for Indian Multilingual Input.
 * Configures the native SpeechRecognizer with Indian BCP-47 language tags.
 */
class VoiceInputHandler(
    private val context: Context,
    private val onSpeechResult: (String) -> Unit,
    private val onSpeechStatus: (VoiceStatus) -> Unit
) {

    enum class VoiceStatus {
        IDLE,
        LISTENING,
        PROCESSING,
        ERROR,
        UNAVAILABLE
    }

    private var speechRecognizer: SpeechRecognizer? = null

    init {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(createListener())
            }
        }
    }

    /**
     * Maps BROKEY language code to standard Indic BCP-47 locale tag for Android ASR.
     */
    fun getBcp47Locale(langCode: String): String {
        return when (langCode.lowercase()) {
            "hi" -> "hi-IN" // Hindi
            "bn" -> "bn-IN" // Bengali
            "ta" -> "ta-IN" // Tamil
            "te" -> "te-IN" // Telugu
            "mr" -> "mr-IN" // Marathi
            "gu" -> "gu-IN" // Gujarati
            "kn" -> "kn-IN" // Kannada
            "ml" -> "ml-IN" // Malayalam
            "pa" -> "pa-IN" // Punjabi
            "or" -> "or-IN" // Odia
            "ur" -> "ur-IN" // Urdu
            "as" -> "as-IN" // Assamese
            "ne" -> "ne-NP" // Nepali
            "sa" -> "sa-IN" // Sanskrit
            "mai" -> "mai-IN" // Maithili
            "kok" -> "kok-IN" // Konkani
            "sd" -> "sd-IN" // Sindhi
            "ks" -> "ks-IN" // Kashmiri
            "doi" -> "doi-IN" // Dogri
            "brx" -> "brx-IN" // Bodo
            "mni" -> "mni-IN" // Manipuri
            "sat" -> "sat-IN" // Santali
            else -> "en-IN" // Indian English default
        }
    }

    fun startListening(langCode: String) {
        if (speechRecognizer == null) {
            onSpeechStatus(VoiceStatus.UNAVAILABLE)
            return
        }

        val bcp47 = getBcp47Locale(langCode)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, bcp47)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, bcp47)
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, bcp47)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        }

        try {
            speechRecognizer?.startListening(intent)
            onSpeechStatus(VoiceStatus.LISTENING)
        } catch (e: Exception) {
            Log.e("VoiceInputHandler", "Error starting speech recognition", e)
            onSpeechStatus(VoiceStatus.ERROR)
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
            onSpeechStatus(VoiceStatus.PROCESSING)
        } catch (e: Exception) {
            onSpeechStatus(VoiceStatus.IDLE)
        }
    }

    fun destroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }

    private fun createListener() = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            onSpeechStatus(VoiceStatus.LISTENING)
        }

        override fun onBeginningOfSpeech() {}

        override fun onRmsChanged(rmsdB: Float) {}

        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {
            onSpeechStatus(VoiceStatus.PROCESSING)
        }

        override fun onError(error: Int) {
            Log.w("VoiceInputHandler", "Speech recognition error code: $error")
            onSpeechStatus(VoiceStatus.ERROR)
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val recognizedText = matches?.firstOrNull() ?: ""
            if (recognizedText.isNotEmpty()) {
                onSpeechResult(recognizedText)
            }
            onSpeechStatus(VoiceStatus.IDLE)
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
            if (!partial.isNullOrEmpty()) {
                onSpeechResult(partial)
            }
        }

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }
}
