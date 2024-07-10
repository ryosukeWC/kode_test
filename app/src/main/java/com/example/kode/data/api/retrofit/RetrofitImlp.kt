package com.example.kode.data.api.retrofit

import com.example.kode.data.api.WorkersApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitImlp{

    private const val BASE_URL = "https://stoplight.io/mocks/kode-api/trainee-test/331141861/"

    private val contentType = "application/json".toMediaType()

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    val api : WorkersApi by lazy { retrofit.create(WorkersApi::class.java) }
}
