package com.impraise.supr.data

sealed class Result<out T> {

    class Success<out T>(val data: T) : Result<T>()
    class Error<out T>(val error: Throwable, val data: T? = null) : Result<T>()
}

sealed class PaginatedResult<out T> {

    class Success<out T>(val data: List<T>, val pageDetail: PageDetail) : PaginatedResult<T>()
    class Error<out T>(val error: Throwable, val data: T? = null) : PaginatedResult<T>()
}

data class PageDetail(val hasNextPage: Boolean = false,
                        val totalCount: Int = 0,
                        val pageNumber: Int = 0)