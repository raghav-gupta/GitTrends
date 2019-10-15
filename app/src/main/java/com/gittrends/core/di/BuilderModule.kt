package com.gittrends.core.di

import com.gittrends.ui.trendinglist.TrendingListModule
import com.gittrends.ui.trendinglist.TrendingListScope
import com.gittrends.ui.trendinglist.TrendingListScreen
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuilderModule {

    @TrendingListScope
    @ContributesAndroidInjector(modules = [TrendingListModule::class])
    abstract fun contributeTrendingList(): TrendingListScreen
}
