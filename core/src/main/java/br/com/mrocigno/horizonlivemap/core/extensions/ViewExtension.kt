package br.com.mrocigno.horizonlivemap.core.extensions

import android.view.View

fun View.gone() { visibility = View.GONE }
fun View.visible() { visibility = View.VISIBLE }
fun View.invisible() { visibility = View.INVISIBLE }

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