package br.com.mrocigno.horizonlivemap.map.ui.map.pages

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.transition.Slide
import androidx.transition.TransitionManager
import br.com.arch.toolkit.delegate.viewProvider
import br.com.mrocigno.horizonlivemap.core.extensions.*
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
import com.peterlaurence.mapview.markers.MarkerTapListener
import kotlin.math.roundToInt

class MapFragment : Fragment(R.layout.fragment_map), OnRotateListener {

    private val mapView: HorizonMapView by lazy { requireView().findViewById(R.id.map_view) }
    private val rotateIndicator: View by viewProvider(R.id.rotate_indicator)
    private val rotateBase: View by viewProvider(R.id.rotate_base)
    private val lock: View by viewProvider(R.id.lock)
    private val bottomSheet: ViewGroup by viewProvider(R.id.bottom_sheet)
    private val root: ViewGroup by viewProvider(R.id.root)
    private val appBar: AppBarLayout by viewProvider(R.id.app_bar)

    private val headerContainer: FragmentContainerView by viewProvider(R.id.header_container)
    private val contentContainer: FragmentContainerView by viewProvider(R.id.content_container)
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
                if (behavior.state in 1..2 && slideOffset > .5f && !loopBreaker) {
                    appBar.show()
                    loopBreaker = true
                }
                when (slideOffset) {
                    in -1f..0f -> {
                        headerContainer.alpha = 1f
                        contentContainer.alpha = 0f
                    }
                    in 0f..0.3f -> {
                        headerContainer.alpha = 1f - (slideOffset * 2)
                        contentContainer.alpha = slideOffset * 2
                    }
                    in 0.4f..1f -> {
                        headerContainer.alpha = 0f
                        val ratio = ((slideOffset - .5f) * 2).coerceIn(0f, 1f)
                        val padding = requireActivity().let {
                            it.statusBarHeight + it.actionBarHeight
                        }
                        with(contentContainer) {
                            alpha = 1f
                            setPadding(
                                paddingLeft,
                                (padding * ratio).roundToInt(),
                                paddingRight,
                                paddingBottom
                            )
                        }
                    }
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
                requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.header_container, BottomSheetHeaderFragment())
                        .replace(R.id.content_container, BottomSheetContentFragment())
                        .commit()
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
        requireActivity().transparentStatusBar(android.R.color.transparent)
        requireActivity().lightStatusBar(this)
        this.visible()
    }

    private fun AppBarLayout.hide() {
        TransitionManager.beginDelayedTransition(root, Slide(Gravity.TOP))
        requireActivity().transparentStatusBar(R.color.black_menu_gradient)
        this.gone()
    }
}