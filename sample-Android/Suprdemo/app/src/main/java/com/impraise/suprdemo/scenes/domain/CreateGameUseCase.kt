package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.domain.DomainLayerException
import com.impraise.supr.domain.DomainResult
import com.impraise.supr.domain.DomainResultState
import com.impraise.supr.domain.SimpleUseCase
import com.impraise.suprdemo.R
import com.impraise.suprdemo.scenes.domain.model.Game
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo

/**
 * Created by guilhermebranco on 3/11/18.
 */
class CreateGameUseCase(private val membersPaginatedUseCase: MembersPaginatedUseCase,
                        private val createRoundUseCase: CreateRoundUseCase): SimpleUseCase<DomainResult<Game>>() {


    override fun doYourJob() {
        membersPaginatedUseCase.get(Unit)
                .toObservable()
                .flatMap { Observable.fromIterable(it.data) }
                .flatMap {
                    createRoundUseCase.get(it).toObservable()
                }
                .filter { it.state == DomainResultState.Success && it.data != null}
                .map { it.data!! }
                .toList()
                .subscribe({
                    complete(DomainResult.success(Game(it)))
                }, {
                    complete(DomainResult.error(DomainLayerException(R.string.domain_game_creation_error), null))
                }).addTo(subscriptions)
    }

    private fun complete(domainResult: DomainResult<Game>) {
        callback?.let {
            it(domainResult)
        }
    }
}