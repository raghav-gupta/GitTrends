package com.datasource.roomsource.di

import android.content.Context
import androidx.room.Room
import com.datasource.roomsource.db.ListDao
import com.datasource.roomsource.db.ListDatabase
import dagger.Module
import dagger.Provides


@Module
class RoomModule {

    @Provides
    @RoomScope
    fun listDao(database: ListDatabase): ListDao {
        return database.getListDao()
    }

    @Provides
    @RoomScope
    fun provideListDataBase(context: Context): ListDatabase {
        return Room.databaseBuilder(
            context,
            ListDatabase::class.java,
            ListDatabase.DBNAME
        ).build()
    }
}