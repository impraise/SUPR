package com.impraise.supr.data

import io.reactivex.Flowable

interface Repository<in ParamType, ResultType> {

    fun fetch(param: ParamType): Flowable<ResultType>
}