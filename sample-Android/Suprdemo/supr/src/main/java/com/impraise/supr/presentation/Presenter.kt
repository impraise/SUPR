package com.impraise.supr.presentation

import android.arch.lifecycle.LiveData

/**
 * Created by guilhermebranco on 1/18/18.
 */
interface Presenter<in ParamType, ResponseType> {

    val viewModelStream: LiveData<ResponseType>

    fun present(param: ParamType)

    fun loading()
}