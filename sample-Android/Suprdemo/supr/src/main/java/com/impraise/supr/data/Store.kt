package com.impraise.supr.data

import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by guilhermebranco on 12/30/17.
 */
interface Store<in Id, Object> {

    fun save(data: Object): Single<Object>

    fun saveAll(data: List<Object>): Single<List<Object>>

    fun getAll(): Single<List<Object>>

    fun get(id: Id): Maybe<Object>
}