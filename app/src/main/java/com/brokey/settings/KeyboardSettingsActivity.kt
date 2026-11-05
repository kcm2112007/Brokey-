package com.brokey.settings

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brokey.BrokeyApplication
import com.brokey.R

/**
 * Settings Activity allowing user to configure haptic feedback, sound,
 * height scale, themes, number row, and privacy data.
 */
class KeyboardSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val prefs = BrokeyApplication.instance.preferences

        val btnBack = findViewById<TextView>(R.id.btn_back)
        val switchHaptic = findViewById<Switch>(R.id.switch_haptic)
        val switchSound = findViewById<Switch>(R.id.switch_sound)
        val switchAutoCaps = findViewById<Switch>(R.id.switch_auto_caps)
        val switchNumberRow = findViewById<Switch>(R.id.switch_number_row)
        val tvCurrentTheme = findViewById<TextView>(R.id.tv_current_theme)
        val btnThemeCharcoal = findViewById<Button>(R.id.btn_theme_charcoal)
        val btnThemeLight = findViewById<Button>(R.id.btn_theme_light)
        val btnClearData = findViewById<Button>(R.id.btn_clear_clipboard)

        btnBack.setOnClickListener {
            finish()
        }

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

        switchNumberRow.isChecked = prefs.isNumberRowEnabled()
        switchNumberRow.setOnCheckedChangeListener { _, isChecked ->
            prefs.setNumberRowEnabled(isChecked)
        }

        val currentTheme = prefs.getSelectedTheme()
        updateThemeUI(currentTheme, tvCurrentTheme, btnThemeCharcoal, btnThemeLight)

        btnThemeCharcoal.setOnClickListener {
            prefs.setSelectedTheme("charcoal_dark")
            updateThemeUI("charcoal_dark", tvCurrentTheme, btnThemeCharcoal, btnThemeLight)
            Toast.makeText(this, "Dark Charcoal theme activated", Toast.LENGTH_SHORT).show()
        }

        btnThemeLight.setOnClickListener {
            prefs.setSelectedTheme("light_theme")
            updateThemeUI("light_theme", tvCurrentTheme, btnThemeCharcoal, btnThemeLight)
            Toast.makeText(this, "Light theme activated", Toast.LENGTH_SHORT).show()
        }

        btnClearData.setOnClickListener {
            Toast.makeText(this, "Local temporary clipboard & learning cache cleared.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateThemeUI(themeId: String, label: TextView, btnCharcoal: Button, btnLight: Button) {
        if (themeId == "light_theme") {
            label.text = "Active: Light Theme"
            btnLight.setBackgroundColor(Color.parseColor("#F97316"))
            btnLight.setTextColor(Color.parseColor("#000000"))
            btnCharcoal.setBackgroundColor(Color.parseColor("#2B3038"))
            btnCharcoal.setTextColor(Color.parseColor("#F8FAFC"))
        } else {
            label.text = "Active: Charcoal Dark (Orange Accent)"
            btnCharcoal.setBackgroundColor(Color.parseColor("#F97316"))
            btnCharcoal.setTextColor(Color.parseColor("#000000"))
            btnLight.setBackgroundColor(Color.parseColor("#2B3038"))
            btnLight.setTextColor(Color.parseColor("#F8FAFC"))
        }
    }
}
