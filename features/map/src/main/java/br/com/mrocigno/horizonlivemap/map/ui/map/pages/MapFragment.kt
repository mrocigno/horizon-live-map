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
import androidx.core.content.ContextCompat
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
import br.com.mrocigno.horizonlivemap.core.helpers.picasso
import br.com.mrocigno.horizonlivemap.map.R
import br.com.mrocigno.horizonlivemap.map.ui.map.marker.CampMarker
import br.com.mrocigno.horizonlivemap.map.ui.view.HorizonMapView
import br.com.mrocigno.horizonlivemap.map.ui.view.OnRotateListener
import br.com.mrocigno.sdk.api.MapDataResponse
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.peterlaurence.mapview.MapViewConfiguration
import com.peterlaurence.mapview.api.addMarker
import com.peterlaurence.mapview.api.enableRotation
import com.peterlaurence.mapview.api.setAngle
import com.peterlaurence.mapview.api.setMarkerTapListener
import com.peterlaurence.mapview.core.TileStreamProvider
import com.peterlaurence.mapview.markers.MarkerTapListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

private const val HALF_EXPANDED_RATIO = 0.7f

class MapFragment : Fragment(R.layout.fragment_map), OnRotateListener {

    private val mapView: HorizonMapView by lazy { requireView().findViewById(R.id.map_view) }
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

        mapViewModel.teste.collect(lifecycleScope) {
            empty {
                val i = 10
            }
            loading {
                val i = 10
            }
            data {
                addMarkers(it)
            }
            error {
                it.printStackTrace()
                val i = 10
            }
        }
        mapViewModel.testee()
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
        config.setMaxScale(10f)
        config.setWorkerCount(60)
        config.enableRotation()
        mapView.configure(config)
        mapView.defineBounds(0.0, 0.0, 500.0, -500.0)
        mapView.rotateListener = this
        mapView.setMarkerTapListener(MarkerOnClickListener { view, x, y ->
            showBottomSheet(true)
            imageAdapter.setList(listOf("1", "4", "3"))
        })
    }

    private fun addMarkers(list: List<MapDataResponse>) {
        list.forEach {
            val test = CampMarker(
                context = requireContext(),
                markerData = it.marker
            )
            mapView.addMarker(
                view = test,
                x = it.marker.position[1],
                y = it.marker.position[0]
            )
        }
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

    override fun bind(model: String) = when(model) {
        "1" -> setImageDrawable(ContextCompat.getDrawable(context, R.drawable.trash_1))
        "2" -> setImageDrawable(ContextCompat.getDrawable(context, R.drawable.trash_2))
        "3" -> setImageDrawable(ContextCompat.getDrawable(context, R.drawable.trash_3))
        else -> setImageDrawable(ContextCompat.getDrawable(context, R.drawable.trash_4))
    }
}

fun interface MarkerOnClickListener : MarkerTapListener