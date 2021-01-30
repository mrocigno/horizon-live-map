package br.com.mrocigno.horizonlivemap.core.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.*
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import br.com.mrocigno.horizonlivemap.core.R

abstract class BaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.run {
            decorView.fitsSystemWindows = true
            statusBarColor = getColor(R.color.black_translucent)
        }
        super.onCreate(savedInstanceState)
    }
}