package com.impraise.suprdemo.scenes.data

import com.impraise.supr.data.ResultList
import com.impraise.suprdemo.scenes.data.model.Member
import io.reactivex.Flowable

/**
 * Created by guilhermebranco on 3/10/18.
 */
interface MemberRepository {

    fun all(): Flowable<ResultList<Member>>
}