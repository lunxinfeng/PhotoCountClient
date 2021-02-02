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
    previewWidth: Int,
    previewHeight: Int,
    rotate: Float
): Bitmap {

    val bitmap = toBitmap()

    //旋转
    val matrixRotate = Matrix().apply { this.setRotate(rotate) }
    val rotateBitmap = Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrixRotate,
        true
    )


    //裁剪
    val clip =
        (rotateBitmap.height - previewHeight.toFloat() / previewWidth * rotateBitmap.width.toFloat()) / 2

    val matrixScreen = Matrix().apply {
        this.setScale(
            previewWidth.toFloat() / rotateBitmap.width,
            previewHeight.toFloat() / (rotateBitmap.height - clip.toInt() * 2)
        )
    }
    val screenClipBitmap = Bitmap.createBitmap(
        rotateBitmap,
        0,
        clip.toInt(),
        rotateBitmap.width,
        rotateBitmap.height - clip.toInt() * 2,
        matrixScreen,
        true
    )

    //压缩
    return Bitmap.createScaledBitmap(screenClipBitmap, 608, 608, true)
}