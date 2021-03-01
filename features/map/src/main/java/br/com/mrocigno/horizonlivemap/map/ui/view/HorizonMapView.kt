package br.com.mrocigno.horizonlivemap.map.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.peterlaurence.mapview.MapView
import com.peterlaurence.mapview.api.moveToMarker
import com.peterlaurence.mapview.util.AngleDegree
import com.peterlaurence.mapview.util.modulo

class HorizonMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MapView(context, attrs, defStyleAttr) {

    var rotateListener: OnRotateListener? = null

    override fun onRotate(rotationDelta: Float, focusX: Float, focusY: Float): Boolean {
        rotateListener?.let {
            val angle: AngleDegree = rotationDelta
            it.onRotate(angle.modulo())
        }

        return super.onRotate(rotationDelta, focusX, focusY)
    }
}

interface OnRotateListener {
    fun onRotate(radians: Float)
}