package com.datasource.roomsource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.datasource.roomsource.model.TrendingRoomItem

@Dao
interface ListDao {

    @Query("SELECT * FROM list_items")
    fun getList(): List<TrendingRoomItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveList(list: List<TrendingRoomItem>): List<Long>

    @Query("DELETE FROM list_items")
    fun clear()
}