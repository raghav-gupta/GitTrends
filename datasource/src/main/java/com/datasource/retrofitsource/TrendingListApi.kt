package com.datasource.retrofitsource

import com.datasource.retrofitsource.model.TrendingNetworkItem
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

interface TrendingListApi {

    @GET("/repositories")
    fun getTrendingList(): Observable<Response<List<TrendingNetworkItem>>>
}
