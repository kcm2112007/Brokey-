package com.brokey.keyboard

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.brokey.ai.InlineAiAssistant
import com.brokey.clipboard.ClipItem
import com.brokey.clipboard.ClipboardManager
import com.brokey.transliteration.TransliterationEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Gboard-Style Dynamic Top Bar.
 * Integrates:
 * 1. Asynchronous Candidate Suggestion Strip (zero UI blocking)
 * 2. Quick Action Toolbar (AI, Clipboard, Language, Theme, Settings, Voice)
 * 3. Inline AI Assistant Panel (Rewrite, Grammar, Tone)
 * 4. Sticky Clipboard Manager Drawer
 */
class KeyboardTopBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    enum class BarMode {
        CANDIDATES,
        TOOLBAR,
        AI_PANEL,
        CLIPBOARD_PANEL
    }

    var currentMode: BarMode = BarMode.TOOLBAR
        private set

    var onCandidateSelected: ((TransliterationEngine.Candidate) -> Unit)? = null
    var onToolbarAction: ((String) -> Unit)? = null
    var onClipSelected: ((String) -> Unit)? = null
    var onAiRewriteRequested: ((InlineAiAssistant.RewriteStyle) -> Unit)? = null

    // Containers
    private val contentContainer: LinearLayout
    private val candidateScrollView: HorizontalScrollView
    private val candidateContainer: LinearLayout

    private val toolbarContainer: LinearLayout
    private val aiPanelContainer: LinearLayout
    private val clipboardPanelContainer: LinearLayout

    private val toggleModeBtn: TextView
    private val aiAssistant = InlineAiAssistant()
    private val uiScope = CoroutineScope(Dispatchers.Main + Job())

    init {
        orientation = VERTICAL
        setBackgroundColor(Color.parseColor("#181A1F"))
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        // Main bar container
        val mainRow = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(8, 4, 8, 4)
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 120) // 120px fixed height
        }

        // Toggle button on far left (> chevron)
        toggleModeBtn = TextView(context).apply {
            text = "›"
            textSize = 22f
            setTextColor(Color.parseColor("#94A3B8"))
            setPadding(24, 0, 24, 0)
            gravity = Gravity.CENTER
            setOnClickListener {
                when (currentMode) {
                    BarMode.CANDIDATES -> switchMode(BarMode.TOOLBAR)
                    BarMode.TOOLBAR -> switchMode(BarMode.CANDIDATES)
                    else -> switchMode(BarMode.TOOLBAR)
                }
            }
        }
        mainRow.addView(toggleModeBtn)

        // Center content flipper container
        contentContainer = LinearLayout(context).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f)
            gravity = Gravity.CENTER_VERTICAL
        }
        mainRow.addView(contentContainer)

        // 1. Candidate Strip
        candidateScrollView = HorizontalScrollView(context).apply {
            isHorizontalScrollBarEnabled = false
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        candidateContainer = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(4, 0, 4, 0)
        }
        candidateScrollView.addView(candidateContainer)

        // 2. Toolbar Strip (Gboard Action Icons)
        toolbarContainer = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setupToolbarIcons(this)
        }

        // 3. AI Assistant Panel
        aiPanelContainer = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setupAiPanel(this)
        }

        // 4. Clipboard Panel Strip
        clipboardPanelContainer = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }

        // Default initial view: Toolbar
        contentContainer.addView(toolbarContainer)
        addView(mainRow)
    }

    private fun setupToolbarIcons(container: LinearLayout) {
        val actions = listOf(
            Triple("✨ AI Assist", "AI", "#FF9933"),
            Triple("📋 Clipboard", "CLIPBOARD", "#38BDF8"),
            Triple("🌐 Lang", "LANG", "#A855F7"),
            Triple("🔤 Script", "TRANSLIT", "#34D399"),
            Triple("🎨 Themes", "THEME", "#F472B6"),
            Triple("⚙️ Settings", "SETTINGS", "#94A3B8"),
            Triple("🎙️ Voice", "VOICE", "#FBBF24")
        )

        for ((label, actionId, colorHex) in actions) {
            val btn = TextView(context).apply {
                text = label
                textSize = 12f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
                setPadding(24, 12, 24, 12)

                val shape = GradientDrawable().apply {
                    cornerRadius = 20f
                    setColor(Color.parseColor("#262A32"))
                    setStroke(2, Color.parseColor(colorHex))
                }
                background = shape

                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    marginEnd = 12
                }
                layoutParams = params

                setOnClickListener {
                    when (actionId) {
                        "AI" -> switchMode(BarMode.AI_PANEL)
                        "CLIPBOARD" -> {
                            refreshClipboardPanel()
                            switchMode(BarMode.CLIPBOARD_PANEL)
                        }
                        else -> onToolbarAction?.invoke(actionId)
                    }
                }
            }
            container.addView(btn)
        }
    }

    private fun setupAiPanel(container: LinearLayout) {
        val backBtn = TextView(context).apply {
            text = "‹ Back"
            textSize = 12f
            setTextColor(Color.parseColor("#FF9933"))
            setPadding(16, 12, 16, 12)
            setOnClickListener { switchMode(BarMode.TOOLBAR) }
        }
        container.addView(backBtn)

        val aiStyles = listOf(
            "Formal" to InlineAiAssistant.RewriteStyle.PROFESSIONAL,
            "Casual" to InlineAiAssistant.RewriteStyle.CASUAL,
            "Concise" to InlineAiAssistant.RewriteStyle.CONCISE,
            "Fix Grammar" to InlineAiAssistant.RewriteStyle.GRAMMAR_ONLY,
            "Elaborate" to InlineAiAssistant.RewriteStyle.ELABORATE
        )

        val scroll = HorizontalScrollView(context).apply {
            isHorizontalScrollBarEnabled = false
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        val chipContainer = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        for ((label, style) in aiStyles) {
            val chip = TextView(context).apply {
                text = label
                textSize = 12f
                setTextColor(Color.WHITE)
                setPadding(20, 10, 20, 10)
                background = GradientDrawable().apply {
                    cornerRadius = 16f
                    setColor(Color.parseColor("#342A20"))
                    setStroke(2, Color.parseColor("#FF9933"))
                }
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    marginEnd = 10
                }
                setOnClickListener {
                    onAiRewriteRequested?.invoke(style)
                }
            }
            chipContainer.addView(chip)
        }

        scroll.addView(chipContainer)
        container.addView(scroll)
    }

    fun refreshClipboardPanel() {
        clipboardPanelContainer.removeAllViews()

        val backBtn = TextView(context).apply {
            text = "‹ Back"
            textSize = 12f
            setTextColor(Color.parseColor("#38BDF8"))
            setPadding(16, 12, 16, 12)
            setOnClickListener { switchMode(BarMode.TOOLBAR) }
        }
        clipboardPanelContainer.addView(backBtn)

        val clips = ClipboardManager.getInstance(context).getAllClips()
        if (clips.isEmpty()) {
            val emptyMsg = TextView(context).apply {
                text = "Clipboard is empty"
                textSize = 12f
                setTextColor(Color.parseColor("#64748B"))
                setPadding(24, 0, 0, 0)
            }
            clipboardPanelContainer.addView(emptyMsg)
            return
        }

        val scroll = HorizontalScrollView(context).apply {
            isHorizontalScrollBarEnabled = false
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        val clipsRow = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        for (clip in clips.take(8)) {
            val chip = LinearLayout(context).apply {
                orientation = HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(16, 8, 16, 8)
                background = GradientDrawable().apply {
                    cornerRadius = 16f
                    setColor(if (clip.isPinned) Color.parseColor("#1E293B") else Color.parseColor("#18202F"))
                    setStroke(2, if (clip.isPinned) Color.parseColor("#38BDF8") else Color.parseColor("#334155"))
                }
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    marginEnd = 10
                }
                layoutParams = params

                val tv = TextView(context).apply {
                    text = if (clip.text.length > 20) clip.text.take(20) + "…" else clip.text
                    textSize = 12f
                    setTextColor(Color.WHITE)
                    maxLines = 1
                }
                addView(tv)

                val pinBtn = TextView(context).apply {
                    text = if (clip.isPinned) " 📌" else " 📍"
                    textSize = 11f
                    setOnClickListener {
                        ClipboardManager.getInstance(context).togglePin(clip.id)
                        refreshClipboardPanel()
                    }
                }
                addView(pinBtn)

                setOnClickListener {
                    onClipSelected?.invoke(clip.text)
                }
            }
            clipsRow.addView(chip)
        }

        scroll.addView(clipsRow)
        clipboardPanelContainer.addView(scroll)
    }

    /**
     * Efficiently updates suggestion candidates without blocking main thread.
     */
    fun setCandidates(candidates: List<TransliterationEngine.Candidate>) {
        post {
            candidateContainer.removeAllViews()

            if (candidates.isEmpty()) {
                if (currentMode == BarMode.CANDIDATES) {
                    switchMode(BarMode.TOOLBAR)
                }
                return@post
            }

            // Switch into candidate mode if not actively using AI or Clipboard panel
            if (currentMode != BarMode.AI_PANEL && currentMode != BarMode.CLIPBOARD_PANEL) {
                switchMode(BarMode.CANDIDATES)
            }

            candidates.forEachIndexed { index, candidate ->
                val isPrimary = index == 0 || candidate.isExactMatch
                val chip = TextView(context).apply {
                    text = candidate.word
                    textSize = if (isPrimary) 15f else 14f
                    typeface = if (isPrimary) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                    setTextColor(if (isPrimary) Color.parseColor("#FB923C") else Color.parseColor("#F1F5F9"))
                    gravity = Gravity.CENTER
                    setPadding(30, 10, 30, 10)

                    background = GradientDrawable().apply {
                        cornerRadius = 18f
                        setColor(if (isPrimary) Color.parseColor("#2D2319") else Color.parseColor("#252930"))
                        if (isPrimary) {
                            setStroke(2, Color.parseColor("#F97316"))
                        }
                    }

                    val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        marginStart = 4
                        marginEnd = 8
                    }
                    layoutParams = params

                    setOnClickListener {
                        onCandidateSelected?.invoke(candidate)
                    }
                }
                candidateContainer.addView(chip)
            }
        }
    }

    fun switchMode(mode: BarMode) {
        currentMode = mode
        contentContainer.removeAllViews()

        when (mode) {
            BarMode.CANDIDATES -> {
                toggleModeBtn.text = "›"
                contentContainer.addView(candidateScrollView)
            }
            BarMode.TOOLBAR -> {
                toggleModeBtn.text = "‹"
                contentContainer.addView(toolbarContainer)
            }
            BarMode.AI_PANEL -> {
                toggleModeBtn.text = "‹"
                contentContainer.addView(aiPanelContainer)
            }
            BarMode.CLIPBOARD_PANEL -> {
                toggleModeBtn.text = "‹"
                contentContainer.addView(clipboardPanelContainer)
            }
        }
    }
}
