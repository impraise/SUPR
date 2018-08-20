package com.impraise.supr.data

sealed class Result<out T> {

    class Success<out T>(val data: T) : Result<T>()
    class Error<out T>(val error: Throwable, val data: T? = null) : Result<T>()
}