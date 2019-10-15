package com.datasource.roomsource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.entity.TrendingItem

@Entity(tableName = "list_items")
data class TrendingRoomItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "repo") val repo: String,
    @ColumnInfo(name = "authorUrl") val authorUrl: String,
    @ColumnInfo(name = "repoDetail") val repoDetail: String,
    @ColumnInfo(name = "repoUrl") val repoUrl: String,
    @ColumnInfo(name = "language") val language: String?,
    @ColumnInfo(name = "stars") val stars: Int,
    @ColumnInfo(name = "forks") val forks: Int
) {
    constructor(item: TrendingItem) : this(
        author = item.author,
        repo = item.repo,
        authorUrl = item.authorUrl,
        repoDetail = item.repoDetail,
        repoUrl = item.repoUrl,
        language = item.language,
        stars = item.stars,
        forks = item.forks
    )
}

fun TrendingRoomItem.toEntity(): TrendingItem {
    return TrendingItem(
        author = this.author,
        repo = this.repo,
        authorUrl = this.authorUrl,
        repoDetail = this.repoDetail,
        repoUrl = this.repoUrl,
        language = this.language,
        stars = this.stars,
        forks = this.forks,
        collapse = true
    )
}

