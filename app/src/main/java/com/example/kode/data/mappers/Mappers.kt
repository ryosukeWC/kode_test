package com.example.kode.data.api.mappers

import com.example.kode.data.api.dto.WorkersListDTO
import com.example.kode.model.Worker
import kotlin.collections.map

fun WorkersListDTO.toWorkerListPOJO() : List<Worker> {
    return workersListDTO.map { workerDTO ->
        Worker(
            id = workerDTO.id ?: "",
            firstName = workerDTO.firstName ?: "",
            lastName = workerDTO.lastName ?: "",
            birthday = workerDTO.birthday ?: "",
            phone = workerDTO.phone ?: "",
            userTag = workerDTO.userTag?.lowercase() ?: "",
            position = workerDTO.position ?: "",
            imageUrl = workerDTO.avatarUrl ?: "",
            department = workerDTO.department ?: ""
        )
    }
}