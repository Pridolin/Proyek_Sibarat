package com.example.sibarat.activity.dictionary

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            intent.getParcelableExtra(KEY, Alphabet::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(KEY)
        }

        if (dataAlphabet != null) {
            binding.tvAlphabet.text = dataAlphabet.alphabet
            binding.imgPhoto.setImageResource(dataAlphabet.photo)
        }
    }

    companion object {
        const val KEY = "key"
    }
}