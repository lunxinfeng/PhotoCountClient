package com.lxf.photocount

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.ImageProxy

fun ImageProxy.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    buffer.rewind()
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun ImageProxy.toBitmap608(
    screenWidth: Int,
    screenHeight: Int,
    rectX: Int,
    rectY: Int,
    rectSize: Int,
    rotate: Float
): Bitmap {
    val bitmap = toBitmap()

    val clip = (bitmap.width - screenWidth.toFloat() / screenHeight * bitmap.height.toFloat()) / 2

    val matrix = Matrix().apply {
        this.setRotate(rotate)
        this.setScale(screenWidth.toFloat() / (bitmap.width - clip.toInt() * 2), screenHeight.toFloat() / bitmap.height)
    }
    val screenBitmap = Bitmap.createBitmap(
        bitmap,
        clip.toInt(),
        0,
        (bitmap.width - clip.toInt() * 2),
        bitmap.height,
        matrix,
        true
    )

    val matrix2 = Matrix().apply {
        this.setScale(608f / rectSize, 608f / rectSize)
    }

    return Bitmap.createBitmap(
        screenBitmap,
        rectX,
        rectY,
        rectSize,
        rectSize,
        matrix2,
        true
    )

//    return Bitmap.createScaledBitmap(bitmap, 608, 608, true)
}