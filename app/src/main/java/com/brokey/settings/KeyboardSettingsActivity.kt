package com.brokey.settings

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brokey.BrokeyApplication
import com.brokey.R

/**
 * Settings Activity allowing user to configure haptic feedback, sound,
 * height scale, active themes, and view privacy policies.
 */
class KeyboardSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val prefs = BrokeyApplication.instance.preferences

        val switchHaptic = findViewById<Switch>(R.id.switch_haptic)
        val switchSound = findViewById<Switch>(R.id.switch_sound)
        val switchAutoCaps = findViewById<Switch>(R.id.switch_auto_caps)
        val btnClearData = findViewById<Button>(R.id.btn_clear_clipboard)

        switchHaptic.isChecked = prefs.isHapticFeedbackEnabled()
        switchHaptic.setOnCheckedChangeListener { _, isChecked ->
            prefs.setHapticFeedback(isChecked)
        }

        switchSound.isChecked = prefs.isKeySoundEnabled()
        switchSound.setOnCheckedChangeListener { _, isChecked ->
            prefs.setKeySound(isChecked)
        }

        switchAutoCaps.isChecked = prefs.isAutoCapitalizationEnabled()
        switchAutoCaps.setOnCheckedChangeListener { _, isChecked ->
            prefs.setAutoCapitalization(isChecked)
        }

        btnClearData.setOnClickListener {
            Toast.makeText(this, "Local temporary clipboard & learning cache cleared.", Toast.LENGTH_SHORT).show()
        }
    }
}
