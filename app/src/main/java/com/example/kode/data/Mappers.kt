package com.example.kode.data

import com.example.kode.data.api.dto.WorkerDTO
import com.example.kode.model.Worker

class Mappers {

    fun workerDTOtoPojo(listWorkersDTO : List<WorkerDTO>) : List<Worker> {
        return listWorkersDTO.map { workerDTO ->
            Worker(
                id = workerDTO.id,
                fullName =  "${workerDTO.firstName} ${workerDTO.lastName}",
                position = workerDTO.position,
                imageUrl = workerDTO.avatarUrl,
                department = workerDTO.department
            )
        }
    }
}