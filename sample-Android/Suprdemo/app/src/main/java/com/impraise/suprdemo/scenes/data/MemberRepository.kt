package com.impraise.suprdemo.scenes.data

import com.impraise.supr.common.Pagination
import com.impraise.supr.data.PageDetail
import com.impraise.supr.data.PaginatedRepository
import com.impraise.supr.data.PaginatedResult
import com.impraise.suprdemo.scenes.data.model.Character
import com.impraise.suprdemo.scenes.data.model.CharacterDataWrapper
import com.impraise.suprdemo.scenes.data.model.Member
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by guilhermebranco on 3/10/18.
 */
class MarvelApiRepository(okHttpClient: OkHttpClient) : PaginatedRepository<Member> {

    private val service: Service

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://gateway.marvel.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

        service = retrofit.create(Service::class.java)
    }

    override fun fetch(pagination: Pagination): Flowable<PaginatedResult<Member>> {
        return service.load(pagination.size, pagination.after?.toInt() ?: 0)
                .map {
                    val results = it.data.results.map { it.toMember() }
                    PaginatedResult.Success(results, PageDetail(hasNextPage = it.data.count == 0, totalCount = it.data.total, pageNumber = pagination.pageNumber)) as PaginatedResult<Member>
                }
                .onErrorReturn { PaginatedResult.Error(it) }
                .toFlowable(BackpressureStrategy.DROP)
    }

    private fun Character.toMember(): Member {
        val image = this.thumbnail?.let {
            "${it.path}.${it.extension}"
        } ?: ""
        return Member(this.name, image)
    }

    internal interface Service {

        @GET("/v1/public/characters")
        fun load(@Query("limit") limit: Int, @Query("offset") offset: Int): Observable<CharacterDataWrapper>
    }
}