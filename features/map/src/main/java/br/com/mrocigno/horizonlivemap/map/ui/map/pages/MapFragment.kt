package br.com.mrocigno.horizonlivemap.map.ui.map.pages

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
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

class MapFragment : Fragment(R.layout.fragment_map), OnRotateListener {

    private val mapView: HorizonMapView by lazy { requireView().findViewById(R.id.map_view) }
    private val rotateIndicator: View by viewProvider(R.id.rotate_indicator)
    private val rotateBase: View by viewProvider(R.id.rotate_base)
    private val lock: View by viewProvider(R.id.lock)
    private val bottomSheet: ViewGroup by viewProvider(R.id.bottom_sheet)
    private val imageFrame: ViewGroup by viewProvider(R.id.image_frame)
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
        behavior.halfExpandedRatio = HALF_EXPANDED_RATIO

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> showBottomSheet(false)
                    BottomSheetBehavior.STATE_COLLAPSED -> Unit// TODO()
                    BottomSheetBehavior.STATE_DRAGGING -> Unit
                    BottomSheetBehavior.STATE_EXPANDED -> Unit
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> Unit// TODO()
                    BottomSheetBehavior.STATE_SETTLING -> Unit// TODO()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                peekTransforms(bottomSheet)
                halfExpandedTransforms(bottomSheet)
                expandedTransforms(bottomSheet)
            }
        })
    }

    private fun peekTransforms(bottomSheet: View) {
        val mHeight = bottomSheet.height
        val frameFixedSize = resources.getDimension(R.dimen.image_container_peek_size)
        val offset = mHeight - bottomSheet.top
        val complete = resources.getDimensionPixelOffset(R.dimen.peek_bottom_sheet)
        val ratio = (offset.toFloat() / complete.toFloat()).coerceIn(0f, 1f)

        imageFrame.translationY = frameFixedSize * (1 - ratio)
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
        if (visible) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.header_container, BottomSheetHeaderFragment())
                .replace(R.id.content_container, BottomSheetContentFragment())
                .commit()
        } else {
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onDestroy() {
        mapView.destroy()
        super.onDestroy()
    }

    override fun onRotate(radians: Float) {
        val rotation = rotateIndicator.rotation + radians
        rotateIndicator.rotation = rotation % 360
    }
}
