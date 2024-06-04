package com.example.sibarat

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sibarat.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupButton()
    }

    private fun setupButton() {
        binding.btnSelesai.setOnClickListener {
            val finsihIntent = Intent(this@CameraActivity, ResultActivity::class.java)
            startActivity(finsihIntent)
            finish()
        }

        binding.btnScan.setOnClickListener {
            //TODO: Fungsi scan gambar
        }

    }
}