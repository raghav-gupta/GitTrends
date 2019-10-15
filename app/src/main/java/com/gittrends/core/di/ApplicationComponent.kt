package com.gittrends.core.di

import com.datasource.retrofitsource.di.RetrofitModule
import com.datasource.retrofitsource.di.RetrofitScope
import com.datasource.roomsource.di.RoomModule
import com.datasource.roomsource.di.RoomScope
import com.gittrends.core.App
import com.gittrends.core.di.viewmodel.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector


@AppScope
@RetrofitScope
@RoomScope
@Component(modules = [ApplicationModule::class, ViewModelModule::class, AndroidInjectionModule::class, BuilderModule::class, RetrofitModule::class, RoomModule::class])
interface ApplicationComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
