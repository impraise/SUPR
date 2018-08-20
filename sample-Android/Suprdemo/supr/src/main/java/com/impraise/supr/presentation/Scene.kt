package com.impraise.supr.presentation

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by guilhermebranco on 3/12/18.
 */
abstract class Scene<in Interaction>: ViewModel() {

    protected val subscriptions = CompositeDisposable()

    abstract fun onInteraction(interaction: Interaction)

    override fun onCleared() {
        subscriptions.apply {
            if (isDisposed) return@apply
            dispose()
            clear()
        }
        super.onCleared()
    }
}