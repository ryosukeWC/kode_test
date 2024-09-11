package com.example.kode.feature.workers.topappbar

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import com.example.kode.R
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import com.example.kode.feature.workers.adapter.WorkersAdapter
import com.example.kode.feature.workers.viewmodel.WorkersViewModel
import com.example.kode.databinding.FragmentWorkersBinding
import com.example.kode.feature.workers.common.tabFilterMap
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class TopBarConfiguration(
    private val viewModel: WorkersViewModel,
    private val adapter: WorkersAdapter,
    private val binding : FragmentWorkersBinding,
    private val context: Context
) {

    fun configureSearch(searchView: SearchView) {

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

    private fun getSearchIcon(searchView: SearchView) : ImageView {
        val searchIconId = searchView.context.resources.getIdentifier("android:id/search_mag_icon", null, null);
        return searchView.findViewById(searchIconId)
    }

    private fun turnOnFocus(searchView: SearchView) {

        getSearchIcon(searchView).setImageResource(R.drawable.search_focus_enabled)

        val queryTextId = searchView.context.resources.getIdentifier("android:id/search_src_text",null,null)
        searchView.findViewById<EditText>(queryTextId).setTextColor(Color.BLACK)

        val layoutParams = searchView.layoutParams as MarginLayoutParams
        layoutParams.width = dpToPx(265)

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

    private fun setTabColor(tabLayout: TabLayout,tab: TabLayout.Tab) {

        val name = tab.text.toString()
        if (name != "Все") {
            tabLayout.setTabTextColors(
                ContextCompat.getColor(context,R.color.tabs_text_color),
                ContextCompat.getColor(context,R.color.tabs_text_color)
            )
        }
        else
        {
            tabLayout.setTabTextColors(
                ContextCompat.getColor(context,R.color.tabs_text_color),
                ContextCompat.getColor(context,R.color.black)
            )
        }
    }

    fun configureTab(tabLayout: TabLayout) {

        for (department in tabFilterMap) {
            tabLayout.addTab(tabLayout.newTab().setText(department.key))
        }

        val savedTab = viewModel.selectedTab
        val tabToSelect = tabLayout.tabCount
            .takeIf { it > 0 }
            ?.let { count ->
                (0 until count).firstNotNullOfOrNull { index ->
                    tabLayout.getTabAt(index)?.takeIf { it.text == savedTab }
                }
            }

        tabToSelect?.let {
            tabLayout.selectTab(it)
        }

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                setTabColor(tabLayout,tab!!)

                tab?.let {

                    val tabName = it.text.toString()
                    viewModel.selectedTab = tabName

                    if (tabName == "Все") {
                        viewModel.setFetchedListToAdapter()
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


}