package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.data.Result
import com.impraise.supr.data.ResultList
import com.impraise.supr.domain.*
import com.impraise.suprdemo.scenes.data.model.Member
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
                .map {
                    it.filterGroupsWithoutAvatar()
                }
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

    private fun ResultList<List<Member>>.filterGroupsWithoutAvatar(): ResultList<List<Member>> {
        return when (this) {
            is ResultList.Success -> {
                ResultList.Success(GameCreationHelper().filterGroupsWithoutAvatar(this.data))
            }
            is ResultList.Error -> this
        }
    }
}

class GameCreationHelper {

    fun filterGroupsWithoutAvatar(groups: List<List<Member>>): List<List<Member>> {
        return groups.filter {
            it.firstOrNull { !it.avatarUrl.isEmpty() } != null
        }
    }
}