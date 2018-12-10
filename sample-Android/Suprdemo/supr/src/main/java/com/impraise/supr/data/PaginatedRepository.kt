package com.impraise.supr.data

import com.impraise.supr.common.Pagination
import io.reactivex.Flowable

interface PaginatedRepository<ResultType> : Repository<Pagination, PaginatedResult<ResultType>> {

    override fun fetch(pagination: Pagination): Flowable<PaginatedResult<ResultType>>
}