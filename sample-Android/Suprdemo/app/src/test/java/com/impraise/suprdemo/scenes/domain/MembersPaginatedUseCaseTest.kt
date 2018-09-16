package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.data.ResultList
import com.impraise.suprdemo.scenes.data.model.Member
import com.impraise.suprdemo.scenes.data.MemberRepository
import io.reactivex.Flowable
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations.*

/**
 * Created by guilhermebranco on 3/10/18.
 */
class MembersPaginatedUseCaseTest {

    @Mock
    private lateinit var repository: MemberRepository

    private lateinit var useCase: MembersPaginatedUseCase

    @Before
    fun setup() {
        initMocks(this)
        useCase = MembersPaginatedUseCase(repository)
    }

    @Test
    fun shouldSplitListOfMembers() {
        given(repository.all()).willReturn(Flowable.just(ResultList.Success(members())))

        val testObserver = useCase.get(Unit).test()

        testObserver.assertComplete()
        val result = testObserver.values().first() as ResultList.Success
        result.data.numberOfGroupsEqualsTo(3)
        result.data.numberOfMemberEqualsTo(5, 0)
        result.data.numberOfMemberEqualsTo(5, 1)
        result.data.numberOfMemberEqualsTo(2, 2)
    }

    private fun members(): List<Member> {
        return (1..12).map { Member(it.toString(), it.toString()) }
    }

    private fun List<List<Member>>.numberOfGroupsEqualsTo(expected: Int) {
        assertEquals(expected, this.size)
    }

    private fun List<List<Member>>.numberOfMemberEqualsTo(expected: Int, groupIndex: Int) {
        assertEquals(expected, this[groupIndex].size)
    }
}