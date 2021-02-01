package com.lxf.photocount

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lxf.photocount.databinding.ActivityPhotoBinding
import java.io.File

class PhotoActivity : AppCompatActivity() {
    private lateinit var model: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val photoPath = intent.getStringExtra("photoPath")
        binding.imageViewPhoto.setImageBitmap(BitmapFactory.decodeFile(photoPath))

        model = ViewModelProvider(this).get(PhotoViewModel::class.java)

        val progressDialog = ProgressDialog(this).apply {
            this.setCancelable(false)
            this.setMessage("正在识别...")
        }
        model.photoPath.value = photoPath
        model.chess.observe(this, Observer {
            progressDialog.dismiss()
            if (it == null || it.isEmpty()){
                Toast.makeText(this,"请求失败", Toast.LENGTH_LONG).show()
                return@Observer
            }
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("chess", it)
            intent.putExtra("photoPath", photoPath)
            startActivity(intent)
        })

        binding.btnRePhoto.setOnClickListener {
            finish()
        }

        binding.btnStart.setOnClickListener {
            photoPath?.let {
                progressDialog.show()
                model.photoCount(File(photoPath))
            }?: kotlin.run {
                Toast.makeText(this,"照片路径不对: $photoPath", Toast.LENGTH_LONG).show()
            }
        }
    }
}