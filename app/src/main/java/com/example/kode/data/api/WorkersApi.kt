package com.example.kode.data.api

import com.example.kode.data.api.dto.WorkerDTO
import retrofit2.http.GET

interface WorkersApi {
    @GET("/users")
    suspend fun getWorkers() : List<WorkerDTO>
}