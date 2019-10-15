package com.datasource.retrofitsource

import com.datasource.retrofitsource.model.toEntity
import com.entity.AppError
import com.entity.AppResult
import com.entity.TrendingItem
import com.repository.RetrofitSource
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RetrofitDataSource @Inject constructor(val api: TrendingListApi) : RetrofitSource {

    override fun trendingItemsList(): Observable<AppResult<List<TrendingItem>>> {
        return api.getTrendingList()
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful && it.code() == 200) {
                    val result = it.body()
                    if (result != null) {
                        AppResult.OnSuccess(result.map { it.toEntity() })
                    } else {
                        AppResult.OnError(AppError(message = "Data not present"))
                    }
                } else {
                    AppResult.OnError(AppError(code = it.code(), message = it.message()))
                }
            }
            .onErrorReturn { AppResult.OnError(AppError(cause = it)) }
    }
}