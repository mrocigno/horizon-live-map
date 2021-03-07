package br.com.mrocigno.horizonlivemap.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import br.com.arch.toolkit.delegate.viewProvider
import br.com.mrocigno.horizonlivemap.R
import br.com.mrocigno.horizonlivemap.map.ui.map.MapActivity

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    private val motionLayout: MotionLayout by viewProvider(R.id.motion_layout)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) = Unit

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) = Unit

            override fun onTransitionCompleted(p0: MotionLayout?, state: Int) {
                if (state == R.id.end) {
                    startActivity(Intent(this@SplashActivity, MapActivity::class.java))
                    finish()
                }
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) = Unit
        })
        motionLayout.transitionToState(R.id.mid)
    }
}