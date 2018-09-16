package com.impraise.suprdemo.scenes.data

import com.impraise.supr.data.ResultList
import com.impraise.suprdemo.scenes.data.model.Member
import io.reactivex.Flowable

/**
 * Created by guilhermebranco on 3/13/18.
 */
class InMemoryMemberRepository: MemberRepository {

    override fun all(): Flowable<ResultList<Member>> {
        val members = mutableListOf<Member>()
        for (index in 1..40) {
            members.add(Member("$index Test teteteteteteteteteteete", index.toString()))
        }
        return Flowable.just(ResultList.Success(members))
    }
}