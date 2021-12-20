package com.example.simplepaintapp

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.core.view.doOnLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.example.simplepaintapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }

    private var eraserSpringAnimationX: SpringAnimation? = null
    private var eraserSpringAnimationY: SpringAnimation? = null

    private fun setListeners() {
        with(binding) {
            blackButton.setOnClickListener { paintView.setColor(Color.BLACK) }
            redButton.setOnClickListener { paintView.setColor(Color.RED) }
            blueButton.setOnClickListener { paintView.setColor(Color.BLUE) }
            greenButton.setOnClickListener { paintView.setColor(Color.GREEN) }
            yellowButton.setOnClickListener { paintView.setColor(Color.YELLOW) }

            eraserButton.setOnClickListener {
                paintView.setColor(Color.WHITE, isEraser = true)
                paintView.detectDragOnEraser { event -> detectDragOnEraser(event) }
            }
            eraserButton.doOnLayout {
                setUpEraserSpringAnim()
            }
        }
    }

    private fun setUpEraserSpringAnim() {
        with(binding) {
            eraserSpringAnimationX = SpringAnimation(eraserButton, DynamicAnimation.X).apply {
                spring = SpringForce(eraserButton.x).apply {
                    dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                    stiffness = SpringForce.STIFFNESS_MEDIUM
                }
            }

            eraserSpringAnimationY = SpringAnimation(eraserButton, DynamicAnimation.Y).apply {
                spring = SpringForce(eraserButton.y).apply {
                    dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                    stiffness = SpringForce.STIFFNESS_MEDIUM
                }
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun detectDragOnEraser(event: MotionEvent) {
        with(binding) {
            val startPointX = eraserButton.x
            val startPointY = eraserButton.y

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        eraserSpringAnimationX?.cancel()
                        eraserSpringAnimationY?.cancel()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        eraserButton.x += event.x - startPointX
                        eraserButton.y += event.y - startPointY
                    }
                    MotionEvent.ACTION_UP -> {
                        eraserSpringAnimationX?.start()
                        eraserSpringAnimationY?.start()
                    }
                    else -> {}
                }

        }

    }


}