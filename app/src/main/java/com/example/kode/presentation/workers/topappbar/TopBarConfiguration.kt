package com.example.kode.presentation.workers.topappbar

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import com.example.kode.R
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.kode.presentation.workers.adapter.WorkersAdapter
import com.example.kode.presentation.workers.viewmodel.WorkersViewModel
import com.example.kode.databinding.FragmentWorkersBinding
import com.example.kode.presentation.workers.UiState
import com.example.kode.presentation.workers.common.tabFilterMap
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class TopBarConfiguration(
    private val viewModel: WorkersViewModel,
    private val adapter: WorkersAdapter,
    private val binding : FragmentWorkersBinding,
    private val context: Context,
    private val recyclerView: RecyclerView
) {

    fun configureSearch(searchView: SearchView) {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    val filteredList = viewModel.searchFilterByName(query)

                    if (filteredList.isEmpty()) {
                        binding.errorSearch.visibility = View.VISIBLE
                        binding.workersRv.visibility = View.GONE
                    }
                    else {
                        binding.errorSearch.visibility = View.GONE
                        binding.workersRv.visibility = View.VISIBLE
                        adapter.submitList(filteredList)
                    }
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
            binding.searchView.setQuery("", false)
            binding.searchView.clearFocus()
        }
    }

    private val searchIconId by lazy {
        context.resources.getIdentifier("android:id/search_mag_icon", null, null)
    }

    private val queryTextId by lazy {
        context.resources.getIdentifier("android:id/search_src_text",null,null)
    }

    private fun getSearchIcon(searchView: SearchView) : ImageView {
        return searchView.findViewById(searchIconId)
    }

    private fun turnOnFocus(searchView: SearchView) {

        getSearchIcon(searchView).setImageResource(R.drawable.search_focus_enabled)
        searchView.findViewById<EditText>(queryTextId).setTextColor(Color.BLACK)

        val layoutParams = searchView.layoutParams as MarginLayoutParams

        layoutParams.width = dpToPx(0)

        layoutParams.marginStart = dpToPx(16)
        layoutParams.marginEnd = dpToPx(0)
        searchView.layoutParams = layoutParams

        with(binding) {

            cancelButton.visibility = View.VISIBLE
            filterView.visibility = View.GONE
            filterButton.visibility = View.GONE
        }
    }

    private fun turnOffFocus(searchView: SearchView) {

        getSearchIcon(searchView).setImageResource(R.drawable.icon_search)

        binding.cancelButton.visibility = View.GONE
        val layoutParams = searchView.layoutParams as MarginLayoutParams

        layoutParams.width = MarginLayoutParams.MATCH_PARENT
        layoutParams.marginStart = dpToPx(16)
        layoutParams.marginEnd = dpToPx(16)
        searchView.layoutParams = layoutParams

        binding.filterView.visibility = View.VISIBLE
        binding.filterButton.visibility = View.VISIBLE

    }

    private fun dpToPx(dp: Int): Int {
        val resources = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    fun configureTab(tabLayout: TabLayout) {

        for (department in tabFilterMap) {
            tabLayout.addTab(tabLayout.newTab().setText(department.key))
        }

        val selectedTabPosition = viewModel.selectedTabPosition
        if (selectedTabPosition != null) {
            val selectedTab = tabLayout.getTabAt(selectedTabPosition)
            selectedTab?.select()
        }

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                tab?.let {

                    viewModel.selectedTabPosition = it.position

                    val tabName = it.text.toString()

                    if (tabName == ALL_TAB) {
                        viewModel.getListWithLoading(viewModel.getListFromSuccessState())
                        adapter.submitList((viewModel.state.value as UiState.Success).workersList)
                    }
                    else {
                        val departmentName = tabFilterMap[tabName]
                        departmentName?.let { departmentName ->
                            viewModel.filterTheListByDepartment(departmentName)
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

    companion object {
        const val ALL_TAB = "Все"
    }
}