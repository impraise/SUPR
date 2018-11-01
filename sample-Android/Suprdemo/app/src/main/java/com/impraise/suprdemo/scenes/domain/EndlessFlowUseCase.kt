package com.impraise.suprdemo.scenes.domain

import io.reactivex.Flowable

interface EndlessFlowUseCase<in ParamType, ResultType> {

    fun stream(param: ParamType): Flowable<ResultType>
}