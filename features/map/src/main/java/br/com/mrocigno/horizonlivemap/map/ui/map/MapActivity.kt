package br.com.mrocigno.horizonlivemap.map.ui.map

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import br.com.arch.toolkit.recycler.adapter.SimpleAdapter
import br.com.mrocigno.horizonlivemap.core.extensions.transparentStatusBar
import br.com.mrocigno.horizonlivemap.map.R
import br.com.mrocigno.horizonlivemap.core.R as CR

class MapActivity : FragmentActivity(R.layout.activity_map) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar(CR.color.black_menu_gradient)
    }
}