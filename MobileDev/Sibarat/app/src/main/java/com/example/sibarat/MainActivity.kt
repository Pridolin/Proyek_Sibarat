package com.example.sibarat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sibarat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupButton()
    }

    private fun setupButton() {
        binding.btnScanFoto.setOnClickListener {
            val scanIntent = Intent(this@MainActivity, CameraActivity::class.java)
            startActivity(scanIntent)
        }

        binding.btnKamus.setOnClickListener {
            val kamusIntent = Intent(this@MainActivity, DictionaryActivity::class.java)
            startActivity(kamusIntent)
        }
    }
}