package br.com.mrocigno.horizonlivemap.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import br.com.arch.toolkit.delegate.viewProvider
import br.com.mrocigno.horizonlivemap.R
import br.com.mrocigno.horizonlivemap.map.ui.map.MapActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    private val motionLayout: MotionLayout by viewProvider(R.id.motion_layout)
    private val btnError: Button by viewProvider(R.id.error_button)

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) = Unit
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) = Unit
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) = Unit
            override fun onTransitionCompleted(p0: MotionLayout?, state: Int) {
                if (state == R.id.end) {
                    val intent = Intent(this@SplashActivity, MapActivity::class.java)
                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity).toBundle()
                    startActivity(intent, options)
                    finish()
                }
            }
        })

        viewModel.sync.collect {
            data { motionLayout.transitionToState(R.id.mid) }
            error {
                it.printStackTrace()
                motionLayout.transitionToState(R.id.error)
            }
        }

        viewModel.sync()
        btnError.setOnClickListener {
            viewModel.sync()
            motionLayout.transitionToState(R.id.start)
        }
    }
}