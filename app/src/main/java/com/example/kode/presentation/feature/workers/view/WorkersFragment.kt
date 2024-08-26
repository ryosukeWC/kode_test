package com.example.kode.presentation.feature.workers.view

import LoadingAdapter
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kode.R
import com.example.kode.databinding.FragmentWorkersBinding
import com.example.kode.domain.util.ResponseResult
import com.example.kode.presentation.feature.workers.common.tabFilterMap
import com.example.kode.presentation.feature.workers.adapter.WorkersAdapter
import com.example.kode.presentation.feature.workers.common.OnRadioButtonClickListener
import com.example.kode.presentation.feature.workers.topappbar.TopBarConfiguration
import com.example.kode.presentation.feature.workers.viewmodel.WorkersViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkersFragment : Fragment(), OnRadioButtonClickListener {

    private var _binding: FragmentWorkersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkersViewModel by viewModels()

    private lateinit var adapter: WorkersAdapter

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

        val navController = binding.root.findNavController()

        binding.workersRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = WorkersAdapter()
        binding.workersRv.adapter = adapter

        lifecycleScope.launch {

            launch {
                viewModel.state.collect {
                    when (it) {
                        is ResponseResult.Success -> {
                            binding.workersRv.adapter = adapter
                            adapter.submitList(it.data)
                        }

                        is ResponseResult.Error -> navController.navigate(R.id.action_workersFragment_to_errorFragment)
                        is ResponseResult.Loading -> {
                            binding.workersRv.adapter = LoadingAdapter()
                        }
                    }
                }
            }

            launch {
                lifecycleScope.launch {
                    viewModel.currencyList.collect {
                        adapter.submitList(it)
                    }
                }
            }
        }

        val topAppBar = TopBarConfiguration(viewModel,adapter,binding,requireContext())
        topAppBar.configureSearch(binding.searchView)
        topAppBar.configureTab(binding.tabLayout)

        binding.filterView.setOnClickListener {
            val modalBottomSheet = BottomFragment()
            modalBottomSheet.setListener(this)
            modalBottomSheet.show(childFragmentManager, BottomFragment.TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickAlphabet() {
        viewModel.filterListByAlphaBet()
    }

    override fun onClickBirthday() {
        viewModel.filterByBirthday()
    }
}