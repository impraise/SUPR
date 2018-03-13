package com.impraise.supr.domain.util

import com.impraise.supr.data.DataResult
import com.impraise.supr.data.DataResultList
import com.impraise.supr.data.DataResultState
import com.impraise.supr.domain.DomainLayerException
import com.impraise.supr.domain.DomainResult
import com.impraise.supr.domain.DomainResultList

/**
 * Created by guilhermebranco on 1/22/18.
 */
fun <T> DataResultList<T>.toDomainResult(): DomainResultList<T> {
    return when(this.state) {
        DataResultState.Error -> DomainResultList.error(DomainLayerException(), this.data)
        DataResultState.Success -> DomainResultList.success(this.data ?: emptyList())
    }
}

fun <T> DataResult<T>.toDomainResult(): DomainResult<T> {
    return when(this.state) {
        DataResultState.Error -> DomainResult.error(this.error.defaultIfNull(), this.data)
        DataResultState.Success -> {
            if (this.data == null) DomainResult.error(DomainLayerException(), null)
            else DomainResult.success(this.data)
        }
    }
}

fun Throwable?.defaultIfNull(): Throwable {
    return this ?: DomainLayerException()
}

fun Throwable?.errorMessage(): Int {
    return (this as? DomainLayerException)?.errorMessageId ?: DomainLayerException.DEFAULT_ERROR_MESSAGE_ID
}