package com.lxf.photocount

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lxf.photocount.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chess = intent.getStringExtra("chess")
        val photoPath = intent.getStringExtra("photoPath")
        chess?.let {
            binding.chessView.setChess(it)
        }
        binding.imageView.setImageBitmap(BitmapFactory.decodeFile(photoPath))
    }
}