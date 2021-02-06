package br.com.mrocigno.horizonlivemap.core.extensions

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.annotation.FloatRange
import androidx.core.animation.doOnEnd

fun View.resetRotation(duration: Long = 300, config: ViewPropertyAnimator.() -> Unit) {
    this.animate().apply {
        val rotationTo = if (this@resetRotation.rotation > 180f) 360f else 0f
        rotation(rotationTo)
        setDuration(duration)
        apply(config)
    }.start()
}

fun ObjectAnimator.doOn(
    @FloatRange(from = 0.0, to = 1.0) timing: Float = .5f,
    action: () -> Unit
) {
    val helper = OnTimingHelper(timing, action)
    val listener = ValueAnimator.AnimatorUpdateListener { animation ->
        helper.process(animation.animatedFraction)
    }
    addUpdateListener(listener)
    doOnEnd {
        removeUpdateListener(listener)
    }
}

private class OnTimingHelper(
    @FloatRange(from = 0.0, to = 1.0)val timing: Float,
    val action: () -> Unit
) {

    var executed = false

    fun process(fraction: Float) {
        if (fraction >= timing && !executed) {
            executed = true
            action.invoke()
        }
    }
}
