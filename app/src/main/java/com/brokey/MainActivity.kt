package com.brokey

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.brokey.settings.KeyboardSettingsActivity

/**
 * Main activation and management activity for BROKEY keyboard.
 * Features:
 * - Dynamic real-time activation status tracker
 * - Guided 2-step setup with completion badges
 * - Integrated typing sandbox with hold-to-delete testing
 * - Fast access to 22 Indian language profiles and preferences
 */
class MainActivity : AppCompatActivity() {

    private lateinit var statusBadge: TextView
    private lateinit var statusIndicator: TextView
    private lateinit var statusDesc: TextView
    private lateinit var step1Num: TextView
    private lateinit var step1Check: TextView
    private lateinit var step2Num: TextView
    private lateinit var step2Check: TextView
    private lateinit var btnEnable: Button
    private lateinit var btnSelect: Button
    private lateinit var btnSettings: Button
    private lateinit var btnLanguages: Button
    private lateinit var testInput: EditText
    private lateinit var btnClearTest: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusBadge = findViewById(R.id.tv_status_badge)
        statusIndicator = findViewById(R.id.tv_status_indicator)
        statusDesc = findViewById(R.id.tv_status_desc)
        step1Num = findViewById(R.id.tv_step_1_num)
        step1Check = findViewById(R.id.tv_step_1_check)
        step2Num = findViewById(R.id.tv_step_2_num)
        step2Check = findViewById(R.id.tv_step_2_check)
        btnEnable = findViewById(R.id.btn_enable_keyboard)
        btnSelect = findViewById(R.id.btn_select_keyboard)
        btnSettings = findViewById(R.id.btn_open_settings)
        btnLanguages = findViewById(R.id.btn_open_languages)
        testInput = findViewById(R.id.et_test_typing)
        btnClearTest = findViewById(R.id.btn_clear_test)

        btnEnable.setOnClickListener {
            // Open Android System Input Method Settings
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        btnSelect.setOnClickListener {
            // Display Android System Input Method Chooser dialog
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showInputMethodPicker()
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, KeyboardSettingsActivity::class.java))
        }

        btnLanguages.setOnClickListener {
            startActivity(Intent(this, KeyboardSettingsActivity::class.java))
        }

        btnClearTest.setOnClickListener {
            testInput.text.clear()
        }
    }

    override fun onResume() {
        super.onResume()
        updateKeyboardStatus()
    }

    /**
     * Automatically inspects Android system settings to determine BROKEY's activation state
     */
    private fun updateKeyboardStatus() {
        val isEnabled = isBrokeyEnabled()
        val isSelected = isBrokeySelected()

        when {
            isSelected -> {
                // Fully Active & Selected
                statusBadge.text = "ACTIVE"
                statusBadge.setBackgroundColor(Color.parseColor("#064E3B")) // Emerald Dark
                statusBadge.setTextColor(Color.parseColor("#34D399"))

                statusIndicator.text = "BROKEY is your active keyboard!"
                statusIndicator.setTextColor(Color.parseColor("#34D399"))

                statusDesc.text = "You are ready to type in English and 22 Indian languages across WhatsApp, Telegram, Chrome, and all apps."

                // Step 1 Completed
                step1Check.visibility = View.VISIBLE
                btnEnable.isEnabled = false
                btnEnable.text = "Step 1: Enabled ✓"
                btnEnable.setBackgroundColor(Color.parseColor("#1E293B"))

                // Step 2 Completed
                step2Check.visibility = View.VISIBLE
                btnSelect.isEnabled = false
                btnSelect.text = "Step 2: Selected as Default ✓"
                btnSelect.setBackgroundColor(Color.parseColor("#1E293B"))
            }

            isEnabled -> {
                // Enabled in Settings, but not selected as default
                statusBadge.text = "READY FOR STEP 2"
                statusBadge.setBackgroundColor(Color.parseColor("#78350F")) // Warm Amber Dark
                statusBadge.setTextColor(Color.parseColor("#FBBF24"))

                statusIndicator.text = "Enabled — Select BROKEY as default"
                statusIndicator.setTextColor(Color.parseColor("#FBBF24"))

                statusDesc.text = "BROKEY is turned on in System Settings. Tap Step 2 to choose it as your active keyboard."

                // Step 1 Completed
                step1Check.visibility = View.VISIBLE
                btnEnable.isEnabled = false
                btnEnable.text = "Step 1: Enabled ✓"
                btnEnable.setBackgroundColor(Color.parseColor("#1E293B"))

                // Step 2 Active
                step2Check.visibility = View.GONE
                btnSelect.isEnabled = true
                btnSelect.text = "Step 2: Choose BROKEY Input Method"
                btnSelect.setBackgroundColor(Color.parseColor("#F97316")) // Accent orange
            }

            else -> {
                // Not yet enabled
                statusBadge.text = "NOT ENABLED"
                statusBadge.setBackgroundColor(Color.parseColor("#334155")) // Muted Slate
                statusBadge.setTextColor(Color.parseColor("#94A3B8"))

                statusIndicator.text = "BROKEY is not yet enabled"
                statusIndicator.setTextColor(Color.parseColor("#F8FAFC"))

                statusDesc.text = "Tap Step 1 below to open Android Settings and turn ON the BROKEY keyboard."

                // Step 1 Active
                step1Check.visibility = View.GONE
                btnEnable.isEnabled = true
                btnEnable.text = "Step 1: Enable BROKEY in Settings"
                btnEnable.setBackgroundColor(Color.parseColor("#F97316")) // Accent orange

                // Step 2 Disabled
                step2Check.visibility = View.GONE
                btnSelect.isEnabled = false
                btnSelect.text = "Step 2: Choose BROKEY (Complete Step 1 first)"
                btnSelect.setBackgroundColor(Color.parseColor("#22262E"))
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
