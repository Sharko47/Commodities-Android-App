package com.carnot.commodities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carnot.commodities.data.api.helper.NetworkResult
import com.carnot.commodities.data.model.Record
import com.carnot.commodities.databinding.ActivityMainBinding
import com.carnot.commodities.utils.hide
import com.carnot.commodities.utils.show
import com.carnot.commodities.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var commodityList: MutableList<Record>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        commodityList = mutableListOf()
        val commodityAdapter = CommodityAdapter(commodityList)
        binding.commodityRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = commodityAdapter
            addOnScrollListener(scrollListener)
        }

//        binding.backBtnContainer.setOnClickListener { onBackPressed() }
        mainViewModel.getLatestCommodityList()

        binding.swipeToRefreshLayout.setOnRefreshListener {
            commodityList.clear()
            commodityAdapter.notifyDataSetChanged()
            mainViewModel.totalDataCount = 0
            mainViewModel.currentPage = 0
            mainViewModel.commoditiesResponse = null
            mainViewModel.getLatestCommodityList()
        }

        mainViewModel.response.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    showToast(it.message.toString())
                    isLoading = false
                    binding.swipeToRefreshLayout.isRefreshing = isLoading
                }
                is NetworkResult.Loading -> {
                    isLoading = true
                    binding.swipeToRefreshLayout.isRefreshing = isLoading
                }
                is NetworkResult.Success -> {
                    isLoading = false
                    binding.swipeToRefreshLayout.isRefreshing = isLoading
                    it.data?.let { data ->
                        commodityList.clear()
                        mainViewModel.totalDataCount = data.total ?: 0
                        data.records?.let { it1 -> commodityList.addAll(it1) }
                        val totalPages = mainViewModel.totalDataCount / 10 + 2
                        isLastPage = mainViewModel.currentPage == totalPages
                        commodityAdapter.notifyDataSetChanged()
                        if (mainViewModel.currentPage != 1) {
                            binding.commodityRecyclerView.smoothScrollBy(200, 200)
                        }

                    }
                }
            }
        }
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 10
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                mainViewModel.getLatestCommodityList()
                isScrolling = false
            } else {
                binding.commodityRecyclerView.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

}