package com.example.kode.presentation.feature.workers.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kode.databinding.FragmentWorkersBinding
import com.example.kode.model.Worker
import com.example.kode.presentation.feature.workers.adapter.WorkersAdapter
import com.example.kode.presentation.feature.workers.viewmodel.WorkersViewModel
import kotlinx.coroutines.launch

class WorkersFragment : Fragment() {

    private var _binding: FragmentWorkersBinding? = null
    private val binding get() = _binding!! // зачем это нужно?

    private val viewModel : WorkersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.workersRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = WorkersAdapter()
        binding.workersRv.adapter = adapter

        var receivedList : List<Worker>

        lifecycleScope.launch {
            viewModel.listWorkers.collect { workers ->
                receivedList = workers
                adapter.submitList(receivedList)
            }
        }

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    val originalList = adapter.currentList
                    val tempList = originalList
                    val filteredList = originalList.filter { worker ->
                        worker.fullName.contains(it, ignoreCase = true)
                    }
                    adapter.submitList(filteredList)
                }

                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}