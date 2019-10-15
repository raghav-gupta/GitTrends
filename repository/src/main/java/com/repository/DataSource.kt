package com.repository

import com.entity.AppResult
import com.entity.TrendingItem
import io.reactivex.Observable

interface RetrofitSource {
    fun trendingItemsList(): Observable<AppResult<List<TrendingItem>>>
}

interface RoomSource {
    fun saveTrendingItemsList(list: List<TrendingItem>): Boolean

    fun trendingItemsList(): Observable<AppResult<List<TrendingItem>>>
}

interface CacheSource {

    fun setLastCacheTime(lastCache: Long)

    fun isExpired(): Boolean

}