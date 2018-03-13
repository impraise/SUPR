package com.impraise.supr.domain

import io.reactivex.Single

/**
 * Created by guilhermebranco on 12/30/17.
 */
interface ReactiveUseCase<in ParamType, ResponseType> {

    fun get(param: ParamType): Single<ResponseType>
}