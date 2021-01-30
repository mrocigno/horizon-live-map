package br.com.mrocigno.horizonlivemap.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.mrocigno.horizonlivemap.core.base.BaseActivity
import br.com.mrocigno.horizonlivemap.ui.R
import br.com.mrocigno.horizonlivemap.ui.map.marker.CampMarker
import com.peterlaurence.mapview.MapView
import com.peterlaurence.mapview.MapViewConfiguration
import com.peterlaurence.mapview.api.addMarker
import com.peterlaurence.mapview.core.TileStreamProvider

class MapActivity : BaseActivity(R.layout.activity_map) {

    private val mapView : MapView by lazy { findViewById(R.id.mapView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tilesProvider = TileStreamProvider { row, col, zoomLvl ->
            assets.open("tiles/$zoomLvl/$row/${col}.jpg")
        }
        val config = MapViewConfiguration(
            levelCount = 4,
            fullWidth = 4096,
            fullHeight = 4096,
            tileSize = 256,
            tileStreamProvider = tilesProvider
        )
        config.setMaxScale(10f)
        config.enableRotation()
        mapView.configure(config)
        mapView.defineBounds(0.0, 0.0, 1.0, 1.0)
        mapView.addMarker(
            view = CampMarker(this),
            x = 0.5859375,
            y = 0.638977635782748,
        )
    }
}