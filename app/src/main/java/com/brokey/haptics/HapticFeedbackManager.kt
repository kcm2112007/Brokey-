package com.brokey.haptics

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.SystemClock
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import com.brokey.BrokeyApplication

/**
 * Modern Precision Haptic & Audio Feedback Manager tailored for rapid typing rhythms.
 * Complies with Android 8.0 - 15+ vibration guidelines.
 */
class HapticFeedbackManager(private val context: Context) {

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private val audioManager: AudioManager? = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager

    private var lastVibrationTime = 0L
    private val MIN_VIBRATION_INTERVAL_MS = 15L // Prevents vibration motor muddying at > 80 WPM

    enum class HapticType {
        KEY_PRESS,
        KEY_SPECIAL,  // Enter, Shift, Backspace
        KEY_LONG_PRESS,
        GESTURE_TRAIL_POINT,
        AI_COMPLETE,
        CLIPBOARD_PASTE
    }

    /**
     * Trigger modern waveform or predefined vibration effect tailored to typing.
     */
    fun performHaptic(type: HapticType) {
        val prefs = BrokeyApplication.instance.preferences
        if (!prefs.isHapticFeedbackEnabled()) return
        if (vibrator == null || !vibrator.hasVibrator()) return

        val now = SystemClock.uptimeMillis()
        if (type == HapticType.KEY_PRESS && now - lastVibrationTime < MIN_VIBRATION_INTERVAL_MS) {
            return
        }
        lastVibrationTime = now

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val effect = when (type) {
                HapticType.KEY_PRESS -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                HapticType.KEY_SPECIAL -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                HapticType.KEY_LONG_PRESS -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                HapticType.GESTURE_TRAIL_POINT -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                HapticType.AI_COMPLETE -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
                HapticType.CLIPBOARD_PASTE -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val attrs = VibrationAttributes.Builder()
                    .setUsage(VibrationAttributes.USAGE_TOUCH)
                    .build()
                vibrator.vibrate(effect, attrs)
            } else {
                vibrator.vibrate(effect)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val (duration, amplitude) = when (type) {
                HapticType.KEY_PRESS -> 10L to 100
                HapticType.KEY_SPECIAL -> 20L to 180
                HapticType.KEY_LONG_PRESS -> 35L to 255
                HapticType.GESTURE_TRAIL_POINT -> 6L to 60
                HapticType.AI_COMPLETE -> 25L to 160
                HapticType.CLIPBOARD_PASTE -> 15L to 120
            }
            vibrator.vibrate(VibrationEffect.createOneShot(duration, amplitude))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(12L)
        }
    }

    /**
     * Trigger auditory click using system audio FX channel.
     */
    fun performAudioFeedback(keyType: com.brokey.keyboard.KeyModel.KeyType) {
        val prefs = BrokeyApplication.instance.preferences
        if (!prefs.isSoundEnabled()) return
        val am = audioManager ?: return

        val fx = when (keyType) {
            com.brokey.keyboard.KeyModel.KeyType.SPACE -> AudioManager.FX_KEYPRESS_SPACEBAR
            com.brokey.keyboard.KeyModel.KeyType.BACKSPACE -> AudioManager.FX_KEYPRESS_DELETE
            com.brokey.keyboard.KeyModel.KeyType.ENTER -> AudioManager.FX_KEYPRESS_RETURN
            else -> AudioManager.FX_KEYPRESS_STANDARD
        }
        am.playSoundEffect(fx, 0.4f)
    }
}
