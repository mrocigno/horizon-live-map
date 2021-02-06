package br.com.mrocigno.horizonlivemap.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.mrocigno.horizonlivemap.R
import br.com.mrocigno.horizonlivemap.map.ui.map.MapActivity

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MapActivity::class.java))
        finish()
    }
}