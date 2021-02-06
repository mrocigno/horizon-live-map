package br.com.mrocigno.horizonlivemap.map.ui.map.marker

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import br.com.mrocigno.horizonlivemap.map.R

class CampMarker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.marker_camp, this)
    }

}