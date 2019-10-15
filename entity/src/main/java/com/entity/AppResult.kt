package com.entity

sealed class AppResult<out T> {
    data class OnSuccess<T>(val data: T) : AppResult<T>()
    data class OnError(val error: AppError) : AppResult<Nothing>()
}

data class AppError(
    val code: Int = GENERAL_ERROR,
    val message: String? = null,
    val cause: Throwable? = null
) {
    companion object {
        const val GENERAL_ERROR = 89
    }
}