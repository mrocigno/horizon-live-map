package br.com.mrocigno.horizonlivemap.map.ui.map.pages

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.Guideline
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
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

private const val HALF_EXPANDED_RATIO = 0.7f

class MapFragment : Fragment(R.layout.fragment_map_2), OnRotateListener {

    private val mapView: HorizonMapView by lazy { requireView().findViewById(R.id.map_view) }
    private val rotateIndicator: View by viewProvider(R.id.rotate_indicator)
    private val rotateBase: View by viewProvider(R.id.rotate_base)
    private val lock: View by viewProvider(R.id.lock)
    private val bottomSheet: ViewGroup by viewProvider(R.id.bottom_sheet)
    private val imageFrame: ViewGroup by viewProvider(R.id.image_frame)
    private val root: MotionLayout by viewProvider(R.id.root)
    private val guide: Guideline by viewProvider(R.id.guide)
    private val appBar: AppBarLayout by viewProvider(R.id.app_bar)

    private val headerContainer: FragmentContainerView by viewProvider(R.id.header_container)
    private val contentContainer: FragmentContainerView by viewProvider(R.id.content_container)

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

    private fun halfExpandedTransforms(bottomSheet: View) {
        val mHeight = bottomSheet.height
        val frameFixedSize = resources.getDimension(R.dimen.image_container_peek_size)
        val frameSize = mHeight - mHeight * HALF_EXPANDED_RATIO
        val peekCompensation = (mHeight - resources.getDimension(R.dimen.peek_bottom_sheet))
        val complete = peekCompensation - frameSize
        val offset = peekCompensation - bottomSheet.top
        val ratio = (offset / complete).coerceAtLeast(0f)
        imageFrame.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            val fixedSize = frameFixedSize * (1 - ratio)
            val variableSize = frameSize * ratio
            height = (fixedSize + variableSize).roundToInt()
        }
        headerContainer.alpha = 1 - ratio
        contentContainer.alpha = ratio
        when {
            ratio > .9f -> {
                with(requireActivity()) {
                    transparentStatusBar(android.R.color.transparent)
                    if (!isNightMode) lightStatusBar(appBar)
                }
            }
            ratio in 0.01..0.9 -> {
                contentContainer.visible()
                requireActivity().transparentStatusBar(R.color.black_menu_gradient)
            }
            ratio < .01 -> {
                contentContainer.gone()
            }
        }
    }

    private fun expandedTransforms(bottomSheet: View) {
        val mHeight = bottomSheet.height
        val frameSize = (mHeight - mHeight * HALF_EXPANDED_RATIO).roundToInt()
        val offset = (frameSize - bottomSheet.top)
        val ratio = (offset.toFloat() / frameSize.toFloat()).coerceAtLeast(0f)
        val padding = requireActivity().let { it.statusBarHeight + it.actionBarHeight }
        contentContainer.setPaddingTop((padding * ratio).roundToInt())

        appBar.translationY = padding.unaryMinus() * (1 - ratio)
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
                showBottomSheet(true)
            }
        })
    }

    private fun showBottomSheet(visible: Boolean) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.header_container, BottomSheetHeaderFragment())
            .replace(R.id.content_container, BottomSheetContentFragment())
            .commit()
        root.transitionToState(R.id.peek)
    }

    override fun onDestroy() {
        mapView.destroy()
        super.onDestroy()
    }

    override fun onRotate(radians: Float) {
        val rotation = rotateIndicator.rotation + radians
        rotateIndicator.rotation = rotation % 360
    }

//    private fun AppBarLayout.show() {
//        if (!isVisible) {
//            TransitionManager.beginDelayedTransition(root, Slide(Gravity.TOP).apply {
//                addTarget(R.id.app_bar)
//            })
//            with(requireActivity()) {
//                transparentStatusBar(android.R.color.transparent)
//                if (!isNightMode) lightStatusBar(this@show)
//            }
//            this.visible()
//        }
//    }
//
//    var teste = true
//    private fun AppBarLayout.hide() {
//        if (teste) {
//            teste = false
//            with(requireActivity()){
//                transparentStatusBar(R.color.black_menu_gradient)
//                animate()
//                    .translationYBy(0f)
//                    .translationY(height.toFloat().unaryMinus())
//                    .withEndAction { gone(); translationY = 0f; teste = true }
//                    .start()
//            }
//        }
//    }
}
