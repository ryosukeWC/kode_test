package com.example.kode.data.api.retrofit

import com.example.kode.data.api.WorkersApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RetrofitImlp{

    private val contentType = "application/json".toMediaType()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    val api : WorkersApi by lazy { retrofit.create(WorkersApi::class.java) }

    companion object {
        const val BASE_URL = ""
    }
}
