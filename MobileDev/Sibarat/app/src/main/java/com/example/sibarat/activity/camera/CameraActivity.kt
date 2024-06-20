package com.example.sibarat.activity.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sibarat.R
import com.example.sibarat.ViewModelFactory
import com.example.sibarat.activity.result.ResultActivity
import com.example.sibarat.data.Result
import com.example.sibarat.databinding.ActivityCameraBinding
import com.example.sibarat.getImageUri
import com.example.sibarat.reduceFileImage
import com.example.sibarat.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class CameraActivity : AppCompatActivity() {
    private val viewModel by viewModels<CameraViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityCameraBinding
    private var currentImageUri: Uri? = null
    private lateinit var multipartBody: MultipartBody.Part
    private var collectedAlphabet: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupButton()
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("showImage", "$it")
            binding.ivResultPhoto.setImageURI(it)
            showDeleteBtn(true)
        }
    }

    private fun deleteImage() {
        currentImageUri = null
        binding.ivResultPhoto.setImageResource(R.drawable.ic_image_placeholder)
    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                Log.d("Photo Picker", "No media selected.")
            }
        }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                showImage()
            }
        }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherCamera.launch(currentImageUri!!)
    }

    private fun setupButton() {
        binding.btnDelete.setOnClickListener {
            deleteImage()
            showDeleteBtn(false)
            showToast("Gambar berhasil dihapus.")
        }

        binding.btnReset.setOnClickListener {
            val tempAlphabet = collectedAlphabet
            collectedAlphabet = tempAlphabet!!.dropLast(1)
            binding.result.text = collectedAlphabet

            if (collectedAlphabet != "") {
                return@setOnClickListener showToast("Result berhasil dihapus.")
            }

            showResetBtn(false)
            showToast("Result berhasil dihapus.")
        }

        binding.btnQuestion.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Cara Pakai:")
                setMessage("1. Pastikan pencahayaan tidak terlalu gelap \n" +
                        "2. Gambar tangan berada di tengah \n" +
                        "3. Tidak ada objek lain pada gambar \n" +
                        "4. Maksimal huruf adalah 5")
                setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        binding.btnSelesai.setOnClickListener {
            val finishIntent = Intent(this@CameraActivity, ResultActivity::class.java)
                .putExtra("RESULT", "$collectedAlphabet")
            startActivity(finishIntent)
            finish()
        }

        binding.btnScan.setOnClickListener {
            if (currentImageUri == null) {
                return@setOnClickListener showToast("Masukkan gambar terlebih dahulu.")
            }
            if (collectedAlphabet?.length!! >= 5) {
                return@setOnClickListener showToast("Hasil mencapai batas. Tekan tombol ulang untuk menghapus huruf.")
            }
            uploadImage()
            viewModel.uploadImage(multipartBody).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Success -> {
                            showLoading(false)
                            showResetBtn(true)
                            val currentAlphabet = result.data.data.result
                            collectedAlphabet += currentAlphabet
                            binding.result.text = "$collectedAlphabet"
                            AlertDialog.Builder(this).apply {
                                setTitle("Berhasil")
                                setMessage(result.data.data.suggestion)
                                setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                create()
                                show()
                            }
                        }
                        is Result.Error -> {
                            showLoading(false)
                            AlertDialog.Builder(this).apply {
                                setTitle("Error")
                                setMessage(result.error)
                                setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                create()
                                show()
                            }
                        }
                        Result.Loading -> {
                            showLoading(true)
                        }
                    }
                }
            }
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this). reduceFileImage()

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

            val multipartImagePart = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            multipartBody = multipartImagePart
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDeleteBtn(isDelete: Boolean) {
        binding.btnDelete.visibility = if (isDelete) View.VISIBLE else View.GONE
    }

    private fun showResetBtn(isReset: Boolean) {
        binding.btnReset.visibility = if (isReset) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}