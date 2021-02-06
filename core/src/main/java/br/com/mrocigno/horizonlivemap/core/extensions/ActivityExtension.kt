package br.com.mrocigno.horizonlivemap.core.extensions

import android.app.Activity
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.view.WindowCompat
import br.com.mrocigno.horizonlivemap.core.R

fun Activity.transparentToolbar(@ColorRes toolbarColor: Int = R.color.black_translucent) {
    window.run {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        WindowCompat.setDecorFitsSystemWindows(this, false)
        statusBarColor = getColor(toolbarColor)
    }
}