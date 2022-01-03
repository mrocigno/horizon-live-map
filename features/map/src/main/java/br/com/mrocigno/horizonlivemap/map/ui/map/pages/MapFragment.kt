package br.com.mrocigno.horizonlivemap.map.ui.map.pages

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import br.com.arch.toolkit.recycler.adapter.SimpleAdapter
import br.com.arch.toolkit.recycler.adapter.ViewBinder
import br.com.mrocigno.horizonlivemap.core.extensions.*
import br.com.mrocigno.horizonlivemap.core.functions.baseUrl
import br.com.mrocigno.horizonlivemap.core.helpers.load
import br.com.mrocigno.horizonlivemap.core.helpers.picasso
import br.com.mrocigno.horizonlivemap.map.R
import br.com.mrocigno.horizonlivemap.map.ui.map.marker.CampMarker
import br.com.mrocigno.horizonlivemap.map.ui.map.model.ItemData
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import ovh.plrapps.mapview.MapView
import ovh.plrapps.mapview.MapViewConfiguration
import ovh.plrapps.mapview.api.*
import ovh.plrapps.mapview.core.TileStreamProvider
import ovh.plrapps.mapview.markers.MarkerTapListener
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

private const val HALF_EXPANDED_RATIO = 0.7f

class MapFragment : Fragment(R.layout.fragment_map) {

    private val mapView: MapView by lazy { requireView().findViewById(R.id.map_view) }
    private val rotateIndicator: View by viewProvider(R.id.rotate_indicator)
    private val rotateBase: View by viewProvider(R.id.rotate_base)
    private val lock: View by viewProvider(R.id.lock)
    private val bottomSheet: ViewGroup by viewProvider(R.id.bottom_sheet)
    private val imageRecycler: RecyclerView by viewProvider(R.id.image_recycler)
    private val root: ViewGroup by viewProvider(R.id.root)
    private val appBar: AppBarLayout by viewProvider(R.id.app_bar)

    private val imageAdapter = ImageRecyclerAdapter()

    private val headerContainer: FragmentContainerView by viewProvider(R.id.header_container)
    private val contentContainer: FragmentContainerView by viewProvider(R.id.content_container)
    private lateinit var behavior: BottomSheetBehavior<ViewGroup>

    private val mapViewModel: MapViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configView()
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

        mapViewModel.mapData.collect(lifecycleScope) {
            data(::addMarkers)
            error {
                it.printStackTrace()
            }
        }
        mapViewModel.getData()
    }

    private fun resetMapAngle() {
        rotateIndicator.resetRotation {
            setUpdateListener {
                mapView.setAngle(rotateIndicator.rotation)
            }
        }
    }

    private fun peekTransforms(bottomSheet: View) {
        val mHeight = bottomSheet.height
        val frameFixedSize = resources.getDimension(R.dimen.image_container_peek_size)
        val offset = mHeight - bottomSheet.top
        val complete = resources.getDimensionPixelOffset(R.dimen.peek_bottom_sheet)
        val ratio = (offset.toFloat() / complete.toFloat()).coerceIn(0f, 1f)

        imageRecycler.translationY = frameFixedSize * (1 - ratio)
    }

    private fun halfExpandedTransforms(bottomSheet: View) {
        val mHeight = bottomSheet.height
        val frameFixedSize = resources.getDimension(R.dimen.image_container_peek_size)
        val frameSize = mHeight - mHeight * HALF_EXPANDED_RATIO
        val peekCompensation = (mHeight - resources.getDimension(R.dimen.peek_bottom_sheet))
        val complete = peekCompensation - frameSize
        val offset = peekCompensation - bottomSheet.top
        val ratio = (offset / complete).coerceIn(0f, 1f)
        imageRecycler.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            val fixedSize = frameFixedSize * (1 - ratio)
            val variableSize = frameSize * ratio
            height = (fixedSize + variableSize).roundToInt()
        }
        when {
            ratio > .9f -> {
                with(requireActivity()) {
                    transparentStatusBar(android.R.color.transparent)
                    if (!isNightMode) lightStatusBar(appBar)
                }
            }
            ratio in 0.01..0.9 -> {
                with (requireActivity()) {
                    transparentStatusBar(R.color.black_menu_gradient)
                    lightStatusBar(appBar, false)
                }
            }
        }
    }

    private fun expandedTransforms(bottomSheet: View) {
        val mHeight = bottomSheet.height
        val frameSize = (mHeight - mHeight * HALF_EXPANDED_RATIO).roundToInt()
        val offset = (frameSize - bottomSheet.top)
        val ratio = (offset.toFloat() / frameSize.toFloat()).coerceIn(0f, 1f)
        val padding = requireActivity().let { it.statusBarHeight + it.actionBarHeight }
        val peek = resources.getDimension(R.dimen.peek_bottom_sheet)
        contentContainer.setPaddingTop((padding * ratio).roundToInt())
        headerContainer.updateLayoutParams<LinearLayout.LayoutParams> {
            height = (peek * (1 - ratio)).roundToInt()
        }

        appBar.translationY = padding.unaryMinus() * (1 - ratio)
    }

    private fun configView() {
        imageRecycler.adapter = imageAdapter
        imageRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            false
        )
    }

    private fun configBottomSheet() {
        imageRecycler.isEnabled = false
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.isFitToContents = false
        behavior.halfExpandedRatio = HALF_EXPANDED_RATIO

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) = Unit

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                imageRecycler.bottom = bottomSheet.top
                peekTransforms(bottomSheet)
                halfExpandedTransforms(bottomSheet)
                expandedTransforms(bottomSheet)
            }
        })
    }

    private fun configMapView() {
        val tilesProvider = TileStreamProvider { row, col, zoomLvl ->
            picasso(baseUrl("tiles/$zoomLvl/$row/${col}.webp")).getCached()?.let { bmp ->
                ByteArrayOutputStream().let { bos ->
                    bmp.compress(Bitmap.CompressFormat.WEBP, 100, bos)
                    ByteArrayInputStream(bos.toByteArray())
                }
            }
        }
        val config = MapViewConfiguration(
            levelCount = 5,
            fullWidth = 8000,
            fullHeight = 8000,
            tileSize = 250,
            tileStreamProvider = tilesProvider
        )
        config.setMaxScale(5f)
        config.setWorkerCount(60)
        config.enableRotation()
        mapView.configure(config)
        mapView.defineBounds(0.0, 0.0, 500.0, -500.0)
        mapView.setMarkerTapListener(MarkerOnClickListener { view, _, _ ->
            if (view is CampMarker) {
                showBottomSheet(view.markerData)
            }
        })
        mapView.addReferentialListener {
            onRotate(it.angle)
            onScale(it.scale)
        }
    }

    private fun onRotate(rotation: Float) {
        rotateIndicator.rotation = rotation
    }

    private fun onScale(scale: Float) {
        mapViewModel.mapData.value?.forEach {
            mapView.getMarkerByTag(it.id.toString())?.run {
                if (scale >= it.marker.category.zoomLvl) fadeIn() else fadeOut()
            }
        }
    }

    private fun addMarkers(list: List<ItemData>) {
        list.forEach {
            val test = CampMarker(
                context = requireContext(),
                markerData = it
            )
            mapView.addMarker(
                view = test,
                x = it.marker.position[1],
                y = it.marker.position[0],
                tag = it.id.toString()
            )
        }
    }

    private fun showBottomSheet(data: ItemData?) {
        if (data != null) {
            val hasImage = data.images.isNotEmpty()
            behavior.isFitToContents = !hasImage
            imageAdapter.setList(data.images)
            if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                imageRecycler.isVisible = hasImage
                imageRecycler.isEnabled = hasImage
            } else {
                imageRecycler.isVisibleFade = hasImage
            }

            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.header_container, BottomSheetHeaderFragment.newInstance(data))
                .replace(R.id.content_container, BottomSheetContentFragment.newInstance())
                .commit()
        } else {
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onDestroy() {
        mapView.destroy()
        super.onDestroy()
    }
}

class ImageRecyclerAdapter : SimpleAdapter<String, ImageItem>(::ImageItem)

class ImageItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), ViewBinder<String> {

    init {
        scaleType = ScaleType.CENTER_CROP
        layoutParams = ViewGroup.LayoutParams(
            1000,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun bind(model: String) {
        load(baseUrl("/places/$model"))
    }
}

fun interface MarkerOnClickListener : MarkerTapListener