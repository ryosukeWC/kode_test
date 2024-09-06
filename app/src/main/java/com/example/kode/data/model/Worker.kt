package com.example.kode.data.model

import java.io.Serializable

data class Worker (
    val id : String,
    val firstName : String,
    val lastName : String,
    val position : String,
    val imageUrl : String,
    val department : String,
    val userTag : String,
    val birthday : String,
    val phone : String
) : Serializable