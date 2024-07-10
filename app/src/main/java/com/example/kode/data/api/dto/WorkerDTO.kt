package com.example.kode.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkerDTO(

    val id: String,
    @SerialName("avatarUrl") val avatarUrl: String,
    val firstName: String,
    val lastName: String,
    val userTag: String,
    val department: String,
    val position: String,
    val birthday: String,
    val phone: String
)


