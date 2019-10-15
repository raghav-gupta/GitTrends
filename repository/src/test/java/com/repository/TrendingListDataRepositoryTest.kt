package com.repository

import com.entity.AppError
import com.entity.AppResult
import com.entity.TrendingItem
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
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


class TrendingListDataRepositoryTest {

    lateinit var trendingListDataRepository: TrendingListDataRepository
    @Mock
    lateinit var roomSource: RoomSource
    @Mock
    lateinit var retrofitSource: RetrofitSource
    @Mock
    lateinit var cacheSource: CacheSource


    @Before
    fun setUp() {
        retrofitSource = mock()
        roomSource = mock()
        cacheSource = mock()
        trendingListDataRepository =
            TrendingListDataRepository(retrofitSource, roomSource, cacheSource)
    }


    @Test
    fun testFetchFromRetrofit() {
        val forceUpdate = true

        whenever(retrofitSource.trendingItemsList()).thenReturn(
            Observable.just(
                createDummyResponse()
            )
        )

        whenever(roomSource.trendingItemsList()).thenReturn(
            Observable.just(
                createErrorResponse()
            )
        )

        whenever(cacheSource.isExpired()).thenReturn(true)


        val observer = TestObserver<AppResult<List<TrendingItem>>>()
        trendingListDataRepository.getTrendingList(forceUpdate).subscribe(observer)
        observer.assertNoErrors()
        Assert.assertTrue(observer.values().isNotEmpty())
        Assert.assertEquals(1, observer.valueCount())
        val response = observer.values().first()
        Assert.assertTrue(response is AppResult.OnSuccess)
        Assert.assertEquals(25, (response as AppResult.OnSuccess).data.size)
    }

    @Test
    fun testFetchFromDB() {
        val forceUpdate = false

        whenever(retrofitSource.trendingItemsList()).thenReturn(
            Observable.just(
                createErrorResponse()
            )
        )

        whenever(roomSource.trendingItemsList()).thenReturn(
            Observable.just(
                createDummyResponse()
            )
        )

        whenever(cacheSource.isExpired()).thenReturn(false)

        val observer = TestObserver<AppResult<List<TrendingItem>>>()
        trendingListDataRepository.getTrendingList(forceUpdate).subscribe(observer)
        observer.assertNoErrors()
        Assert.assertTrue(observer.values().isNotEmpty())
        Assert.assertEquals(1, observer.valueCount())
        val response = observer.values().first()
        Assert.assertTrue(response is AppResult.OnSuccess)
        Assert.assertEquals(25, (response as AppResult.OnSuccess).data.size)
    }


    @Test
    fun testCacheExpired() {
        val forceUpdate = false

        whenever(cacheSource.isExpired()).thenReturn(true)

        whenever(retrofitSource.trendingItemsList()).thenReturn(
            Observable.just(
                createDummyResponse()
            )
        )

        whenever(roomSource.trendingItemsList()).thenReturn(
            Observable.just(
                createErrorResponse()
            )
        )

        val observer = TestObserver<AppResult<List<TrendingItem>>>()
        trendingListDataRepository.getTrendingList(forceUpdate).subscribe(observer)
        observer.assertNoErrors()
        Assert.assertTrue(observer.values().isNotEmpty())
        Assert.assertEquals(1, observer.valueCount())
        val response = observer.values().first()
        Assert.assertTrue(response is AppResult.OnSuccess)
        Assert.assertEquals(25, (response as AppResult.OnSuccess).data.size)
    }

    @Test
    fun testCacheNotExpired() {
        val forceUpdate = false

        whenever(cacheSource.isExpired()).thenReturn(false)

        whenever(retrofitSource.trendingItemsList()).thenReturn(
            Observable.just(
                createErrorResponse()
            )
        )

        whenever(roomSource.trendingItemsList()).thenReturn(
            Observable.just(
                createDummyResponse()
            )
        )

        val observer = TestObserver<AppResult<List<TrendingItem>>>()
        trendingListDataRepository.getTrendingList(forceUpdate).subscribe(observer)
        observer.assertNoErrors()
        Assert.assertTrue(observer.values().isNotEmpty())
        Assert.assertEquals(1, observer.valueCount())
        val response = observer.values().first()
        Assert.assertTrue(response is AppResult.OnSuccess)
        Assert.assertEquals(25, (response as AppResult.OnSuccess).data.size)
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