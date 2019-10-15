package com.gittrends.ui.trendinglist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.datasource.retrofitsource.model.TrendingNetworkItem
import com.datasource.retrofitsource.model.toEntity
import com.entity.AppError
import com.entity.AppResult
import com.entity.TrendingItem
import com.interactors.GetTrendingItems
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okio.Buffer
import okio.BufferedSource
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock


@RunWith(JUnit4::class)
class TrendingListViewModelTest {

    private val ERROR_MESSAGE: String = "Error in response"

    @Mock
    private lateinit var resultObserver: Observer<List<TrendingItem>>


    @Mock
    private lateinit var stateObserver: Observer<ScreenState>

    @Mock
    private lateinit var getTrendingItems: GetTrendingItems

    private lateinit var trendingListViewModel: TrendingListViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        resultObserver = mock()
        stateObserver = mock()
        getTrendingItems = mock()
        trendingListViewModel = TrendingListViewModel(getTrendingItems, Schedulers.trampoline())
    }

    @Test
    fun testGetTrendingListFirstInstall() {
        val forceUpdate = true

        trendingListViewModel.trendingList.observeForever(resultObserver)
        trendingListViewModel.screenState.observeForever(stateObserver)


        whenever(getTrendingItems.invoke(forceUpdate)).thenReturn(
            Observable.just(
                createDummyResponse()
            )
        )
        trendingListViewModel.getTrendingList(forceUpdate)

        verify(resultObserver).onChanged(getMockData().map { it.toEntity() })
        verify(stateObserver).onChanged(ScreenState.APPLOADING)
        verify(stateObserver).onChanged(ScreenState.SUCCESS)
    }

    @Test
    fun testGetTrendingListNoForceUpdate() {
        val forceUpdate = false

        trendingListViewModel.trendingList.observeForever(resultObserver)
        trendingListViewModel.screenState.observeForever(stateObserver)


        whenever(getTrendingItems.invoke(forceUpdate)).thenReturn(
            Observable.just(
                createDummyResponse()
            )
        )
        trendingListViewModel.getTrendingList(forceUpdate)

        verify(resultObserver).onChanged(getMockData().map { it.toEntity() })
        verify(stateObserver).onChanged(ScreenState.APPLOADING)
        verify(stateObserver).onChanged(ScreenState.SUCCESS)
    }

    @Test
    fun testError() {
        val forceUpdate = true

        trendingListViewModel.screenState.observeForever(stateObserver)


        whenever(getTrendingItems.invoke(forceUpdate)).thenReturn(
            Observable.just(
                createErrorResponse()
            )
        )
        trendingListViewModel.getTrendingList(forceUpdate)

        verify(stateObserver).onChanged(ScreenState.APPLOADING)

        verify(stateObserver).onChanged(ScreenState.Error(ERROR_MESSAGE))
    }


    private fun createDummyResponse(): AppResult<List<TrendingItem>>? {
        return AppResult.OnSuccess(getMockData().map { it.toEntity() })
    }

    private fun getMockData(): List<TrendingNetworkItem> {
        val moshi = Moshi.Builder().build()
        val listMyData =
            Types.newParameterizedType(List::class.java, TrendingNetworkItem::class.java)
        val adapter = moshi.adapter<List<TrendingNetworkItem>>(listMyData)
        return adapter.fromJson(
            resourceFileToStream(
                moshi.javaClass.classLoader!!,
                "valid_results.json"
            )
        )!!
    }

    private fun resourceFileToStream(classLoader: ClassLoader, fileName: String): BufferedSource {
        return Buffer().readFrom(classLoader.getResourceAsStream(fileName))
    }

    private fun createErrorResponse(): AppResult<List<TrendingItem>>? {
        return AppResult.OnError(AppError(message = ERROR_MESSAGE))
    }

}
