package br.com.mrocigno.horizonlivemap.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import br.com.mrocigno.horizonlivemap.core.providers.navProvider
import br.com.mrocigno.horizonlivemap.map.marker.CampMarker
import com.peterlaurence.mapview.MapView
import com.peterlaurence.mapview.MapViewConfiguration
import com.peterlaurence.mapview.api.addMarker
import com.peterlaurence.mapview.api.setMarkerTapListener
import com.peterlaurence.mapview.core.TileStreamProvider
import com.peterlaurence.mapview.markers.MarkerTapListener

class MapFragment : Fragment(R.layout.fragment_map) {

    private val mapView : MapView by lazy { requireActivity().findViewById(R.id.mapView) }
    private val navController by navProvider()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tilesProvider = TileStreamProvider { row, col, zoomLvl ->
            requireActivity().assets.open("tiles/$zoomLvl/$row/${col}.jpg")
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
            view = CampMarker(requireContext()),
            x = 0.5859375,
            y = 0.638977635782748,
        )
        mapView.setMarkerTapListener(object : MarkerTapListener {
            override fun onMarkerTap(view: View, x: Int, y: Int) {
                navController.popBackStack()
            }
        })
    }
}