package com.datasource.cachesource

import android.content.Context
import android.content.SharedPreferences
import com.repository.CacheSource
import javax.inject.Inject

class CacheDataSource @Inject constructor(context: Context) : CacheSource {

    companion object {
        private val PREF_PACKAGE_NAME = "com.gittrends"

        private val PREF_KEY_LAST_CACHE = "last_cache"

        private val EXPIRATION_TIME = (60 * 10 * 1000).toLong()

    }

    private val sharedPref: SharedPreferences

    init {
        sharedPref = context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    private fun getLastCacheTime(): Long {
        return sharedPref.getLong(PREF_KEY_LAST_CACHE, 0)
    }

    override fun setLastCacheTime(lastCache: Long) {
        sharedPref.edit().putLong(PREF_KEY_LAST_CACHE, lastCache).apply()
    }

    override fun isExpired(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastUpdateTime = getLastCacheTime()
        return currentTime - lastUpdateTime > EXPIRATION_TIME

    }
}