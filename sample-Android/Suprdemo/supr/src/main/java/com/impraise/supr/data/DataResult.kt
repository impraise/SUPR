package com.impraise.supr.data

/**
 * Created by guilhermebranco on 1/5/18.
 */
open class DataResult<out T> protected constructor(val state: DataResultState, val data: T?, val error: Throwable?) {

    companion object {

        fun <T> success(data: T): DataResult<T> {
            return DataResult(DataResultState.Success, data, null)
        }

        fun <T> error(error: Throwable, data: T?): DataResult<T> {
            return DataResult(DataResultState.Error, data, error)
        }
    }
}