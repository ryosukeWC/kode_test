package com.example.kode.presentation.feature.workers.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kode.data.Mappers
import com.example.kode.data.api.dto.WorkersListDTO
import com.example.kode.databinding.FragmentWorkersBinding
import com.example.kode.model.Worker
import com.example.kode.presentation.feature.workers.adapter.WorkersAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkersFragment : Fragment() {

    private var _binding: FragmentWorkersBinding? = null
    private val binding get() = _binding!! // зачем это нужно?

    private lateinit var testList: List<Worker>

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

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val loadedList = Mappers().workerDTOtoPojo()

            withContext(Dispatchers.Main) {
                testList = loadedList
                adapter.submitList(testList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}