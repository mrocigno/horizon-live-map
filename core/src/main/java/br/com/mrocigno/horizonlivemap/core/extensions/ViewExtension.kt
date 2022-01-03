package br.com.mrocigno.horizonlivemap.core.extensions

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import java.time.Duration

fun View.gone() { visibility = View.GONE }
fun View.visible() { visibility = View.VISIBLE }
fun View.invisible() { visibility = View.INVISIBLE }
var View.isVisibleFade: Boolean
    get() = true
    set(value) {
        if (value) fadeIn() else fadeOut()
    }

fun View.setPaddingLeft(padding: Int) {
    setPadding(
        padding,
        paddingTop,
        paddingRight,
        paddingBottom
    )
}

fun View.setPaddingTop(padding: Int) {
    setPadding(
        paddingLeft,
        padding,
        paddingRight,
        paddingBottom
    )
}

fun View.setPaddingRight(padding: Int) {
    setPadding(
        paddingLeft,
        paddingTop,
        padding,
        paddingBottom
    )
}

fun View.setPaddingBottom(padding: Int) {
    setPadding(
        paddingLeft,
        paddingTop,
        paddingRight,
        padding
    )
}

fun View.fadeOut(duration: Long = 300, antiLoop: Boolean = true) {
    if (antiLoop && (isEnabled || isGone)) return
    animate()
        .alphaBy(1f)
        .alpha(0f)
        .setDuration(duration)
        .withStartAction {
            visible()
            isEnabled = true
        }
        .withEndAction {
            gone()
            isEnabled = false
        }
        .start()
}

fun View.fadeIn(duration: Long = 300, antiLoop: Boolean = true) {
    if (antiLoop && isVisible) return
    animate()
        .alphaBy(0f)
        .alpha(1f)
        .setDuration(duration)
        .withStartAction { visible() }
        .start()
}
