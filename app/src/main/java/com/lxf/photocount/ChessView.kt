package com.lxf.photocount

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ChessView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    val boardSize = 19
    val chessArray = Array(boardSize, init = {
        IntArray(boardSize, init = {
            3
        })
    })
    val pieceArray = Array(boardSize, init = {
        IntArray(boardSize, init = {
            0
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

    fun markPiece(piece: String) {
        for (i in 0 until boardSize) {
            val s1 = piece.substring(i * boardSize, i * boardSize + boardSize)
            for (j in s1.indices) {
                val c = s1[j]
                val c1 = (c.toString()).toInt()
                pieceArray[j][boardSize - 1 - i] = c1
            }
        }

        invalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(Color.parseColor("#DAA569"))

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
                when (chessArray[i][j]) {
                    1 -> {
                        paint.color = Color.BLACK
                        canvas?.drawCircle(
                            i * size + size / 2,
                            j * size + size / 2,
                            size / 2,
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

                when (pieceArray[i][j]) {
                    3, 5 -> {
                        paint.color = Color.BLACK
                        canvas?.drawRect(
                            i * size + 15,
                            j * size + 15,
                            (i + 1) * size - 15,
                            (j + 1) * size - 15,
                            paint
                        )
                    }
                    4, 6 -> {
                        paint.color = Color.WHITE
                        canvas?.drawRect(
                            i * size + 15,
                            j * size + 15,
                            (i + 1) * size - 15,
                            (j + 1) * size - 15,
                            paint
                        )
                    }
                }
            }
        }
    }
}