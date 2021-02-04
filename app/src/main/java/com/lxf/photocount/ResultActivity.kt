package com.lxf.photocount

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lxf.photocount.databinding.ActivityResultBinding
import java.io.File

class ResultActivity : AppCompatActivity() {
    private lateinit var model: ResultViewModel
    private var photoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(ResultViewModel::class.java)

        val chess = intent.getStringExtra("chess")
        photoPath = intent.getStringExtra("photoPath")
        chess?.let {
            binding.chessView.setChess(it)
        }
        binding.imageView.setImageBitmap(BitmapFactory.decodeFile(photoPath))

        model.result.observe(this, Observer {
            if (it == 0)
                Toast.makeText(this, "上报失败", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "上报成功", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_result_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_error_up -> {
                photoPath?.let {
                    model.errorUp(File(it))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}