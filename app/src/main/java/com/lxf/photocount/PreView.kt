package com.lxf.photocount

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver

class PreView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val distance = 50f
    private val paint = Paint()
    private var rectPath = Path()


    init {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                rectPath.moveTo(distance, (height - (width - distance * 2)) / 2)
                rectPath.lineTo(width - distance, (height - (width - distance * 2)) / 2)
                rectPath.lineTo(
                    width - distance,
                    (height - (width - distance * 2)) / 2 + (width - distance * 2)
                )
                rectPath.lineTo(
                    distance,
                    (height - (width - distance * 2)) / 2 + (width - distance * 2)
                )
                rectPath.close()
                rectPath.fillType = Path.FillType.INVERSE_EVEN_ODD

                viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.apply {
            this.color = Color.GRAY
            this.alpha = 150
            this.style = Paint.Style.FILL
        }
        canvas?.drawPath(rectPath, paint)

        paint.apply {
            this.color = Color.GREEN
            this.alpha = 255
            this.style = Paint.Style.STROKE
        }
        canvas?.drawPath(rectPath, paint)
    }

    fun getLeftDistance() = distance
    fun getTopDistance() = (height - (width - distance * 2)) / 2
    fun getSize() = width - distance * 2
}