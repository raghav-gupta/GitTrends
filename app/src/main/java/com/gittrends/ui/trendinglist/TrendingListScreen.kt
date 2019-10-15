package com.gittrends.ui.trendinglist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.entity.TrendingItem
import com.gittrends.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_trending_list_screen.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.header_view.*
import kotlinx.android.synthetic.main.list_view.*
import javax.inject.Inject


class TrendingListScreen : AppCompatActivity() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    private lateinit var trendingListViewModel: TrendingListViewModel

    @Inject
    lateinit var trendListAdapter: TrendingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trending_list_screen)

        trendingListViewModel =
            ViewModelProviders.of(this, vmFactory)[TrendingListViewModel::class.java]

        setupTrendsResultsView()

        setupOnClickListener()

        observeViewModel()

        fetchList(false)

    }

    private fun fetchList(forceUpdate: Boolean) {
        trendingListViewModel.getTrendingList(forceUpdate)
    }

    private fun setupOnClickListener() {

        ivMenuIcon.setOnClickListener {
            if (layoutMenu.visibility == View.VISIBLE) {
                hideMenu()
            } else {
                showMenu()
            }
        }

        vMenuDismiss.setOnClickListener {
            hideMenu()
        }

        tvSortName.setOnClickListener {
            trendingListViewModel.sortByName()
            hideMenu()
        }

        tvSortStar.setOnClickListener {
            trendingListViewModel.sortByStar()
            hideMenu()
        }

        tvRetry.setOnClickListener {
            fetchList(false)
        }

        swipeContainer.setOnRefreshListener {
            fetchList(true)
            swipeContainer.isRefreshing = false
        }

    }

    private fun observeViewModel() {

        trendingListViewModel.screenState.observe(
            this,
            Observer { screenState: ScreenState -> checkScreenState(screenState) })

        trendingListViewModel.trendingList.observe(
            this,
            Observer { list: List<TrendingItem> ->
                trendListAdapter.items = list
                rvTrendingItems.smoothScrollToPosition(0)
            })

    }

    private fun checkScreenState(screenState: ScreenState) {
        when (screenState) {
            ScreenState.APPLOADING -> {
                ivMenuIcon.visibility = View.INVISIBLE
                layoutLoading.visibility = View.VISIBLE
                layoutList.visibility = View.GONE
                layoutError.visibility = View.GONE
            }
            ScreenState.SUCCESS -> {
                layoutLoading.visibility = View.GONE
                layoutList.visibility = View.VISIBLE
                ivMenuIcon.visibility = View.VISIBLE
            }
            is ScreenState.Error -> {
                tvErrorMsg.setText(screenState.errorMsg)
                ivMenuIcon.visibility = View.INVISIBLE
                layoutLoading.visibility = View.GONE
                layoutError.visibility = View.VISIBLE
            }
        }
    }

    private fun setupTrendsResultsView() {
        rvTrendingItems?.layoutManager = LinearLayoutManager(this)
        rvTrendingItems?.adapter = trendListAdapter
    }

    private fun hideMenu() {
        layoutMenu.visibility = View.GONE
        vMenuDismiss.visibility = View.GONE
    }

    private fun showMenu() {
        layoutMenu.visibility = View.VISIBLE
        vMenuDismiss.visibility = View.VISIBLE
    }

}
