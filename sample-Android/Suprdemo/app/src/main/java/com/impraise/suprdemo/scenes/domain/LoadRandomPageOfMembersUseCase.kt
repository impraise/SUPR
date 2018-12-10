package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.common.Pagination
import com.impraise.supr.data.PaginatedRepository
import com.impraise.supr.data.PaginatedResult
import com.impraise.supr.data.ResultList
import com.impraise.supr.domain.ReactiveUseCase
import com.impraise.suprdemo.scenes.data.model.Member
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

/**
 * Created by guilhermebranco on 3/10/18.
 */
class LoadRandomPageOfMembersUseCase(
        private val repository: PaginatedRepository<Member>,
        private val threshold: Int = 5): ReactiveUseCase<Unit, ResultList<List<Member>>> {

    override fun get(param: Unit): Single<ResultList<List<Member>>> {
        return repository
                .fetch(Pagination(50, (0..1000).random().toString()))
                .flatMap { result ->
                    when (result) {
                        is PaginatedResult.Success -> {
                            val members = result.data.toMutableList()
                            members.shuffle()
                            Flowable.fromIterable(members)
                        }
                        is PaginatedResult.Error -> Flowable.fromIterable(emptyList())
                    }
                }
                .buffer(threshold)
                .toList()
                .map {
                    ResultList.Success(it)
                }
    }
}

fun IntRange.random() =
        Random().nextInt((endInclusive + 1) - start) +  start