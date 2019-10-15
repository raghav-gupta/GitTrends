package com.entity

data class TrendingItem(
    val author: String,
    val repo: String,
    val authorUrl: String,
    val repoDetail: String,
    val repoUrl: String,
    val language: String?,
    val stars: Int,
    val forks: Int,
    var collapse: Boolean = true
){
    val repoDetailWithUrl:String = "$repoDetail ($repoUrl)"
}
