package com.example.kode.presentation.feature.workers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kode.data.api.WorkersApi
import com.example.kode.data.api.mappers.toWorkerListPOJO
import com.example.kode.domain.util.ResponseResult
import com.example.kode.model.Worker
import com.example.kode.presentation.feature.workers.adapter.WorkersAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkersViewModel @Inject constructor(private val api: WorkersApi) : ViewModel() {

    private val _currencyList = MutableStateFlow(emptyList<Worker>())
    val currencyList : StateFlow<List<Worker>> = _currencyList

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
                _currencyList.value = fetchList
                _state.value = ResponseResult.Success(fetchList)
            }
            catch (exception : Exception) {
                _state.value = ResponseResult.Error(exception.message)
            }

        }
    }

    fun filterListByDepartment(departmentName : String) {
        _currencyList.value = getReceivedList().filter { worker ->
            worker.department.contains(departmentName, ignoreCase = true)
        }
    }

    fun transferFullListToCurrency() {
        _currencyList.value = getReceivedList()
    }

    fun searchFilterByName(name : String, adapter: WorkersAdapter) {
        val filteredList = currencyList.value.filter { worker ->
            worker.fullName.contains(name, ignoreCase = true)
        }
        adapter.submitList(filteredList)
    }
    
    private fun getReceivedList(): List<Worker> {
        return _state.value.data!!
    }

    fun getWorkerById(workerId: String): Worker? {
        return getReceivedList().firstOrNull { it.id == workerId }
    }


}