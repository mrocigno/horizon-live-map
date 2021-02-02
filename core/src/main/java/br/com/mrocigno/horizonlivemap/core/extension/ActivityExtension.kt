package br.com.mrocigno.horizonlivemap.core.extension

import android.app.Activity
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import br.com.mrocigno.horizonlivemap.core.R

fun Activity.transparentToolbar(@ColorRes toolbarColor: Int = R.color.black_translucent) {
    window.run {
        decorView.fitsSystemWindows = true
        statusBarColor = getColor(toolbarColor)
    }
}