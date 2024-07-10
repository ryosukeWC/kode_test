package com.example.kode.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkersListDTO(
    @SerialName("items") val workersListDTO: List<WorkerDTO>
)
