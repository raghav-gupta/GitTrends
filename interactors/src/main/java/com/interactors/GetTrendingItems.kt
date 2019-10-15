package com.interactors

import com.entity.AppResult
import com.entity.TrendingItem
import com.repository.TrendingListRepository
import io.reactivex.Observable

class GetTrendingItems(private val trendingListRepository: TrendingListRepository) {

    operator fun invoke(forceUpdate: Boolean): Observable<AppResult<List<TrendingItem>>> {
        return trendingListRepository.getTrendingList(forceUpdate)
    }
}
