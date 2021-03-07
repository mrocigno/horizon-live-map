package br.com.mrocigno.horizonlivemap.core.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import br.com.mrocigno.horizonlivemap.core.R
import br.com.mrocigno.horizonlivemap.core.functions.logD

class RoundedFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mWidth = 0f
    private var mHeight = 0f

    var radius = 0f
        set(value) {
            field = value
            topRightRadius = value
            topLeftRadius = value
            bottomRightRadius = value
            bottomLeftRadius = value
            invalidate()
        }

    var topRadius = 0f
        set(value) {
            field = value
            topRightRadius = value
            topLeftRadius = value
            invalidate()
        }

    var bottomRadius = 0f
        set(value) {
            field = value
            bottomRightRadius = value
            bottomLeftRadius = value
            invalidate()
        }

    private var topLeftRadius = 0f
    private var topRightRadius = 0f

    private var bottomLeftRadius = 0f
    private var bottomRightRadius = 0f

    private val cleaner = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        alpha = 0
    }

    private var topLeftPath = createTopLeftPath()
    private var topRightPath = createTopRightPath()

    private var bottomLeftPath = createBottomLeftPath()
    private var bottomRightPath = createBottomRightPath()

    private fun createTopLeftPath() = Path().apply {
        this.moveTo(0f, 0f)
        this.lineTo(topLeftRadius, 0f)
        this.cubicTo(topLeftRadius, 0f, 0f, 0f, 0f, topLeftRadius)
        this.close()
    }

    private fun createTopRightPath() = Path().apply {
        val xDistance = mWidth - topRightRadius
        this.moveTo(mWidth, 0f)
        this.lineTo(xDistance, 0f)
        this.cubicTo(xDistance, 0f, mWidth, 0f, mWidth, topRightRadius)
        this.close()
    }

    private fun createBottomLeftPath() = Path().apply {
        val yDistance = mHeight - bottomLeftRadius
        this.moveTo(0f, mHeight)
        this.lineTo(bottomLeftRadius, mHeight)
        this.cubicTo(bottomLeftRadius, mHeight, 0f, mHeight, 0f, yDistance)
        this.close()
    }

    private fun createBottomRightPath() = Path().apply {
        val xDistance = mWidth - bottomRightRadius
        val yDistance = mHeight - bottomRightRadius
        this.moveTo(mWidth, mHeight)
        this.lineTo(xDistance, mHeight)
        this.cubicTo(xDistance, mHeight, mWidth, mHeight, mWidth, yDistance)
        this.close()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        mHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        topLeftPath = createTopLeftPath()
        topRightPath = createTopRightPath()
        bottomLeftPath = createBottomLeftPath()
        bottomRightPath = createBottomRightPath()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    init {
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RoundedFrameLayout,
            0,
            0
        ).apply {
            try {
                topLeftRadius = getDimension(R.styleable.RoundedFrameLayout_rounded_top_left_radius, 0f)
                topRightRadius = getDimension(R.styleable.RoundedFrameLayout_rounded_top_right_radius, 0f)
                bottomLeftRadius = getDimension(R.styleable.RoundedFrameLayout_rounded_bottom_left_radius, 0f)
                bottomRightRadius = getDimension(R.styleable.RoundedFrameLayout_rounded_bottom_right_radius, 0f)
                if (topLeftRadius == 0f && topRightRadius == 0f) {
                    topRadius = getDimension(R.styleable.RoundedFrameLayout_rounded_top_radius, 0f)
                }
                if (bottomLeftRadius == 0f && bottomRightRadius == 0f) {
                    bottomRadius = getDimension(R.styleable.RoundedFrameLayout_rounded_bottom_radius, 0f)
                }
                if (topLeftRadius == 0f
                        && topRightRadius == 0f
                        && bottomLeftRadius == 0f
                        && bottomRightRadius == 0f) {
                    radius = getDimension(R.styleable.RoundedFrameLayout_rounded_radius, 0f)
                }
            } finally {
                recycle()
            }
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.drawPath(topLeftPath, cleaner)
        canvas?.drawPath(topRightPath, cleaner)
        canvas?.drawPath(bottomLeftPath, cleaner)
        canvas?.drawPath(bottomRightPath, cleaner)
    }
}