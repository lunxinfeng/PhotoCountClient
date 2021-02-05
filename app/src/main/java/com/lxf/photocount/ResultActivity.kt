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
import java.lang.StringBuilder

class ResultActivity : AppCompatActivity() {
    private lateinit var model: ResultViewModel
    private lateinit var binding: ActivityResultBinding
    private var photoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(ResultViewModel::class.java)

        val chess = intent.getStringExtra("chess")
        photoPath = intent.getStringExtra("photoPath")
        chess?.let {
            binding.chessView.setChess(it)
        }
        binding.imageView.setImageBitmap(BitmapFactory.decodeFile(photoPath))

        model.errorUpResult.observe(this, Observer {
            if (it == 0)
                Toast.makeText(this, "上报失败", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "上报成功", Toast.LENGTH_SHORT).show()
        })

        model.situationResult.observe(this, Observer {
            if (it == null){
                Toast.makeText(this, "请求失败", Toast.LENGTH_SHORT).show()
                return@Observer
            }
            binding.chessView.markPiece(it.board_state)
            binding.tvResult.text = "白方胜率：${String.format("%.2f",it.whiteWinRate * 100)}%\n白方领先：${String.format("%.2f",it.whiteWinScore)}目"
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
            R.id.action_situation -> {
                model.situation(binding.chessView.yzSgf())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun ChessView.yzSgf(): String {
        fun Int.to2(): String {
            return if (this < 10) "0$this" else this.toString()
        }

        var value: Int
        val sgf = StringBuilder("")
        for (i in 1 .. boardSize) {
            for (j in 1 .. boardSize) {
                value = chessArray[i-1][j-1]
                when (value) {
                    1 -> {
                        sgf.append("+")
                            .append(j.to2())
                            .append(i.to2())
                    }
                    2 -> {
                        sgf.append("-")
                            .append(j.to2())
                            .append(i.to2())
                    }
                }
            }
        }
        return sgf.toString()
    }
}