package com.lxf.photocount

import android.graphics.*
import android.media.Image
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


fun ImageProxy.toBitmap(): Bitmap {
    val yBuffer = planes[0].buffer // Y
    val vuBuffer = planes[2].buffer // VU

    val ySize = yBuffer.remaining()
    val vuSize = vuBuffer.remaining()

    val nv21 = ByteArray(ySize + vuSize)

    yBuffer.get(nv21, 0, ySize)
    vuBuffer.get(nv21, ySize, vuSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

//fun ImageProxy.toBitmap(): Bitmap {
//    val buffer = planes[0].buffer
//    buffer.rewind()
//    val bytes = ByteArray(buffer.capacity())
//    buffer.get(bytes)
//    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//}

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