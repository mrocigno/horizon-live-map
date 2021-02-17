package br.com.mrocigno.horizonlivemap.map.ui.map.pages

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import br.com.arch.toolkit.recycler.adapter.SimpleAdapter
import br.com.arch.toolkit.recycler.adapter.ViewBinder
import br.com.mrocigno.horizonlivemap.core.extensions.resetRotation
import br.com.mrocigno.horizonlivemap.core.functions.logD
import br.com.mrocigno.horizonlivemap.core.providers.navProvider
import br.com.mrocigno.horizonlivemap.map.R
import br.com.mrocigno.horizonlivemap.map.ui.map.marker.CampMarker
import br.com.mrocigno.horizonlivemap.map.ui.view.HorizonMapView
import br.com.mrocigno.horizonlivemap.map.ui.view.OnRotateListener
import com.peterlaurence.mapview.MapViewConfiguration
import com.peterlaurence.mapview.api.*
import com.peterlaurence.mapview.core.TileStreamProvider
import com.peterlaurence.mapview.markers.MarkerTapListener
import com.peterlaurence.mapview.util.modulo

class MapFragment : Fragment(R.layout.fragment_map), OnRotateListener {

    private val mapView: HorizonMapView by lazy { requireView().findViewById(R.id.map_view) }
    private val rotateIndicator: View by viewProvider(R.id.rotate_indicator)
    private val rotateBase: View by viewProvider(R.id.rotate_base)
    private val lock: View by viewProvider(R.id.lock)
    private val navController by navProvider()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configMapView()
        rotateBase.setOnClickListener {
            resetMapAngle()
        }
        rotateBase.setOnLongClickListener {
            lock.isVisible = !lock.isVisible
            mapView.enableRotation(!lock.isVisible)
            resetMapAngle()
            true
        }
    }

    private fun resetMapAngle() {
        rotateIndicator.resetRotation {
            setUpdateListener {
                mapView.setAngle(rotateIndicator.rotation)
            }
        }
    }

    private fun configMapView() {
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
        mapView.defineBounds(0.0, 0.0, 255.0, -255.0)
        val test = CampMarker(requireContext())
        mapView.rotateListener = this
        mapView.addMarker(
            view = test,
            x = 157.875,
            y = -213.5,
        )
        mapView.moveToMarker(test, 3f, true)
        mapView.setMarkerTapListener(object : MarkerTapListener {
            override fun onMarkerTap(view: View, x: Int, y: Int) {

            }
        })
    }

    override fun onDestroy() {
        mapView.destroy()
        super.onDestroy()
    }

    override fun onRotate(radians: Float) {
        val rotation = rotateIndicator.rotation + radians
        rotateIndicator.rotation = rotation % 360
        logD("${rotation}")
    }
}

class Test @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ViewBinder<String> {

    private val text: TextView by viewProvider(R.id.title)

    init {
        inflate(context, R.layout.cell_selector, this)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    override fun bind(model: String) {
        text.text = model
    }

}