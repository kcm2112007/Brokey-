package com.brokey

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.brokey.settings.KeyboardSettingsActivity

/**
 * Main entry activity guiding the user through 3-step setup:
 * 1. Enable BROKEY in Android Settings.
 * 2. Select BROKEY as the active input method.
 * 3. Test typing directly in an integrated sandbox.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var btnEnable: Button
    private lateinit var btnSelect: Button
    private lateinit var btnSettings: Button
    private lateinit var testInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.tv_status)
        btnEnable = findViewById(R.id.btn_enable_keyboard)
        btnSelect = findViewById(R.id.btn_select_keyboard)
        btnSettings = findViewById(R.id.btn_open_settings)
        testInput = findViewById(R.id.et_test_typing)

        btnEnable.setOnClickListener {
            // Open system input method settings
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        btnSelect.setOnClickListener {
            // Show system input method picker dialog
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showInputMethodPicker()
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, KeyboardSettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateKeyboardStatus()
    }

    private fun updateKeyboardStatus() {
        val isEnabled = isBrokeyEnabled()
        val isSelected = isBrokeySelected()

        when {
            isSelected -> {
                statusText.text = getString(R.string.status_active)
                btnEnable.isEnabled = false
                btnSelect.isEnabled = false
            }
            isEnabled -> {
                statusText.text = getString(R.string.status_enabled_not_selected)
                btnEnable.isEnabled = false
                btnSelect.isEnabled = true
            }
            else -> {
                statusText.text = getString(R.string.status_not_enabled)
                btnEnable.isEnabled = true
                btnSelect.isEnabled = false
            }
        }
    }

    private fun isBrokeyEnabled(): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val enabledList = imm.enabledInputMethodList
        val myPackage = packageName
        return enabledList.any { it.packageName == myPackage }
    }

    private fun isBrokeySelected(): Boolean {
        val currentIme = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.DEFAULT_INPUT_METHOD
        )
        return currentIme != null && currentIme.contains(packageName)
    }
}
