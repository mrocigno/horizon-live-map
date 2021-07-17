package br.com.mrocigno.horizonlivemap.map.ui.map.marker

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import br.com.arch.toolkit.delegate.viewProvider
import br.com.mrocigno.horizonlivemap.core.extensions.gone
import br.com.mrocigno.horizonlivemap.core.functions.baseUrl
import br.com.mrocigno.horizonlivemap.core.helpers.load
import br.com.mrocigno.horizonlivemap.map.R
import br.com.mrocigno.sdk.api.MarkerResponse

class CampMarker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    markerData: MarkerResponse? = null
) : FrameLayout(context, attrs, defStyleAttr) {

    private val icon: ImageView by viewProvider(R.id.marker_icon)
    private val shimmer: ViewGroup by viewProvider(R.id.loading_frame)

    init {
        inflate(context, R.layout.marker_camp, this)
        markerData?.let {
            icon.load(baseUrl(it.image)) {
                shimmer.gone()
            }
        }
    }

}