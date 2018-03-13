package com.impraise.supr.domain

/**
 * Created by guilhermebranco on 1/18/18.
 */
abstract class ParameterizedUseCase<in ParamType, ResponseType>: UseCase<ResponseType>() {

    abstract fun doYourJob(param: ParamType)

}