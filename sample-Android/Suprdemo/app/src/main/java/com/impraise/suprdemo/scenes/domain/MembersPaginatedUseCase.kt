package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.domain.DomainResultList
import com.impraise.supr.domain.ReactiveUseCase
import com.impraise.suprdemo.scenes.data.model.Member
import com.impraise.suprdemo.scenes.data.MemberRepository
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

/**
 * Created by guilhermebranco on 3/10/18.
 */
class MembersPaginatedUseCase(
        private val repository: MemberRepository,
        private val threshold: Int = 5): ReactiveUseCase<Unit, DomainResultList<List<Member>>> {

    override fun get(param: Unit): Single<DomainResultList<List<Member>>> {
        return repository
                .all()
                .flatMap {
                    val members = it.data?.toMutableList()
                    Collections.shuffle(members)
                    Observable.just(members)
                }
                .flatMap {
                    Observable.fromIterable(it)
                }
                .buffer(threshold)
                .toList()
                .map {
                    DomainResultList.success(it)
                }
    }
}