package com.impraise.supr.data.util

import com.impraise.supr.data.Store
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import java.util.*

/**
 * Created by guilhermebranco on 1/21/18.
 */
class InMemoryStore<in Id, Object>(private val idExtractor: Function<Object, Id>): Store<Id, Object> {

    private val cache = HashMap<Id, Object>()

    override fun save(data: Object): Single<Object> {
        return Single.just(data)
                .flatMap {
                    val id = idExtractor.apply(data)
                    cache[id] = data
                    Single.just(data)
                }

    }

    override fun saveAll(data: List<Object>): Single<List<Object>> {
        return Observable.fromIterable(data)
                .flatMap {
                    save(it).toObservable()
                }.toList()
    }

    override fun getAll(): Single<List<Object>> {
        return Single.just(cache.values.toList())
    }

    override fun get(id: Id): Maybe<Object> {
        return if (cache.containsKey(id)) Maybe.just(cache[id])
        else Maybe.empty()
    }
}