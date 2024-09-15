package com.example.kode.feature.workers.view

import LoadingAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kode.R
import com.example.kode.databinding.FragmentWorkersBinding
import com.example.kode.feature.workers.UiState
import com.example.kode.feature.workers.adapter.WorkersAdapter
import com.example.kode.feature.workers.common.OnRadioButtonClickListener
import com.example.kode.feature.workers.topappbar.TopBarConfiguration
import com.example.kode.feature.workers.viewmodel.WorkersViewModel
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
    ): View? {
        _binding = FragmentWorkersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = view.findNavController()

        val recyclerView = binding.workersRv

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = WorkersAdapter()
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        viewLifecycleOwner.lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.state.collect { state ->
                        when (state) {
                            is UiState.Success -> {
                                binding.workersRv.adapter = adapter
                                adapter.submitList(state.workersList)
                            }
                            is UiState.Error -> navController.navigate(R.id.action_workersFragment_to_errorFragment)
                            is UiState.Loading -> {
                                binding.workersRv.adapter = LoadingAdapter()
                            }
                        }
                    }

                }

                launch {
                    viewModel.adapterList.collect { filteredList ->
                        adapter.submitList(filteredList)
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

        binding.swipeRefreshLayout.setOnRefreshListener {
            // определить имя таба и загрузить только нужный
//            viewModel.selectedTabPosition?.let {
//                val selectedTab = binding.tabLayout.getTabAt(it)
//                viewModel.loadWorkers()
//                viewModel.filterTheListByDepartment(selectedTab?.text.toString())
//            }
            binding.swipeRefreshLayout.isRefreshing = false
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