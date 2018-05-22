package com.impraise.suprdemo.scenes.data

import com.impraise.supr.data.DataResultList
import com.impraise.suprdemo.scenes.data.model.Member
import io.reactivex.Observable

/**
 * Created by guilhermebranco on 3/13/18.
 */
class InMemoryMemberRepository: MemberRepository {

    override fun all(): Observable<DataResultList<Member>> {
        val members = mutableListOf<Member>()
        for (index in 1..40) {
            members.add(Member(index.toString(), index.toString()))
        }
        return Observable.just(DataResultList.success(members))
    }
}