package com.example.kode.presentation.workers

import com.example.kode.data.ResponseResult
import com.example.kode.data.model.Worker

sealed class UiState() {

    data object Loading : UiState()
    data class Success(val workersList: List<Worker>) : UiState()
    data class Error(val message: String) : UiState()
}

internal fun ResponseResult<List<Worker>>.toUiState() : UiState {
    return when (this) {
        is ResponseResult.Loading -> UiState.Loading
        is ResponseResult.Success -> UiState.Success(data ?: emptyList())
        is ResponseResult.Error -> UiState.Error(error ?: "Unknown exception")
    }
}