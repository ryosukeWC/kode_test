package com.example.kode.presentation.feature.workers.view

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kode.databinding.FragmentWorkersBinding
import com.example.kode.model.Worker
import com.example.kode.presentation.feature.workers.adapter.WorkersAdapter
import com.example.kode.presentation.feature.workers.viewmodel.WorkersViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.launch

class WorkersFragment : Fragment() {

    private var _binding: FragmentWorkersBinding? = null
    private val binding get() = _binding!!

    private val viewModel : WorkersViewModel by viewModels()

    private lateinit var fetchList: List<Worker>
    private lateinit var adapter: WorkersAdapter
    private lateinit var currencyList: List<Worker>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (savedInstanceState != null) {

        }
        _binding = FragmentWorkersBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.workersRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = WorkersAdapter()
        binding.workersRv.adapter = adapter

        lifecycleScope.launch {
            viewModel.listWorkers.collect { workers ->
                fetchList = workers
                currencyList = workers
                adapter.submitList(fetchList)
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
                    val filteredList = currencyList.filter { worker ->
                        worker.fullName.contains(it, ignoreCase = true)
                    }
                    adapter.submitList(filteredList)
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

        tab.addTab(tab.newTab().setText("Все"))
        tab.addTab(tab.newTab().setText("Designers"))
        tab.addTab(tab.newTab().setText("Analysts"))
        tab.addTab(tab.newTab().setText("Managers"))
        tab.addTab(tab.newTab().setText("iOS"))
        tab.addTab(tab.newTab().setText("Android"))
        tab.addTab(tab.newTab().setText("QA"))
        tab.addTab(tab.newTab().setText("Back Office"))
        tab.addTab(tab.newTab().setText("Frontend"))
        tab.addTab(tab.newTab().setText("Backend"))
        tab.addTab(tab.newTab().setText("HR"))
        tab.addTab(tab.newTab().setText("PR"))
        tab.addTab(tab.newTab().setText("Support"))

        tab.addOnTabSelectedListener(object : OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val tabName = it.text.toString()
                    when (tabName) {
                        "Все" -> {
                            adapter.submitList(fetchList)
                        }
                        "Android" -> {
                            setToAdapterFilteredList("android")
                        }
                        "Designers" -> {
                            setToAdapterFilteredList("design")
                        }
                        "iOS" -> {
                            setToAdapterFilteredList("ios")
                        }
                        "Managers" -> {
                            setToAdapterFilteredList("management")
                        }
                        "QA" -> {
                            setToAdapterFilteredList("qa")
                        }
                        "Back Office" -> {
                            setToAdapterFilteredList("back_office")
                        }
                        "Frontend" -> {
                            setToAdapterFilteredList("frontend")
                        }
                        "HR" -> {
                            setToAdapterFilteredList("hr")
                        }
                        "PR" -> {
                            setToAdapterFilteredList("pr")
                        }
                        "Backend" -> {
                            setToAdapterFilteredList("backend")
                        }
                        "Analysts" -> {
                            setToAdapterFilteredList("analytics")
                        }
                        "Support" -> {
                            setToAdapterFilteredList("support")
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

    private fun setToAdapterFilteredList(departmentName : String) {
        currencyList = fetchList.filter { worker ->
            worker.department.contains(departmentName, ignoreCase = true)
        }
        adapter.submitList(currencyList)
    }

}