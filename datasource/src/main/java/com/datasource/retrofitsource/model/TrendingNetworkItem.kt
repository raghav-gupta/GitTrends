package com.datasource.retrofitsource.model

import com.entity.TrendingItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrendingNetworkItem(
    val author: String,
    @Json(name = "name") val repo: String,
    @Json(name = "avatar") val authorUrl: String,
    @Json(name = "description") val repoDetail: String,
    @Json(name = "url") val repoUrl: String,
    val language: String?,
    val stars: Int,
    val forks: Int
)

fun TrendingNetworkItem.toEntity(): TrendingItem {
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
