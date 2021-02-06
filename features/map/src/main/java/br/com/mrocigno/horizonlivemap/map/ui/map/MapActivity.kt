package br.com.mrocigno.horizonlivemap.map.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import br.com.arch.toolkit.recycler.adapter.SimpleAdapter
import br.com.mrocigno.horizonlivemap.core.extensions.transparentToolbar
import br.com.mrocigno.horizonlivemap.map.R
import br.com.mrocigno.horizonlivemap.map.ui.map.pages.Test

class MapActivity : AppCompatActivity(R.layout.activity_map) {

    private val adapter = SimpleAdapter { Test(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentToolbar(R.color.black_menu_gradient)
    }
}