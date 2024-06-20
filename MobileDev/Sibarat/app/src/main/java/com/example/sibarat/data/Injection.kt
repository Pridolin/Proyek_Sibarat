package com.example.sibarat.data

import android.content.Context
import com.example.sibarat.data.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}