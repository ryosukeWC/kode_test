package com.example.kode.data

import com.example.kode.data.api.retrofit.RetrofitImlp
import com.example.kode.model.Worker

class Mappers {

    suspend fun workerDTOtoPojo() : List<Worker> {
        return RetrofitImlp.api.getWorkers().workersListDTO.map { workerDTO ->
            Worker(
                id = workerDTO.id,
                fullName =  "${workerDTO.firstName} ${workerDTO.lastName}",
                post = workerDTO.position,
                imageUrl = workerDTO.avatarUrl
            )
        }
    }
}