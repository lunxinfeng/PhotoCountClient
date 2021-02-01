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

fun ImageProxy.toBitmap608(): Bitmap {
    val bitmap = toBitmap()

//    val matrix = Matrix().apply {
//        this.setScale(0.5f, 0.5f)
//    }
//    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    return Bitmap.createScaledBitmap(bitmap, 608, 608, true)
}