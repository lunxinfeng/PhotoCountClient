package com.lxf.photocount

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.lxf.photocount.databinding.ActivityCameraBinding
import java.io.File


class CameraActivity : AppCompatActivity() {
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startCamera()

        binding.btnPhoto.setOnClickListener {
            takePhoto()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder()
//                .setTargetResolution(Size(800, 800))
                .build()

            imageCapture = ImageCapture.Builder()
//                .setTargetResolution(Size(800, 800))
//                .setTargetRotation(getRotation(this, "0"))
//                .setTargetRotation(Surface.ROTATION_90)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            // Select back camera
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

                preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)


                val zoomState: LiveData<ZoomState> = camera?.cameraInfo?.zoomState?: return@Runnable
                println("处理${zoomState.value!!.minZoomRatio}")
                println("处理${zoomState.value!!.maxZoomRatio}")
                println("处理${zoomState.value!!.zoomRatio}")
                println("处理${zoomState.value!!.linearZoom}")
                camera?.cameraControl?.setZoomRatio(zoomState.value!!.minZoomRatio)
            } catch (exc: Exception) {
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create timestamped output file to hold the image
        val photoFile = File(
            cacheDir,
            System.currentTimeMillis().toString() + ".jpg"
        )

//        // Create output options object which contains file + metadata
//        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//        // Setup image capture listener which is triggered after photo has
//        // been taken
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onError(exc: ImageCaptureException) {
//                    exc.printStackTrace()
//                }
//
//                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    val savedUri = Uri.fromFile(photoFile)
//                    val msg = "Photo capture succeeded: $savedUri"
//                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//
//                    model.photoCount(photoFile)
//                }
//            })

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    image.toBitmap608(
                        binding.viewFinder.width,
                        binding.viewFinder.height,
                        image.imageInfo.rotationDegrees.toFloat()
                    )
                        .compress(
                            Bitmap.CompressFormat.JPEG,
                            100,
                            photoFile.outputStream()
                        )
                    super.onCaptureSuccess(image)

                    val intent = Intent(this@CameraActivity, PhotoActivity::class.java)
                    intent.putExtra("photoPath", photoFile.absolutePath)
                    startActivity(intent)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    exception.printStackTrace()
                }
            }
        )
    }
}