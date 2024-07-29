package com.example.kode.presentation.feature.workers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kode.data.Mappers
import com.example.kode.data.api.WorkersApi
import com.example.kode.data.api.retrofit.RetrofitInstance
import com.example.kode.model.Worker
import com.example.kode.presentation.feature.workers.adapter.WorkersAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkersViewModel : ViewModel() {

    private val _listWorkers = MutableStateFlow(emptyList<Worker>())
    val listWorkers: StateFlow<List<Worker>> = _listWorkers

    private val api : WorkersApi = RetrofitInstance.api

    private val currencyList = MutableStateFlow(emptyList<Worker>())

    init {
        loadWorkersList()
    }

    private fun loadWorkersList() {

        // сделать обработку ошибок

        viewModelScope.launch(Dispatchers.IO) {
            _listWorkers.value = Mappers().workerDTOtoPojo(api.getWorkers().workersListDTO)
        }
    }

    fun filterListByDepartment(departmentName : String, adapter: WorkersAdapter) {
        currencyList.value = _listWorkers.value.filter { worker ->
            worker.department.contains(departmentName, ignoreCase = true)
        }
        adapter.submitList(currencyList.value)
    }

    fun searchFilterByName(name : String, adapter: WorkersAdapter) {
        val filteredList = currencyList.value.filter { worker ->
            worker.fullName.contains(name, ignoreCase = true)
        }
        adapter.submitList(filteredList)
    }

}