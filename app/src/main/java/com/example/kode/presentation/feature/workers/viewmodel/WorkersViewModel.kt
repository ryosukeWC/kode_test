package com.example.kode.presentation.feature.workers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kode.data.Mappers
import com.example.kode.model.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkersViewModel : ViewModel() {

    private val _listWorkers = MutableStateFlow(emptyList<Worker>())
    val listWorkers: StateFlow<List<Worker>> = _listWorkers

    init {
        loadWorkersList()
    }

    fun loadWorkersList() {
        viewModelScope.launch(Dispatchers.IO) {
            _listWorkers.value = Mappers().workerDTOtoPojo()
        }
    }

}