package com.brokey.keyboard

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import com.brokey.ai.InlineAiAssistant
import com.brokey.transliteration.TransliterationEngine

/**
 * Premium Gboard-Caliber Keyboard Root Container.
 * Assembles:
 * 1. Dynamic Gboard-Style Top Bar (Candidates, Quick Toolbar, Inline AI, Sticky Clipboard)
 * 2. High-Performance Canvas Keyboard View (< 5ms Latency, Hold-to-delete engine, Zero-GC onDraw)
 */
class KeyboardView @JvmOverloads constructor(
    context: Context,
    private val controller: KeyboardController? = null,
    private val onKeyPressed: ((KeyModel) -> Unit)? = null,
    private val onBackspaceTriggered: ((Boolean) -> Unit)? = null,
    private val onCandidateSelected: ((TransliterationEngine.Candidate) -> Unit)? = null,
    private val onToolbarActionTriggered: ((String) -> Unit)? = null,
    private val onClipPasted: ((String) -> Unit)? = null,
    private val onAiRewriteRequested: ((InlineAiAssistant.RewriteStyle) -> Unit)? = null,
    private val onGestureWordRecognized: ((List<String>) -> Unit)? = null,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val topBarView: KeyboardTopBarView
    val canvasKeyboardView: CanvasKeyboardView

    init {
        orientation = VERTICAL
        setBackgroundColor(Color.parseColor("#181A1F"))

        // 1. Gboard-Style Top Bar
        topBarView = KeyboardTopBarView(context).apply {
            this.onCandidateSelected = { candidate ->
                this@KeyboardView.onCandidateSelected?.invoke(candidate)
            }
            this.onToolbarAction = { actionId ->
                this@KeyboardView.onToolbarActionTriggered?.invoke(actionId)
            }
            this.onClipSelected = { text ->
                this@KeyboardView.onClipPasted?.invoke(text)
            }
            this.onAiRewriteRequested = { style ->
                this@KeyboardView.onAiRewriteRequested?.invoke(style)
            }
        }
        addView(topBarView)

        // 2. High-Performance Canvas Keyboard View
        canvasKeyboardView = CanvasKeyboardView(context).apply {
            this.controller = this@KeyboardView.controller
            this.onKeyPressed = { key ->
                this@KeyboardView.onKeyPressed?.invoke(key)
            }
            this.onBackspaceTriggered = { deleteWord ->
                this@KeyboardView.onBackspaceTriggered?.invoke(deleteWord)
            }
            this.onGestureWordRecognized = { words ->
                this@KeyboardView.onGestureWordRecognized?.invoke(words)
            }
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 650) // Responsive touch surface
        }
        addView(canvasKeyboardView)
    }

    fun setCandidates(candidates: List<TransliterationEngine.Candidate>) {
        topBarView.setCandidates(candidates)
    }

    fun refreshLayout() {
        canvasKeyboardView.controller = controller
        canvasKeyboardView.refreshLayout()
    }

    fun stopAllRepeats() {
        canvasKeyboardView.stopBackspaceRepeat()
    }

    fun clearMemory() {
        canvasKeyboardView.clearMemory()
    }
}
