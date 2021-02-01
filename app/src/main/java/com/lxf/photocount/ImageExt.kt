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

fun ImageProxy.toBitmap608(rotate: Float): Bitmap {
    val bitmap = toBitmap()

    val matrix = Matrix().apply {
        this.setScale(608f / bitmap.width, 608f / bitmap.height)
        this.setRotate(rotate)
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

//    return Bitmap.createScaledBitmap(bitmap, 608, 608, true)
}