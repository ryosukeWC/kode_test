package com.example.kode.data.api

import com.example.kode.data.api.dto.WorkersListDTO
import retrofit2.http.GET
import retrofit2.http.Headers

interface WorkersApi {
    @Headers("Accept: application/json, application/xml", "Prefer: code=200, example=success")
    @GET("users")
    suspend fun getWorkers() : WorkersListDTO
}