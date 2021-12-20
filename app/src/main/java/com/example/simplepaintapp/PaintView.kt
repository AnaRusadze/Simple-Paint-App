package com.example.simplepaintapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PaintView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var path = Path()
    private var paint = Paint()

    private var pathList = mutableListOf<Path>()
    private var colorList = mutableListOf<Int>()
    private var currentBrush = Color.BLACK
    private var isEraser = false

    init {
        paint.apply {
            isAntiAlias = true
            color = currentBrush
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeWidth = 20f
        }

    }

    private var detectDragOnEraser: ((event: MotionEvent) -> Unit)? = null

    fun detectDragOnEraser(detectDragOnEraser: ((event: MotionEvent) -> Unit)?) {
        this.detectDragOnEraser = detectDragOnEraser
    }

    fun setColor(color: Int, isEraser: Boolean = false) {
        currentBrush = color
        this.isEraser = isEraser
        path = Path()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        if (isEraser) detectDragOnEraser?.invoke(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                pathList.add(path)
                colorList.add(currentBrush)
            }
            else -> return false
        }
        postInvalidate()
        return false
    }

    override fun onDraw(canvas: Canvas?) {
        for (i in pathList.indices) {
            paint.color = colorList[i]
            canvas?.drawPath(pathList[i], paint)
            invalidate()
        }
    }

}