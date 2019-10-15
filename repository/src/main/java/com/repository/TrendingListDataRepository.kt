package com.repository

import com.entity.AppResult
import com.entity.TrendingItem
import io.reactivex.Observable

class TrendingListDataRepository constructor(
    private val retrofitDataSource: RetrofitSource,
    private val roomDataSource: RoomSource,
    private val cacheSource: CacheSource
) : TrendingListRepository {

    override fun getTrendingList(forceUpdate: Boolean): Observable<AppResult<List<TrendingItem>>> {
        if (forceUpdate || isCacheExpired()) {
            return fetchFromRetrofit()
        } else {
            return fetchFromDB()
        }
    }

    private fun fetchFromRetrofit(): Observable<AppResult<List<TrendingItem>>> {
        return retrofitDataSource.trendingItemsList().doOnNext {
            when (it) {
                is AppResult.OnSuccess -> addToDB(it.data)
            }
        }
    }

    private fun lastUpdated(time: Long) {
        cacheSource.setLastCacheTime(time)
    }

    private fun addToDB(data: List<TrendingItem>) {
        if (roomDataSource.saveTrendingItemsList(data)) {
            lastUpdated(System.currentTimeMillis())
        }
    }

    private fun fetchFromDB(): Observable<AppResult<List<TrendingItem>>> {
        return roomDataSource.trendingItemsList()
    }

    private fun isCacheExpired(): Boolean {
        return cacheSource.isExpired()
    }

}

