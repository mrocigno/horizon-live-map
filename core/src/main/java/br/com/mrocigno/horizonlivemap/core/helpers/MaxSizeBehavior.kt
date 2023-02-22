package br.com.mrocigno.horizonlivemap.core.helpers

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.withStyledAttributes
import br.com.mrocigno.horizonlivemap.core.R
import kotlin.math.min

class MaxSizeBehavior(context: Context, attrs: AttributeSet): CoordinatorLayout.Behavior<View>(context, attrs) {

    private var maxWidth: Int = NO_MAX_SIZE
    private var maxHeight: Int = NO_MAX_SIZE

    init {
        context.withStyledAttributes(attrs, R.styleable.MaxSizeBehavior) {
            maxWidth = getDimensionPixelSize(R.styleable.MaxSizeBehavior_android_maxWidth, NO_MAX_SIZE)
            maxHeight = getDimensionPixelSize(R.styleable.MaxSizeBehavior_android_maxHeight, NO_MAX_SIZE)
        }
    }

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        val lp = child.layoutParams as ViewGroup.MarginLayoutParams
        val childWidthMeasureSpec = getChildMeasureSpec(
            parentWidthMeasureSpec,
            parent.paddingLeft
                + parent.paddingRight
                + lp.leftMargin
                + lp.rightMargin
                + widthUsed,
            maxWidth,
            lp.width
        )
        val childHeightMeasureSpec: Int = getChildMeasureSpec(
            parentHeightMeasureSpec,
            (parent.paddingTop
                + parent.paddingBottom
                + lp.topMargin
                + lp.bottomMargin
                + heightUsed),
            maxHeight,
            lp.height
        )
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        return true // Child was measured
    }

    private fun getChildMeasureSpec(
        parentMeasureSpec: Int,
        padding: Int,
        maxSize: Int,
        childDimension: Int
    ): Int {
        val result = ViewGroup.getChildMeasureSpec(parentMeasureSpec, padding, childDimension)
        return if (maxSize == NO_MAX_SIZE) {
            result
        } else {
            val mode = MeasureSpec.getMode(result)
            val size = MeasureSpec.getSize(result)
            when (mode) {
                MeasureSpec.EXACTLY -> MeasureSpec.makeMeasureSpec(
                    min(size, maxSize),
                    MeasureSpec.EXACTLY
                )

                MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> MeasureSpec.makeMeasureSpec(
                    if (size == 0) maxSize else min(size, maxSize), MeasureSpec.AT_MOST
                )

                else -> MeasureSpec.makeMeasureSpec(
                    if (size == 0) maxSize else min(size, maxSize), MeasureSpec.AT_MOST
                )
            }
        }
    }

    companion object {

        private const val NO_MAX_SIZE = -1
    }
}