package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.data.DataResultList
import com.impraise.suprdemo.scenes.data.model.Member
import com.impraise.suprdemo.scenes.data.MemberRepository
import io.reactivex.Observable
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
        given(repository.all()).willReturn(Observable.just(DataResultList.success(members())))

        val testObserver = useCase.get(Unit).test()

        testObserver.assertComplete()
        val result = testObserver.values().first()
        assertEquals(3, result.data?.size)
        assertEquals(5, result.data?.get(0)?.size)
        assertEquals(5, result.data?.get(1)?.size)
        assertEquals(2, result.data?.get(2)?.size)
    }

    private fun members(): List<Member> {
        return (1..12).map { Member(it.toString(), it.toString()) }
    }
}