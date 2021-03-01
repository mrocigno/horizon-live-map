package br.com.mrocigno.horizonlivemap.map.ui.map.pages

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.transition.Slide
import androidx.transition.TransitionManager
import br.com.arch.toolkit.delegate.viewProvider
import br.com.arch.toolkit.recycler.adapter.ViewBinder
import br.com.mrocigno.horizonlivemap.core.extensions.gone
import br.com.mrocigno.horizonlivemap.core.extensions.resetRotation
import br.com.mrocigno.horizonlivemap.core.extensions.transparentStatusBar
import br.com.mrocigno.horizonlivemap.core.extensions.visible
import br.com.mrocigno.horizonlivemap.core.functions.logD
import br.com.mrocigno.horizonlivemap.map.R
import br.com.mrocigno.horizonlivemap.map.ui.map.marker.CampMarker
import br.com.mrocigno.horizonlivemap.map.ui.view.HorizonMapView
import br.com.mrocigno.horizonlivemap.map.ui.view.OnRotateListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.peterlaurence.mapview.MapViewConfiguration
import com.peterlaurence.mapview.api.*
import com.peterlaurence.mapview.core.TileStreamProvider
import com.peterlaurence.mapview.layout.GestureLayout
import com.peterlaurence.mapview.markers.MarkerTapListener

class MapFragment : Fragment(R.layout.fragment_map), OnRotateListener {

    private val mapView: HorizonMapView by lazy { requireView().findViewById(R.id.map_view) }
    private val rotateIndicator: View by viewProvider(R.id.rotate_indicator)
    private val rotateBase: View by viewProvider(R.id.rotate_base)
    private val lock: View by viewProvider(R.id.lock)
    private val bottomSheet: ViewGroup by viewProvider(R.id.bottom_sheet)
    private val root: CoordinatorLayout by viewProvider(R.id.root_coordinator)
    private val appBar: AppBarLayout by viewProvider(R.id.app_bar)
    private lateinit var behavior: BottomSheetBehavior<ViewGroup>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configBottomSheet()
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

    private fun configBottomSheet() {
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.isFitToContents = false

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            var loopBreaker = false
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED,
                    BottomSheetBehavior.STATE_HIDDEN,
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        appBar.hide()
                        loopBreaker = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (behavior.state in 1..2 && slideOffset > .8f && !loopBreaker) {
                    appBar.show()
                    loopBreaker = true
                }
            }
        })
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
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
    }

    private fun AppBarLayout.show() {
        TransitionManager.beginDelayedTransition(root, Slide(Gravity.TOP))
        this.visible()
    }

    private fun AppBarLayout.hide() {
        TransitionManager.beginDelayedTransition(root, Slide(Gravity.TOP))
        this.gone()
    }
}