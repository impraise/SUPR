package com.impraise.supr.domain

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by guilhermebranco on 1/16/18.
 */
abstract class UseCase<Object>: DisposableUseCase {

    protected val subscriptions = CompositeDisposable()

    var callback: ((result: Object) -> Unit)? = null

    override fun dispose() {
        if (!subscriptions.isDisposed) {
            subscriptions.dispose()
            subscriptions.clear()
        }
    }
}