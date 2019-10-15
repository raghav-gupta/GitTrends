package com.repository

import com.entity.AppResult
import com.entity.TrendingItem
import io.reactivex.Observable

interface TrendingListRepository {
    fun getTrendingList(forceUpdate: Boolean): Observable<AppResult<List<TrendingItem>>>
}
