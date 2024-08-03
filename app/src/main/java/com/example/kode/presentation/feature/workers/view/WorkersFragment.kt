package com.example.kode.presentation.feature.workers.view

import LoadingAdapter
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
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
import com.example.kode.presentation.feature.workers.viewmodel.WorkersViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.launch

class WorkersFragment : Fragment() {

    private var _binding: FragmentWorkersBinding? = null
    private val binding get() = _binding!!

    private val viewModel : WorkersViewModel by viewModels()
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
            viewModel.state.collect {
                when (it) {
                    is ResponseResult.Success -> {
                        binding.workersRv.adapter = adapter
                        adapter.submitList(it.data)
                    }

                    is ResponseResult.Error -> navController.navigate(R.id.action_workersFragment_to_errorFragment) // сделать переход на экран с ошибкой
                    is ResponseResult.Loading -> {
                        binding.workersRv.adapter = LoadingAdapter()
                    }
                }
            }
        }

        configureSearch(binding.searchView)
        configureTab(binding.tabLayout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun configureSearch(searchView: SearchView) {

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    viewModel.searchFilterByName(query,adapter)
                }
                return true
            }
        })

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                expandSearchView(searchView)
            } else {
                collapseSearchView(binding.searchView)
            }
        }

        binding.cancelButton.setOnClickListener {
            binding.searchView.setQuery("", true)
            binding.searchView.clearFocus()
        }
    }

    private fun expandSearchView(searchView: SearchView) {

        val newWidthInDp = 265
        val newWidthInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            newWidthInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        val layoutParams = searchView.layoutParams as MarginLayoutParams
        layoutParams.width = newWidthInPixels

        layoutParams.leftMargin = 16.dpToPx()
        layoutParams.rightMargin = 0.dpToPx()
        searchView.layoutParams = layoutParams

        binding.cancelButton.visibility = View.VISIBLE
    }

    private fun collapseSearchView(searchView: SearchView) {
        binding.cancelButton.visibility = View.GONE

        val layoutParams = searchView.layoutParams as MarginLayoutParams

        layoutParams.width = MarginLayoutParams.MATCH_PARENT
        layoutParams.rightMargin = 16.dpToPx()
        layoutParams.leftMargin = 16.dpToPx()
        searchView.layoutParams = layoutParams

    }

    private fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()

    }

    private fun configureTab(tab: TabLayout) {

        for (department in tabFilterMap) {
            tab.addTab(tab.newTab().setText(department.key))
        }

        tab.addOnTabSelectedListener(object : OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                tab?.let {
                    val tabName = it.text.toString()

                    if (tabName == "Все") adapter.submitList(viewModel.listWorkers.value)
                    else {
                        val departmentName = tabFilterMap[tabName]
                        departmentName?.let {
                            viewModel.filterListByDepartment(it, adapter)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

}