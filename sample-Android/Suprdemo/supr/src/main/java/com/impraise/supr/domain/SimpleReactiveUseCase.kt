package com.impraise.supr.domain

import io.reactivex.Single

interface SimpleReactiveUseCase<ResponseType> {

    fun get(): Single<ResponseType>
}