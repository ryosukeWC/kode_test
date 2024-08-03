package com.example.kode.presentation.feature.workers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kode.data.api.WorkersApi
import com.example.kode.data.api.retrofit.RetrofitInstance
import com.example.kode.data.api.mappers.toWorkerListPOJO
import com.example.kode.domain.util.ResponseResult
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

    private val _state = MutableStateFlow<ResponseResult<List<Worker>>>(ResponseResult.Loading())
    val state: StateFlow<ResponseResult<List<Worker>>> = _state

    init {
        loadWorkersList()
    }

    fun loadWorkersList() {

        viewModelScope.launch(Dispatchers.IO) {

            _state.value = ResponseResult.Loading()
            try {
                val fetchList = api.getWorkers().toWorkerListPOJO()
                _listWorkers.value = fetchList
                _state.value = ResponseResult.Success(fetchList)
            }
            catch (exception : Exception) {
                _state.value = ResponseResult.Error(exception.message)
            }

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