package com.interactors

import com.entity.AppError
import com.entity.AppError.Companion.GENERAL_ERROR
import com.entity.AppResult
import com.entity.TrendingItem
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.repository.TrendingListRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import okio.Buffer
import okio.BufferedSource
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock


class GetTrendingItemsTest {

    @Mock
    lateinit var getTrendingItems: GetTrendingItems
    lateinit var trendingListRepository: TrendingListRepository

    @Before
    fun setUp() {
        trendingListRepository = mock()
        getTrendingItems = GetTrendingItems(trendingListRepository)
    }


    @Test
    fun testSuccess() {
        val forceUpdate = true
        whenever(trendingListRepository.getTrendingList(forceUpdate)).thenReturn(
            Observable.just(
                createDummyResponse()
            )
        )
        val observer = TestObserver<AppResult<List<TrendingItem>>>()
        getTrendingItems.invoke(forceUpdate).subscribe(observer)
        observer.assertNoErrors()
        Assert.assertTrue(observer.values().isNotEmpty())
        Assert.assertEquals(1, observer.valueCount())
        val response = observer.values().first()
        Assert.assertTrue(response is AppResult.OnSuccess)
        Assert.assertEquals(25, (response as AppResult.OnSuccess).data.size)
    }

    @Test
    fun testFailure() {
        val forceUpdate = true
        whenever(trendingListRepository.getTrendingList(forceUpdate)).thenReturn(
            Observable.just(
                createErrorResponse()
            )
        )
        val observer = TestObserver<AppResult<List<TrendingItem>>>()
        getTrendingItems.invoke(forceUpdate).subscribe(observer)
        observer.assertNoErrors()
        Assert.assertTrue(observer.values().isNotEmpty())
        Assert.assertEquals(1, observer.valueCount())
        val response = observer.values().first()
        Assert.assertTrue(response is AppResult.OnError)
        Assert.assertEquals(GENERAL_ERROR, (response as AppResult.OnError).error.code)
    }

    private fun createErrorResponse(): AppResult<List<TrendingItem>>? {
        return AppResult.OnError(AppError())
    }

    private fun createDummyResponse(): AppResult<List<TrendingItem>>? {
        return AppResult.OnSuccess(getMockData())
    }

    private fun getMockData(): List<TrendingItem> {
        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(List::class.java, TrendingItem::class.java)
        val adapter = moshi.adapter<List<TrendingItem>>(listMyData)
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

}