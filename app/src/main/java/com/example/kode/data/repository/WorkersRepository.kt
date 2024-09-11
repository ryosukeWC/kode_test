package com.example.kode.data.repository

import android.util.Log
import com.example.kode.data.ResponseResult
import com.example.kode.data.api.WorkersApi
import com.example.kode.data.api.mappers.toWorkerListPOJO
import com.example.kode.data.model.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WorkersRepository @Inject constructor(private val api: WorkersApi) {

    fun getWorkersFromServer() : Flow<ResponseResult<List<Worker>>> = flow {

        emit(ResponseResult.Loading())

        try {
            val apiRequest = api.getWorkers()
            emit(ResponseResult.Success(apiRequest.toWorkerListPOJO()))
        }
        catch (exception : Exception) {
            val message = exception.message ?: "Unknown exception"
            Log.e(LOG_TAG,message)
            emit(ResponseResult.Error(message))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        const val LOG_TAG = "WorkersRepository"
    }
}