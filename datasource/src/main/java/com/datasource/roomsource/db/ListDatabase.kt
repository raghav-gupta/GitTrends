package com.datasource.roomsource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.datasource.roomsource.model.TrendingRoomItem

@Database(entities = arrayOf(TrendingRoomItem::class), version = 1)
abstract class ListDatabase : RoomDatabase() {
    companion object {
        const val DBNAME = "list_db"

    }

    abstract fun getListDao(): ListDao
}