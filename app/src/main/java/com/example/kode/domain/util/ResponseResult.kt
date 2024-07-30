package com.example.kode.domain.util

sealed class ResponseResult<T>(val data : T? = null, val error : String? = null) {
    class Success<T>(data: T?) : ResponseResult<T>(data)
    class Error<T>(message: String?) : ResponseResult<T>(error = message)
    class Loading<T> : ResponseResult<T>()
}
