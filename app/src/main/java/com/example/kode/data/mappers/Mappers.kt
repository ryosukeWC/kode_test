package com.example.kode.data.api.mappers

import com.example.kode.data.api.dto.WorkersListDTO
import com.example.kode.model.Worker
import kotlin.collections.map

fun WorkersListDTO.toWorkerListPOJO() : List<Worker> {
    return workersListDTO.map { workerDTO ->
        Worker(
            id = workerDTO.id ?: "",
            fullName = "${workerDTO.firstName} ${workerDTO.lastName}",
            position = workerDTO.position ?: "",
            imageUrl = workerDTO.avatarUrl ?: "",
            department = workerDTO.department ?: ""
        )
    }
}