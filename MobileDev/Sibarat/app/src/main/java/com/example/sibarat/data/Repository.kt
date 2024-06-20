package com.example.sibarat.data

import androidx.lifecycle.liveData
import com.example.sibarat.data.api.ApiService
import com.example.sibarat.data.api.response.UploadResponse
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.HttpException

class Repository private constructor(private var apiService: ApiService) {

    fun uploadImage(image: MultipartBody.Part) = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.uploadImage(image)
            emit(Result.Success(response))
        } catch (error: HttpException) {
            val jsonInString = error.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UploadResponse::class.java)
            emit(Result.Error(errorBody.message))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}