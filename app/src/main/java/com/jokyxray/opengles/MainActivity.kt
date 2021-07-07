package com.jokyxray.opengles

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jokyxray.opengles.databinding.ActivityMainBinding
import com.jokyxray.opengles.triangle.TriangleActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListener()
    }

    private fun setupListener() {
        binding.run {
            drawTriangle.setOnClickListener(this@MainActivity)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.draw_triangle -> {
                startActivity(Intent(this@MainActivity, TriangleActivity::class.java))
            }
        }
    }
}