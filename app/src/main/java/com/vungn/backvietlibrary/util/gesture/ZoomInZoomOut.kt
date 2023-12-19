package com.vungn.backvietlibrary.util.gesture

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import androidx.core.graphics.values
import com.vungn.backvietlibrary.util.listener.OnImageZoom
import com.vungn.backvietlibrary.util.listener.OnSinglePress
import kotlin.math.sqrt


class ZoomInZoomOut(context: Context, private val scaleX: Float) : OnTouchListener {
    private lateinit var view: ImageView
    private var _onSinglePressListener: OnSinglePress? = null
    private var _onImageZoom: OnImageZoom? = null
    private var _isMovable = false
    var onSinglePress: OnSinglePress?
        get() = _onSinglePressListener
        set(value) {
            _onSinglePressListener = value
        }
    var onImageZoom: OnImageZoom?
        get() = _onImageZoom
        set(value) {
            _onImageZoom = value
        }

    // These matrices will be used to scale points of the image
    private var matrix = Matrix()
    private var savedMatrix = Matrix()
    private var mode = NONE

    // these PointF objects are used to record the point(s) the user is touching
    private var start = PointF()
    private var mid = PointF()
    private var oldDist = 1f

    private val gestureDetector: GestureDetector =
        GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                matrix.postScale(
                    scaleX / matrix.values()[Matrix.MSCALE_X],
                    scaleX / matrix.values()[Matrix.MSCALE_Y],
                    mid.x,
                    mid.y
                )
                _onImageZoom?.onImageZoom(false)
                _isMovable = false
                view.imageMatrix = matrix
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                _onSinglePressListener?.onSinglePress()
                return true
            }
        })

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        v.performClick()
        view = v as ImageView
        view.setScaleType(ImageView.ScaleType.MATRIX)
        val scale: Float
        dumpEvent(event)
        gestureDetector.onTouchEvent(event)
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                matrix.set(view.getImageMatrix())
                savedMatrix.set(matrix)
                start[event.x] = event.y
                Log.d(TAG, "mode=DRAG") // write to LogCat
                mode = DRAG
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
                Log.d(TAG, "mode=NONE")
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                Log.d(TAG, "oldDist=$oldDist")
                if (oldDist > 5f) {
                    savedMatrix.set(matrix)
                    midPoint(mid, event)
                    mode = ZOOM
                    Log.d(TAG, "mode=ZOOM")
                }
            }

            MotionEvent.ACTION_MOVE -> if (mode == DRAG || mode == MOVE) {
                mode = MOVE
                Log.d(TAG, "mode=MOVE")
                if (_isMovable) {
                    matrix.set(savedMatrix)
                    matrix.postTranslate(
                        event.x - start.x, event.y - start.y
                    ) // create the transformation in the matrix  of points
                }
            } else if (mode == ZOOM) {
                // pinch zooming
                val f = FloatArray(9)
                val newDist = spacing(event)
                Log.d(TAG, "newDist=$newDist")
                if (newDist > 5f) {
                    matrix.set(savedMatrix)
                    scale = newDist / oldDist // setting the scaling of the
                    // matrix...if scale > 1 means
                    // zoom in...if scale < 1 means
                    // zoom out
                    matrix.postScale(scale, scale, mid.x, mid.y)
                    matrix.getValues(f)
                    val scaleX = f[Matrix.MSCALE_X]
                    val scaleY = f[Matrix.MSCALE_Y]
                    Log.d(TAG, "on zoom: [$scaleX, $scaleY]")
                    if (scaleX <= this.scaleX) {
                        matrix.postScale(
                            (this.scaleX) / scaleX, (this.scaleX) / scaleY, mid.x, mid.y
                        )
                        _onImageZoom?.onImageZoom(false)
                        _isMovable = false
                    } else if (scaleX >= MAX_ZOOM) {
                        matrix.postScale((MAX_ZOOM) / scaleX, (MAX_ZOOM) / scaleY, mid.x, mid.y)
                        _onImageZoom?.onImageZoom(true)
                        _isMovable = true
                    } else {
                        _onImageZoom?.onImageZoom(true)
                        _isMovable = true
                    }
                }
            }
        }
        view.setImageMatrix(matrix) // display the transformation on screen
        return true // indicate event was handled
    }

    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */
    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */
    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point[x / 2] = y / 2
    }

    /** Show an event in the LogCat view, for debugging  */
    private fun dumpEvent(event: MotionEvent) {
        val names = arrayOf(
            "DOWN",
            "UP",
            "MOVE",
            "CANCEL",
            "OUTSIDE",
            "POINTER_DOWN",
            "POINTER_UP",
            "7?",
            "8?",
            "9?"
        )
        val sb = StringBuilder()
        val action = event.action
        val actionCode = action and MotionEvent.ACTION_MASK
        sb.append("event ACTION_").append(names[actionCode])
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(action shr MotionEvent.ACTION_POINTER_ID_SHIFT)
            sb.append(")")
        }
        sb.append("[")
        for (i in 0 until event.pointerCount) {
            sb.append("#").append(i)
            sb.append("(pid ").append(event.getPointerId(i))
            sb.append(")=").append(event.getX(i).toInt())
            sb.append(",").append(event.getY(i).toInt())
            if (i + 1 < event.pointerCount) sb.append(";")
        }
        sb.append("]")
        Log.d("Touch Events ---------", sb.toString())
    }

    companion object {
        private const val TAG = "Touch"

        private const val MIN_ZOOM = 0f

        private const val MAX_ZOOM = 15.0f

        // The 3 states (events) which the user is trying to perform
        const val NONE = 0
        const val DRAG = 1
        const val ZOOM = 2
        const val MOVE = 3
    }
}