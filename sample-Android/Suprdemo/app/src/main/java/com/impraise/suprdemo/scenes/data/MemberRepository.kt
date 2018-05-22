package com.impraise.suprdemo.scenes.data

import com.impraise.supr.data.DataResultList
import com.impraise.suprdemo.scenes.data.model.Member
import io.reactivex.Observable

/**
 * Created by guilhermebranco on 3/10/18.
 */
interface MemberRepository {

    fun all(): Observable<DataResultList<Member>>
}