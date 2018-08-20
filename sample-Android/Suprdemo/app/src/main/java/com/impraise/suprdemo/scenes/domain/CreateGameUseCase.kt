package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.data.Result
import com.impraise.supr.data.ResultList
import com.impraise.supr.domain.*
import com.impraise.suprdemo.scenes.domain.model.Game
import com.impraise.suprdemo.scenes.domain.model.Round
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by guilhermebranco on 3/11/18.
 */
class CreateGameUseCase(private val membersPaginatedUseCase: MembersPaginatedUseCase,
                        private val createRoundUseCase: CreateRoundUseCase): ReactiveUseCase<Unit, Result<Game>> {

    override fun get(param: Unit): Single<Result<Game>> {
        return membersPaginatedUseCase.get(Unit)
                .toFlowable()
                .flatMap { result ->
                    when (result) {
                        is ResultList.Success -> {
                            Flowable.fromIterable(result.data)
                        }

                        is ResultList.Error -> {
                            Flowable.fromIterable(emptyList())
                        }
                    }

                }
                .flatMap {
                    createRoundUseCase.get(it).toFlowable()
                }
                /*.flatMap {
                    when (it) {
                        is ResultList.Success -> {
                            Flowable.fromIterable(it.data)
                        }
                        is ResultList.Error -> Flowable.fromIterable(emptyList())
                    }
                }*/

                .map {
                    when (it) {
                        is Result.Success -> {
                            it.data
                        }
                        else -> Round.INVALID_ROUND
                    }
                }
                .toList()
                .map {
                    Result.Success(Game(it))
                }
    }

    /*override fun doYourJob() {
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
    }*/
}