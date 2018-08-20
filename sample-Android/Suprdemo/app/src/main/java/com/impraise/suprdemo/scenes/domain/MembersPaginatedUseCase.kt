package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.data.ResultList
import com.impraise.supr.domain.ReactiveUseCase
import com.impraise.suprdemo.scenes.data.model.Member
import com.impraise.suprdemo.scenes.data.MemberRepository
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

/**
 * Created by guilhermebranco on 3/10/18.
 */
class MembersPaginatedUseCase(
        private val repository: MemberRepository,
        private val threshold: Int = 5): ReactiveUseCase<Unit, ResultList<List<Member>>> {

    override fun get(param: Unit): Single<ResultList<List<Member>>> {
        return repository
                .all()
                .flatMap { result ->
                    when (result) {
                        is ResultList.Success -> {
                            val members = result.data.toMutableList()
                            Collections.shuffle(members)
                            Flowable.fromIterable(members)
                        }
                        is ResultList.Error -> Flowable.fromIterable(emptyList())
                    }
                }
                .buffer(threshold)
                .toList()
                .map {
                    ResultList.Success(it)
                }
    }
}