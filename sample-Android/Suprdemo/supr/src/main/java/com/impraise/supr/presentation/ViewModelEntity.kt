package com.impraise.common.presentation

/**
 * Created by guilhermebranco on 1/7/18.
 */
class ViewModelEntity<out DataType, out ErrorType>(val state: ViewModelEntityState, val data: DataType, val error: ErrorType?)