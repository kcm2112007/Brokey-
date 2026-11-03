package com.brokey.keyboard

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import com.brokey.BrokeyApplication
import com.brokey.R
import com.brokey.transliteration.TransliterationEngine

/**
 * Custom View container rendering the BROKEY keyboard.
 * Includes suggestion candidate strip, action strip, and dynamic rows of rounded keys.
 */
class KeyboardView @JvmOverloads constructor(
    context: Context,
    private val controller: KeyboardController? = null,
    private val onKeyPressed: ((KeyModel) -> Unit)? = null,
    private val onCandidateSelected: ((TransliterationEngine.Candidate) -> Unit)? = null,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val candidateContainer: LinearLayout
    private val candidateScrollView: HorizontalScrollView
    private val keysLayoutContainer: LinearLayout
    private val actionStripContainer: LinearLayout
    private val langIndicatorView: TextView

    init {
        orientation = VERTICAL
        setBackgroundColor(Color.parseColor("#16181C")) // Default dark charcoal

        // 1. Action strip (Quick Language Switcher, Transliteration mode toggle, theme)
        actionStripContainer = LinearLayout(context).apply {
            orientation = HORIZONTAL
            setPadding(16, 12, 16, 12)
            setBackgroundColor(Color.parseColor("#1F2329"))
            gravity = Gravity.CENTER_VERTICAL
        }

        langIndicatorView = TextView(context).apply {
            text = "BROKEY · " + (controller?.activeLanguage?.nativeName ?: "English")
            setTextColor(Color.parseColor("#FF9933"))
            textSize = 13f
            typeface = Typeface.DEFAULT_BOLD
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f)
        }
        actionStripContainer.addView(langIndicatorView)

        val btnTranslit = Button(context).apply {
            text = if (controller?.isTransliterationEnabled == true) "Phonetic: ON" else "Native Script"
            textSize = 11f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#2C323B"))
            setOnClickListener {
                controller?.toggleTransliteration()
                text = if (controller?.isTransliterationEnabled == true) "Phonetic: ON" else "Native Script"
            }
        }
        actionStripContainer.addView(btnTranslit)
        addView(actionStripContainer)

        // 2. Candidate suggestion strip
        candidateScrollView = HorizontalScrollView(context).apply {
            isHorizontalScrollBarEnabled = false
            setBackgroundColor(Color.parseColor("#20242A"))
        }
        candidateContainer = LinearLayout(context).apply {
            orientation = HORIZONTAL
            setPadding(12, 8, 12, 8)
        }
        candidateScrollView.addView(candidateContainer)
        addView(candidateScrollView)

        // 3. Dynamic Key matrix container
        keysLayoutContainer = LinearLayout(context).apply {
            orientation = VERTICAL
            setPadding(6, 8, 6, 12)
        }
        addView(keysLayoutContainer)

        refreshLayout()
    }

    fun setCandidates(candidates: List<TransliterationEngine.Candidate>) {
        post {
            candidateContainer.removeAllViews()
            if (candidates.isEmpty()) {
                candidateScrollView.visibility = View.GONE
                return@post
            }

            candidateScrollView.visibility = View.VISIBLE
            candidates.forEach { candidate ->
                val btn = Button(context).apply {
                    text = candidate.word
                    textSize = 15f
                    setTextColor(if (candidate.isExactMatch) Color.parseColor("#FF9933") else Color.WHITE)
                    setBackgroundColor(Color.parseColor("#2B3038"))
                    setPadding(28, 8, 28, 8)
                    val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        marginEnd = 12
                    }
                    layoutParams = params
                    setOnClickListener {
                        onCandidateSelected?.invoke(candidate)
                    }
                }
                candidateContainer.addView(btn)
            }
        }
    }

    fun refreshLayout() {
        val ctrl = controller ?: return
        keysLayoutContainer.removeAllViews()
        langIndicatorView.text = "BROKEY · ${ctrl.activeLanguage.nativeName}"

        val rows = ctrl.getCurrentKeyLayout()
        for (row in rows) {
            val rowLayout = LinearLayout(context).apply {
                orientation = HORIZONTAL
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                    topMargin = 4
                    bottomMargin = 4
                }
            }

            for (key in row) {
                val keyView = createKeyButton(key, ctrl.isShifted || ctrl.isCapsLock)
                rowLayout.addView(keyView)
            }

            keysLayoutContainer.addView(rowLayout)
        }
    }

    private fun createKeyButton(key: KeyModel, shifted: Boolean): Button {
        return Button(context).apply {
            val displayLabel = if (shifted && key.shiftedLabel != null) key.shiftedLabel else key.primaryLabel
            text = displayLabel
            textSize = when {
                key.type == KeyModel.KeyType.SPACE -> 12f
                key.type == KeyModel.KeyType.MODE_CHANGE -> 12f
                displayLabel.length > 2 -> 12f
                else -> 18f
            }

            // Colors based on key type
            val bgColor = when (key.type) {
                KeyModel.KeyType.CHARACTER -> Color.parseColor("#2B3038")
                KeyModel.KeyType.SPACE -> Color.parseColor("#2B3038")
                KeyModel.KeyType.ENTER -> Color.parseColor("#FF9933")
                else -> Color.parseColor("#20242A")
            }
            setBackgroundColor(bgColor)

            val textColor = when (key.type) {
                KeyModel.KeyType.ENTER -> Color.BLACK
                else -> Color.parseColor("#F8FAFC")
            }
            setTextColor(textColor)

            layoutParams = LayoutParams(0, 140, key.weight).apply {
                marginStart = 3
                marginEnd = 3
            }

            setOnClickListener {
                onKeyPressed?.invoke(key)
            }
        }
    }
}
