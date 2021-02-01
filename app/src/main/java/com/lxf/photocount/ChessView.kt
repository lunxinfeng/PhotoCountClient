package com.lxf.photocount

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ChessView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val boardSize = 19
    private val chessArray = Array<IntArray>(boardSize, init = {
        IntArray(boardSize, init = {
            3
        })
    })

    private val paint = Paint()

    fun setChess(chess: String) {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                val index = i * boardSize + j
                chessArray[j][i] = chess[index].toString().toInt()
            }
        }

        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(Color.YELLOW)

        val size = width / boardSize.toFloat()

        paint.color = Color.BLACK
        for (i in 0 until boardSize) {
            canvas?.drawLine(
                size / 2,
                i * size + size / 2,
                (boardSize - 1) * size + size / 2,
                i * size + size / 2,
                paint
            )
            canvas?.drawLine(
                i * size + size / 2,
                size / 2,
                i * size + size / 2,
                (boardSize - 1) * size + size / 2,
                paint
            )
        }

        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                print(chessArray[i][j])
                when (chessArray[i][j]) {
                    1 -> {
                        paint.color = Color.BLACK
                        canvas?.drawCircle(
                            i * size + size / 2,
                            j * size + size / 2,
                            size/2,
                            paint
                        )
                    }
                    2 -> {
                        paint.color = Color.BLACK
                        paint.strokeWidth = 1.5f
                        paint.style = Paint.Style.STROKE
                        canvas?.drawCircle(
                            i * size + size / 2,
                            j * size + size / 2,
                            size / 2,
                            paint
                        )

                        paint.style = Paint.Style.FILL
                        paint.color = Color.WHITE
                        canvas?.drawCircle(
                            i * size + size / 2,
                            j * size + size / 2,
                            size / 2,
                            paint
                        )
                    }
                    3 -> {
                        paint.color = Color.RED
                        canvas?.drawRect(
                            i * size + 10,
                            j * size + 10,
                            (i + 1) * size - 10,
                            (j + 1) * size - 10,
                            paint
                        )
                    }
                }
            }
        }
    }
}