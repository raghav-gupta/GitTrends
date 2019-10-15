package com.datasource.roomsource

import com.datasource.roomsource.db.ListDao
import com.datasource.roomsource.model.TrendingRoomItem
import com.datasource.roomsource.model.toEntity
import com.entity.AppError
import com.entity.AppResult
import com.entity.TrendingItem
import com.repository.RoomSource
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable
import javax.inject.Inject

class RoomDataSource @Inject constructor(val listDao: ListDao) : RoomSource {
    override fun trendingItemsList(): Observable<AppResult<List<TrendingItem>>> {
        return Observable.fromCallable(Callable { listDao.getList() }).subscribeOn(Schedulers.io()).map {
            val items = it
            if (!items.isEmpty()) {
                AppResult.OnSuccess(items.map { it.toEntity() })
            } else {
                AppResult.OnError(AppError(message = "No Data found"))
            }
        }
    }

    override fun saveTrendingItemsList(list: List<TrendingItem>): Boolean {
        listDao.clear()
        val insertedSize = listDao.saveList(list.map {
            TrendingRoomItem(it)
        }).size
        return insertedSize == list.size
    }
}