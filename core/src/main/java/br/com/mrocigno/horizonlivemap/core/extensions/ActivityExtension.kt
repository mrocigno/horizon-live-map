package br.com.mrocigno.horizonlivemap.core.extensions

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.ColorRes
import br.com.mrocigno.horizonlivemap.core.R

fun Activity.transparentStatusBar(@ColorRes toolbarColor: Int = R.color.black_translucent) {
    window.run {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        setDecorFitsSystemWindows(this, false)
        statusBarColor = getColor(toolbarColor)
    }
}
fun Activity.lightStatusBar(view: View) {
    window.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insetsController?.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
        } else {
            var flags: Int = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
        }
    }
}

val Activity.statusBarHeight: Int get() {
    val rectangle = Rect()
    window.decorView.getWindowVisibleDisplayFrame(rectangle)
    return rectangle.top
}

val Activity.actionBarHeight: Int get() {
    val tv = TypedValue()
    return if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    } else 0
}

fun setDecorFitsSystemWindows(window: Window, decorFitsSystemWindows: Boolean) {
//    if (Build.VERSION.SDK_INT < 30) {
        window.run {
            val decorFitsFlags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            val sysUiVis = decorView.systemUiVisibility
            decorView.systemUiVisibility = if (decorFitsSystemWindows) sysUiVis and decorFitsFlags.inv() else sysUiVis or decorFitsFlags
        }
//    } else {
//        TODO: Find a method to use `setDecorFitsSystemWindows` and keep the navigationBar
//        window.setDecorFitsSystemWindows(decorFitsSystemWindows)
//    }
}