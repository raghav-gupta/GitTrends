package com.gittrends.ui.trendinglist

sealed class ScreenState {
    object APPLOADING : ScreenState()
    object SUCCESS : ScreenState()
    data class Error(val errorMsg:String) : ScreenState()
}