package com.vungn.backvietlibrary.util.extension

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.TypedValue
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.vungn.backvietlibrary.BuildConfig

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

fun Activity.hideSoftKeyboard() {
    val inputMethodManager = this.getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    if (inputMethodManager.isAcceptingText()) {
        inputMethodManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            0
        )
    }
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

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
    return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

fun Context.dimensionFromAttribute(attribute: Int): Int {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val dimension = attributes.getDimensionPixelSize(0, 0)
    attributes.recycle()
    return dimension
}

@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}

fun String.toAvatarUrl(): String {
    return "${BuildConfig.BASE_URL}$this"
}