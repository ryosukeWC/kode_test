package com.example.kode.feature.workers.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kode.data.repository.WorkersRepository
import com.example.kode.data.model.Worker
import com.example.kode.feature.workers.UiState
import com.example.kode.feature.workers.adapter.WorkersAdapter
import com.example.kode.feature.workers.toUiState
import com.google.android.material.tabs.TabLayout
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
                    (_state.value as UiState.Success).workersList.filter { worker ->
                        worker.department.contains(departmentName, ignoreCase = true)
                    }
            }
        }
    }

    fun getFetchedList(): List<Worker> {
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
//
//    fun filterListByAlphaBet(currencyList : List<Worker>) : List<Worker> {
//        val filteredList = currencyList.sortedBy { it.firstName }
//        return filteredList
//    }
//
//    fun filterByBirthday() {
//        val filteredList = _currencyList.value.sortedBy { it.birthday }
//        _currencyList.value = filteredList
//    }
//

    var selectedTab : String?
        get() = savedStateHandle["SELECTED_TAB_LEY"]
        set(value) {
            savedStateHandle["SELECTED_TAB_LEY"] = value
        }

//    fun setDataToStateHandle(selectedTab : String) {
//        savedStateHandle[SELECTED_TAB_LEY] = selectedTab
//    }

    companion object {
        const val SELECTED_TAB_LEY = "SELECTED_TAB"
    }
}