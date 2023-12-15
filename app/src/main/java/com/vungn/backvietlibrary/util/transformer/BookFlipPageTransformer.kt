/*
Copyright 2018 Wajahat Karim

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.vungn.backvietlibrary.util.transformer

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

/**
 * A book based page flip animation PageTransformer implementation for ViewPager
 *
 * Set the object of this transformer to any ViewPager object.
 * For example, myViewPager.setPageTransformer(true, new BookFlipPageTransformer());
 *
 * @see [EasyFlipViewPager](http://github.com/wajahatkarim3/EasyFlipViewPager)
 *
 *
 * @author Wajahat Karim (http://wajahatkarim.com)
 */
class BookFlipPageTransformer : ViewPager.PageTransformer {
    private var _scaleAmountPercent = 5f

    //endregion
    private var _isEnableScale = true

    var scaleAmountPercent: Float
        get() = _scaleAmountPercent
        set(value) {
            _scaleAmountPercent = value
        }

    var isEnableScale: Boolean
        get() = _isEnableScale
        set(value) {
            _isEnableScale = value
        }

    override fun transformPage(page: View, position: Float) {
        val percentage = (1 - abs(position.toDouble())).toFloat()
        // Don't move pages once they are on left or right
        if (position > CENTER && position <= RIGHT) {
            // This is behind page
            page.translationX = -position * page.width
            page.translationY = 0f
            page.rotation = 0f
            if (_isEnableScale) {
                val amount = (100 - _scaleAmountPercent + _scaleAmountPercent * percentage) / 100
                setSize(page, position, amount)
            }
        } else {
            page.visibility = View.VISIBLE
            flipPage(page, position, percentage)
        }
    }

    private fun flipPage(page: View, position: Float, percentage: Float) {
        // Flip this page
        page.setCameraDistance(-12000f)
        setVisibility(page, position)
        setTranslation(page)
        setPivot(page, 0f, page.height * 0.5f)
        setRotation(page, position, percentage)
    }

    private fun setPivot(page: View, pivotX: Float, pivotY: Float) {
        page.pivotX = pivotX
        page.pivotY = pivotY
    }

    private fun setVisibility(page: View, position: Float) {
        if (position < 0.5 && position > -0.5) {
            page.visibility = View.VISIBLE
        } else {
            page.visibility = View.INVISIBLE
        }
    }

    private fun setTranslation(page: View) {
        val viewPager = page.parent as ViewPager
        val scroll = viewPager.scrollX - page.left
        page.translationX = scroll.toFloat()
    }

    private fun setSize(page: View, position: Float, percentage: Float) {
        page.scaleX = if (position != 0f && position != 1f) percentage else 1f
        page.scaleY = if (position != 0f && position != 1f) percentage else 1f
    }

    private fun setRotation(page: View, position: Float, percentage: Float) {
        if (position > 0) {
            page.rotationY = -180 * (percentage + 1)
        } else {
            page.rotationY = 180 * (percentage + 1)
        }
    }

    companion object {
        private const val LEFT = -1
        private const val RIGHT = 1
        private const val CENTER = 0
    }
}
