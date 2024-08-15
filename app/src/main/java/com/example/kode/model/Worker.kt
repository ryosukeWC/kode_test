package com.example.kode.model

import java.io.Serializable

data class Worker (
    val id : String,
    val fullName : String,
    val position : String,
    val imageUrl : String,
    val department : String
) : Serializable