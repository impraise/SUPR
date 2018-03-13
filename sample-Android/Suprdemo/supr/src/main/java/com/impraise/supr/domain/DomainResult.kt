package com.impraise.supr.domain

/**
 * Created by guilhermebranco on 1/7/18.
 */
open class DomainResult<out T> protected constructor(val state: DomainResultState, val data: T?, val error: Throwable?) {

    companion object {

        fun <T> success(data: T): DomainResult<T> {
            return DomainResult(DomainResultState.Success, data, null)
        }

        fun <T> error(error: Throwable, data: T?): DomainResult<T> {
            return DomainResult(DomainResultState.Error, data, error)
        }
    }
}