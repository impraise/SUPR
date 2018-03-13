package com.impraise.supr.data

/**
 * Created by guilhermebranco on 1/16/18.
 */
class DataResultList<out T> private constructor(state: DataResultState, data: List<T>?, error: Throwable?): DataResult<List<T>>(state, data, error) {

    companion object {

        fun <T> success(data: List<T>): DataResultList<T> {
            return DataResultList(DataResultState.Success, data, null)
        }

        fun <T> error(error: Throwable, data: List<T>?): DataResultList<T> {
            return DataResultList(DataResultState.Error, data, error)
        }
    }
}