package com.vungn.backvietlibrary.util

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorInt

fun View.startAlphaAnimation(duration: Long, visibility: Int) {
    val alphaAnimation =
        if (visibility == View.VISIBLE) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
    alphaAnimation.setDuration(duration)
    alphaAnimation.fillAfter = true
    this.startAnimation(alphaAnimation)
}

fun View.startBackgroundAnimation(duration: Long, colorFrom: Int, colorTo: Int) {
    val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimator.setDuration(duration)
    colorAnimator.addUpdateListener { animator ->
        this.setBackgroundColor(animator.animatedValue as Int)
    }
    colorAnimator.start()
}

@ColorInt
fun Context.getColorAttr(attr: Int): Int {
    val typedValue = TypedValue()
    val theme = this.theme
    theme.resolveAttribute(attr, typedValue, true)
    @ColorInt val color = typedValue.data
    return color
}

fun EditText.focus() {
    text?.let { setSelection(it.length) }
    postDelayed({
        requestFocus()
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val isShowing = imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        if (!isShowing) {
            imm.toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }, 200)
}