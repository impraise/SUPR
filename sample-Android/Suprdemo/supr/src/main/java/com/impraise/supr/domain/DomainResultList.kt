package com.impraise.supr.domain

/**
 * Created by guilhermebranco on 1/16/18.
 */
class DomainResultList<out T> private constructor(state: DomainResultState, data: List<T>?, error: Throwable?): DomainResult<List<T>>(state, data, error) {

    companion object {

        fun <T> success(data: List<T>): DomainResultList<T> {
            return DomainResultList(DomainResultState.Success, data, null)
        }

        fun <T> error(error: Throwable, data: List<T>?): DomainResultList<T> {
            return DomainResultList(DomainResultState.Error, data, error)
        }
    }
}
