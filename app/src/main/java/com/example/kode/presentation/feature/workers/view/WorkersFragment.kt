package com.example.kode.presentation.feature.workers.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.kode.databinding.FragmentWorkersBinding
import com.example.kode.model.Worker
import com.example.kode.presentation.feature.workers.adapter.WorkersAdapter



// 1. adapter V
// 2. item V
// 3. view holder for adapter V
//

class WorkersFragment : Fragment() {

    private var _binding: FragmentWorkersBinding? = null
    private val binding get() = _binding!! // зачем это нужно?

    val testList: List<Worker> = listOf(
        Worker(123,"Генадий Петрович", "Программист","123"),
        Worker(111,"Александр Зубков", "Не программист","123"),
        Worker(145,"Генадий Петрович 2", "Супер программист","123"),
        Worker(187,"Генадий Петрович 3", "Ага","123"),
        Worker(176,"Генадий Петрович 4", "Угу","123"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView : RecyclerView = binding.workersRv
        recyclerView.let {
            it.layoutManager = LinearLayoutManager(it.context)
            val adapter = WorkersAdapter()
            it.adapter = adapter
            adapter.submitList(testList)
        }
    }
}