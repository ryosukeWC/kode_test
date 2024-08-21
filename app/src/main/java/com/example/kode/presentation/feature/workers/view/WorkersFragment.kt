package com.example.kode.presentation.feature.workers.view

import LoadingAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkersFragment : Fragment() {

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

        configureSearch(binding.searchView)
        configureTab(binding.tabLayout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configureSearch(searchView: SearchView) {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
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
                turnOnFocus(searchView)
            } else {
                turnOffFocus(binding.searchView)
            }
        }

        binding.cancelButton.setOnClickListener {
            binding.searchView.setQuery("", true)
            binding.searchView.clearFocus()
        }
    }

    private fun turnOnFocus(searchView: SearchView) {

        val searchIconId = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        val searchIcon = searchView.findViewById<ImageView>(searchIconId)

        searchIcon.setImageResource(R.drawable.search_focus_enabled)

        val layoutParams = searchView.layoutParams as MarginLayoutParams
        layoutParams.width = dpToPx(265)

        layoutParams.marginStart = dpToPx(16)
        layoutParams.marginEnd = dpToPx(0)
        searchView.layoutParams = layoutParams

        binding.cancelButton.visibility = View.VISIBLE
    }

    private fun turnOffFocus(searchView: SearchView) {
        binding.cancelButton.visibility = View.GONE
        val layoutParams = searchView.layoutParams as MarginLayoutParams

        layoutParams.width = MarginLayoutParams.MATCH_PARENT
        layoutParams.marginStart = dpToPx(16)
        layoutParams.marginEnd = dpToPx(16)
        searchView.layoutParams = layoutParams

    }

    private fun dpToPx(dp: Int): Int {
        val resources = requireContext().resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    private fun configureTab(tab: TabLayout) {

        for (department in tabFilterMap) {
            tab.addTab(tab.newTab().setText(department.key))
        }

        tab.addOnTabSelectedListener(object : OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                tab?.let {
                    val tabName = it.text.toString()

                    if (tabName == "Все") viewModel.transferFullListToCurrency()
                    else {
                        val departmentName = tabFilterMap[tabName]
                        departmentName?.let {
                            viewModel.filterListByDepartment(it)
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

//    private fun createMenuProvider() = object : MenuProvider {
//        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//            menuInflater.inflate(R.menu.search_menu,menu)
//            val searchItem = menu.findItem(R.id.app_bar_search)
//            val searchView = searchItem.actionView
//        }
//
//        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//            return true
//        }
//
//    }
}