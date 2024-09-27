package com.example.kode.feature.workers.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kode.data.repository.WorkersRepository
import com.example.kode.data.model.Worker
import com.example.kode.feature.workers.UiState
import com.example.kode.feature.workers.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

                when (state) {
                    is UiState.Success -> {
                        _state.value = state
                        _adapterList.value = state.workersList
                    }
                    else -> _state.value = state
                }
            }
        }
    }

    fun refreshWorkers(departmentName: String) {
        viewModelScope.launch {

            repository.getWorkersFromServer().map { it.toUiState() }.collect { state ->

                when (state) {
                    is UiState.Success -> {
                        _state.value = state
                        if (departmentName == ALL_TAB) {
                            _adapterList.value = state.workersList
                        }
                        else {
                            filterTheListByDepartment(departmentName)
                        }
                    }
                    else -> _state.value = state
                }
            }
        }
    }

    fun filterTheListByDepartment(departmentName: String) {
        _adapterList.value = getFetchedList().filter { worker ->
            worker.department.contains(departmentName, ignoreCase = true)
        }
    }

    private fun getFetchedList(): List<Worker> {
        return (_state.value as UiState.Success).workersList
    }

    fun setFetchedListToAdapter() {
        _adapterList.value = (_state.value as UiState.Success).workersList
    }

    fun searchFilterByName(name : String) : List<Worker> {
        val filteredList = _adapterList.value.filter { worker ->
            val fullName = "${worker.firstName} ${worker.lastName}"
            fullName.contains(name, ignoreCase = true)
        }
        return filteredList
    }

    fun filterByAlphabet() : List<Worker> {
        val currencyList = _adapterList.value
        return currencyList.sortedBy { "${it.firstName} ${it.lastName}" }
    }

    fun filterByBirthday() : List<Worker> {

        val currencyList = _adapterList.value
        return currencyList.sortedBy { it.birthday }
    }


    var selectedTabPosition : Int?
        get() = savedStateHandle[SELECTED_TAB_POSITION_KEY]
        set(value) {
            savedStateHandle[SELECTED_TAB_POSITION_KEY] = value
        }

    companion object {
        const val SELECTED_TAB_POSITION_KEY = "SELECTED_TAB_POSITION"
        const val ALL_TAB = "Все"
    }
}