package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.common.Pagination
import com.impraise.supr.data.PageDetail
import com.impraise.supr.data.PaginatedRepository
import com.impraise.supr.data.PaginatedResult
import com.impraise.supr.data.ResultList
import com.impraise.suprdemo.scenes.data.model.Member
import io.reactivex.Flowable
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.nhaarman.mockito_kotlin.mock

/**
 * Created by guilhermebranco on 3/10/18.
 */
class LoadRandomPageOfMembersUseCaseTest {

    private lateinit var repository: MockRepo

    private lateinit var useCase: LoadRandomPageOfMembersUseCase

    private lateinit var pagination: Pagination

    @Before
    fun setup() {
        repository = MockRepo()
        pagination = mock()
        useCase = LoadRandomPageOfMembersUseCase(repository, threshold = 5)
    }

    @Test
    fun shouldSplitListOfMembers() {
        val testObserver = useCase.get(Unit).test()

        testObserver.assertComplete()
        val result = testObserver.values().first() as ResultList.Success
        result.data.numberOfGroupsEqualsTo(3)
        result.data.numberOfMemberEqualsTo(5, 0)
        result.data.numberOfMemberEqualsTo(5, 1)
        result.data.numberOfMemberEqualsTo(2, 2)
    }

    private fun List<List<Member>>.numberOfGroupsEqualsTo(expected: Int) {
        assertEquals(expected, this.size)
    }

    private fun List<List<Member>>.numberOfMemberEqualsTo(expected: Int, groupIndex: Int) {
        assertEquals(expected, this[groupIndex].size)
    }
}

class MockRepo : PaginatedRepository<Member> {

    override fun fetch(pagination: Pagination): Flowable<PaginatedResult<Member>> {
        val members = members()
        return Flowable.just(PaginatedResult.Success(members, PageDetail(true, members.count(), 0)))
    }

    private fun members(): List<Member> {
        return (1..12).map { Member(it.toString(), it.toString()) }
    }
}