package com.example.kode.feature.error.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.kode.R
import com.example.kode.databinding.FragmentErrorBinding
import com.example.kode.feature.workers.viewmodel.WorkersViewModel

class ErrorFragment : Fragment() {

    private var _binding: FragmentErrorBinding? = null
    private val binding get() = _binding!!

    private val viewModel : WorkersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = binding.root.findNavController()
        binding.retryButton.setOnClickListener {
            controller.navigate(R.id.action_errorFragment_to_workersFragment)
            viewModel.loadWorkers()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}