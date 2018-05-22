package com.impraise.common.presentation

import android.arch.lifecycle.LiveData

/**
 * Created by guilhermebranco on 1/18/18.
 */
interface Presenter<in ParamType, ResponseType> {

    val viewModelStream: LiveData<ViewModelEntity<ResponseType, String>>

    fun present(state: ViewModelEntityState, param: ParamType)
}