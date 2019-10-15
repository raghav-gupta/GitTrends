package com.gittrends.ui.trendinglist

import android.view.LayoutInflater
import dagger.Module
import dagger.Provides

@Module
class TrendingListModule {

    @Provides
    @TrendingListScope
    fun provideInflator(trendingListScreen: TrendingListScreen): LayoutInflater {
        return trendingListScreen.layoutInflater
    }
}

