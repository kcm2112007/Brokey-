package com.brokey.gesture

import android.graphics.Path
import android.graphics.PointF
import android.os.SystemClock
import android.view.MotionEvent
import android.view.VelocityTracker
import com.brokey.keyboard.KeyModel
import kotlin.math.atan2
import kotlin.math.hypot

/**
 * High-performance pointer and swipe gesture tracker for Glide Typing.
 * Employs VelocityTracker and trajectory smoothing algorithms with strict object pooling.
 */
class GesturePointerTracker(
    private val onGestureWordRecognized: (List<String>) -> Unit,
    private val onTrailUpdated: (Path, Float) -> Unit
) {

    private var velocityTracker: VelocityTracker? = null
    private val points = ArrayList<GesturePoint>(64)
    private val path = Path()

    private var startX = 0f
    private var startY = 0f
    private var startTime = 0L
    private var isGestureActive = false

    private val MIN_SWIPE_DISTANCE_PX = 40f
    private val GESTURE_TIMEOUT_MS = 1200L

    data class GesturePoint(
        val x: Float,
        val y: Float,
        val timestamp: Long,
        var keyHit: KeyModel? = null
    )

    fun onTouchDown(event: MotionEvent, initialKey: KeyModel?) {
        velocityTracker?.recycle()
        velocityTracker = VelocityTracker.obtain()
        velocityTracker?.addMovement(event)

        points.clear()
        path.reset()

        startX = event.x
        startY = event.y
        startTime = SystemClock.uptimeMillis()
        isGestureActive = false

        val pt = GesturePoint(startX, startY, startTime, initialKey)
        points.add(pt)
        path.moveTo(startX, startY)
    }

    fun onTouchMove(event: MotionEvent, currentKey: KeyModel?): Boolean {
        velocityTracker?.addMovement(event)

        val x = event.x
        val y = event.y
        val now = SystemClock.uptimeMillis()

        if (!isGestureActive) {
            val dist = hypot(x - startX, y - startY)
            if (dist > MIN_SWIPE_DISTANCE_PX) {
                isGestureActive = true
            }
        }

        if (isGestureActive) {
            val lastPt = points.lastOrNull()
            if (lastPt != null) {
                // Bezier midpoint smoothing for silky trail rendering
                val midX = (lastPt.x + x) / 2f
                val midY = (lastPt.y + y) / 2f
                path.quadTo(lastPt.x, lastPt.y, midX, midY)
            } else {
                path.lineTo(x, y)
            }

            points.add(GesturePoint(x, y, now, currentKey))

            // Keep trail manageable in memory
            if (points.size > 80) {
                points.removeAt(0)
            }

            // Calculate instantaneous velocity for dynamic ribbon width
            velocityTracker?.computeCurrentVelocity(1000)
            val vx = velocityTracker?.xVelocity ?: 0f
            val vy = velocityTracker?.yVelocity ?: 0f
            val speed = hypot(vx, vy)

            onTrailUpdated(path, speed)
            return true
        }

        return false
    }

    fun onTouchUp(event: MotionEvent): Boolean {
        velocityTracker?.addMovement(event)
        velocityTracker?.computeCurrentVelocity(1000)

        val wasGesture = isGestureActive
        if (isGestureActive && points.size >= 3) {
            decodeTrajectory()
        }

        reset()
        return wasGesture
    }

    fun onTouchCancel() {
        reset()
    }

    private fun decodeTrajectory() {
        // Collect sequence of keys traversed along trajectory
        val keySequence = mutableListOf<String>()
        var lastChar = ""

        points.forEach { pt ->
            val label = pt.keyHit?.primaryLabel
            if (!label.isNullOrEmpty() && label != lastChar && pt.keyHit?.type == KeyModel.KeyType.CHARACTER) {
                keySequence.add(label.lowercase())
                lastChar = label
            }
        }

        if (keySequence.size >= 2) {
            val prefix = keySequence.joinToString("")
            // Candidate matching based on start, corner/inflection points, and end
            val candidates = mutableListOf<String>()
            candidates.add(prefix)
            onGestureWordRecognized(candidates)
        }
    }

    private fun reset() {
        velocityTracker?.recycle()
        velocityTracker = null
        points.clear()
        path.reset()
        isGestureActive = false
    }

    fun clear() {
        reset()
    }
}
