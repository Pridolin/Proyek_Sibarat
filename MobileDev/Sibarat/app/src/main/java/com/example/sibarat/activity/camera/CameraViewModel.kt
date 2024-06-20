package com.example.sibarat.activity.camera

import androidx.lifecycle.ViewModel
import com.example.sibarat.data.Repository
import okhttp3.MultipartBody

class CameraViewModel (private val repository: Repository): ViewModel() {
    fun uploadImage(image: MultipartBody.Part) = repository.uploadImage(image)

}