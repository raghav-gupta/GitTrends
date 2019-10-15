package com.datasource.roomsource

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.datasource.retrofitsource.model.TrendingNetworkItem
import com.datasource.retrofitsource.model.toEntity
import com.datasource.roomsource.db.ListDao
import com.datasource.roomsource.db.ListDatabase
import com.entity.TrendingItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okio.Buffer
import okio.BufferedSource
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class RoomDataSourceTest {
    private lateinit var lisDao: ListDao
    private lateinit var db: ListDatabase
    private lateinit var roomDataSource: RoomDataSource

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ListDatabase::class.java
        ).build()
        lisDao = db.getListDao()
        roomDataSource = RoomDataSource(lisDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testWriteToDb() {
        val itemList: List<TrendingItem> = getMockData().map { it.toEntity() }
        val result = roomDataSource.saveTrendingItemsList(itemList)
        Assert.assertEquals(true, result)
    }

    @Test
    @Throws(Exception::class)
    fun testReadFromDB() {
        val itemList: List<TrendingItem> = getMockData().map { it.toEntity() }
        roomDataSource.saveTrendingItemsList(itemList)
        val itemListFromDB = lisDao.getList()
        Assert.assertEquals(itemList.size, itemListFromDB.size)
    }

    private fun getMockData(): List<TrendingNetworkItem> {
        val moshi = Moshi.Builder().build()
        val listMyData =
            Types.newParameterizedType(List::class.java, TrendingNetworkItem::class.java)
        val adapter = moshi.adapter<List<TrendingNetworkItem>>(listMyData)
        return adapter.fromJson(
            resourceFileToStream(
                moshi.javaClass.classLoader!!,
                "valid_results.json"
            )
        )!!
    }

    private fun resourceFileToStream(classLoader: ClassLoader, fileName: String): BufferedSource {
        return Buffer().readFrom(classLoader.getResourceAsStream(fileName))
    }
}


