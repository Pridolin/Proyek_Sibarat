package com.example.sibarat

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sibarat.data.Alphabet
import com.example.sibarat.databinding.ActivityDetailDictionaryBinding

class DetailDictionaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDictionaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDictionaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val dataAlphabet = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<Alphabet>(key, Alphabet::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Alphabet>(key)
        }

        if (dataAlphabet != null) {
            binding.tvAlphabet.text = dataAlphabet.alphabet
            binding.imgPhoto.setImageResource(dataAlphabet.photo)
        }
    }

    companion object {
        const val key = "key"
    }
}