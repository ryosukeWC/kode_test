package com.example.kode.feature.workers.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kode.data.repository.WorkersRepository
import com.example.kode.data.model.Worker
import com.example.kode.feature.workers.UiState
import com.example.kode.feature.workers.adapter.WorkersAdapter
import com.example.kode.feature.workers.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkersViewModel @Inject constructor(
    private val repository: WorkersRepository,
    private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state

    private val _adapterList = MutableStateFlow<List<Worker>>(emptyList())
    val adapterList: StateFlow<List<Worker>> = _adapterList

    init {
        loadWorkers()
    }

    fun loadWorkers() {
        viewModelScope.launch {
            repository.getWorkersFromServer().map { it.toUiState() }.collect { state ->
                _state.value = state
            }
        }
    }

    fun filterTheListByDepartment(departmentName: String) {
        viewModelScope.launch {
            if (_state.value is UiState.Success) {
                _adapterList.value =
                    getFetchedList().filter { worker ->
                        worker.department.contains(departmentName, ignoreCase = true)
                    }
            }
        }
    }

    private fun getFetchedList(): List<Worker> {
        return (_state.value as UiState.Success).workersList
    }

    fun setFetchedListToAdapter() {
        _adapterList.value = (_state.value as UiState.Success).workersList
    }

    fun searchFilterByName(name : String, adapter: WorkersAdapter) {
        val filteredList = _adapterList.value.filter { worker ->
            val fullName = "${worker.firstName} ${worker.lastName}"
            fullName.contains(name, ignoreCase = true)
        }
        adapter.submitList(filteredList)
    }

    fun filterListByAlphaBet() {
        if (_adapterList.value.isEmpty()) {
            _adapterList.value = getFetchedList().sortedBy { it.firstName }
        }
        else {
            val currencyList = _adapterList.value
            _adapterList.value = currencyList.sortedBy { it.firstName }
        }
    }

    fun filterByBirthday() {
        if (_adapterList.value.isEmpty()) {
            _adapterList.value = getFetchedList().sortedBy { it.birthday }
        }
        else {
            val currencyList = _adapterList.value
            _adapterList.value = currencyList.sortedBy { it.birthday }
        }
    }


    var selectedTabPosition : Int?
        get() = savedStateHandle[SELECTED_TAB_POSITION_KEY]
        set(value) {
            savedStateHandle[SELECTED_TAB_POSITION_KEY] = value
        }

    companion object {
        const val SELECTED_TAB_POSITION_KEY = "SELECTED_TAB_POSITION"
    }
}