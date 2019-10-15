package com.gittrends.ui.trendinglist

import androidx.lifecycle.MutableLiveData
import com.entity.AppError
import com.entity.AppResult
import com.entity.TrendingItem
import com.gittrends.core.BaseViewModel
import com.gittrends.core.di.viewmodel.MainThreadScheduler
import com.interactors.GetTrendingItems
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class TrendingListViewModel
@Inject constructor(
    val getTrendingItems: GetTrendingItems,
    @MainThreadScheduler val observerScheduler: Scheduler
) :
    BaseViewModel() {

    private lateinit var disposable: Disposable

    val screenState: MutableLiveData<ScreenState> by lazy {
        MutableLiveData<ScreenState>()
    }

    val trendingList: MutableLiveData<List<TrendingItem>> by lazy { MutableLiveData<List<TrendingItem>>() }

    fun getTrendingList(forceUpdate: Boolean) {

        screenState.value = ScreenState.APPLOADING

        disposable = getTrendingItems(forceUpdate)
            .observeOn(observerScheduler)
            .subscribe {
                when (it) {
                    is AppResult.OnSuccess -> handleSuccess(it.data)
                    is AppResult.OnError -> handleError(it.error)
                }
            }
        add(disposable)
    }

    private fun handleError(error: AppError) {
        screenState.value = ScreenState.Error(
            errorMsg = error.message ?: "An alien is probably blocking your signal"
        )
    }

    private fun handleSuccess(data: List<TrendingItem>) {
        screenState.value = ScreenState.SUCCESS
        trendingList.value = data
    }

    fun sortByStar() {
        screenState.value = ScreenState.APPLOADING
        val data = trendingList.value?.onEach { it.collapse = true }
        trendingList.value = data?.sortedBy { it.stars }
        screenState.value = ScreenState.SUCCESS
    }

    fun sortByName() {
        screenState.value = ScreenState.APPLOADING
        val data = trendingList.value?.onEach { it.collapse = true }
        trendingList.value = data?.sortedBy { it.author }
        screenState.value = ScreenState.SUCCESS
    }
}