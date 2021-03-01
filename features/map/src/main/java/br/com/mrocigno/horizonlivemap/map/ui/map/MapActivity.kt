package br.com.mrocigno.horizonlivemap.map.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.arch.toolkit.recycler.adapter.SimpleAdapter
import br.com.mrocigno.horizonlivemap.core.extensions.transparentStatusBar
import br.com.mrocigno.horizonlivemap.map.R

class MapActivity : AppCompatActivity(R.layout.activity_map) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar(R.color.black_menu_gradient)
    }
}