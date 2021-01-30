package br.com.mrocigno.horizonlivemap

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent("${BuildConfig.APPLICATION_ID}.MAP_VIEW")
        startActivity(intent)
    }
}