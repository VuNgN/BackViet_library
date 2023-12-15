package com.vungn.backvietlibrary.ui.customview

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import kotlin.math.abs

class ImageViewTouchViewPager : ViewPager {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        return if (v is ImageViewTouch) {
            imageViewTouchCanScroll(v, dx)
        } else {
            super.canScroll(v, checkV, dx, x, y)
        }
    }

    /**
     * Determines whether the ImageViewTouch can be scrolled.
     *
     * @param direction - positive direction value means scroll from right to left,
     * negative value means scroll from left to right
     * @return true if there is some more place to scroll, false - otherwise.
     */
    private fun imageViewTouchCanScroll(imageViewTouch: ImageViewTouch, direction: Int): Boolean {
        val widthScreen = widthScreen
        val bitmapRect = imageViewTouch.bitmapRect
        val imageViewRect = Rect()
        getGlobalVisibleRect(imageViewRect)
        val widthBitmapViewTouch = bitmapRect!!.width().toInt()
        if (widthBitmapViewTouch < widthScreen) {
            return false
        }
        return if (direction < 0) {
            abs((bitmapRect.right - imageViewRect.right).toDouble()) > 1.0f
        } else {
            abs((bitmapRect.left - imageViewRect.left).toDouble()) > 1.0f
        }
    }

    private val widthScreen: Int
        get() {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.x
        }
}
