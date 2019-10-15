package com.gittrends.core.di

import android.content.Context
import com.datasource.cachesource.CacheDataSource
import com.datasource.retrofitsource.RetrofitDataSource
import com.datasource.roomsource.RoomDataSource
import com.gittrends.core.App
import com.gittrends.core.di.viewmodel.MainThreadScheduler
import com.interactors.GetTrendingItems
import com.repository.TrendingListDataRepository
import com.repository.TrendingListRepository
import com.squareup.picasso.Downloader
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.OkHttpClient


@Module
class ApplicationModule {

    @AppScope
    @Provides
    fun okHttp3Downloader(okHttpClient: OkHttpClient): Downloader {
        return OkHttp3Downloader(okHttpClient)
    }

    @Provides
    @AppScope
    fun picasso(app: App, okHttp3Downloader: Downloader): Picasso {
        return Picasso.Builder(app.applicationContext)
            .downloader(okHttp3Downloader)
            .loggingEnabled(true)
            .build()
    }

    @AppScope
    @Provides
    fun context(app: App): Context {
        return app
    }

    @AppScope
    @Provides
    fun provideGetTrendingItems(trendingListRepository: TrendingListRepository): GetTrendingItems {
        return GetTrendingItems(trendingListRepository)
    }

    @AppScope
    @Provides
    fun provideTrendingListRepository(
        retrofitSource: RetrofitDataSource,
        roomSource: RoomDataSource,
        cacheSource: CacheDataSource
    ): TrendingListRepository {
        return TrendingListDataRepository(retrofitSource, roomSource, cacheSource)
    }

    @MainThreadScheduler
    @Provides
    fun mainThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
