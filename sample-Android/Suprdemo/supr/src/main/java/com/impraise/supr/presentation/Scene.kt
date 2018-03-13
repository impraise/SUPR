package com.impraise.supr.presentation

import android.arch.lifecycle.ViewModel
import com.impraise.supr.domain.DisposableUseCase

/**
 * Created by guilhermebranco on 3/12/18.
 */
abstract class Scene<in Interaction>: ViewModel() {

    protected val useCases: MutableList<DisposableUseCase> = mutableListOf()

    abstract fun onInteraction(interaction: Interaction)

    override fun onCleared() {
        useCases.forEach {
            it.dispose()
        }
        useCases.clear()
        super.onCleared()
    }
}