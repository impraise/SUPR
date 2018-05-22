package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.domain.DomainResult
import com.impraise.supr.domain.DomainResultList
import com.impraise.suprdemo.scenes.data.model.Member
import com.impraise.suprdemo.scenes.domain.model.Game
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.BDDMockito.*
import org.mockito.Mock

/**
 * Created by guilhermebranco on 3/11/18.
 */
class CreateGameUseCaseTest {

    @Mock
    private lateinit var membersPaginatedUseCase: MembersPaginatedUseCase

    private lateinit var useCase: CreateGameUseCase

    private lateinit var result: DomainResult<Game>

    @Before
    fun setup() {
        val members = members()
        membersPaginatedUseCase = mock()
        given(membersPaginatedUseCase.get(any())).willReturn(Single.just(DomainResultList.success(members)))
        useCase = CreateGameUseCase(membersPaginatedUseCase, CreateRoundUseCase())
        useCase.callback = {
            result  = it
        }
    }

    @Test
    fun shouldCreateGameWithFiveRounds() {
        useCase.doYourJob()

        assertNotNull(result.data)
        assertEquals(1, result.data?.currentState?.currentRound)
        assertEquals(5, result.data?.currentState?.totalRounds)
    }

    private fun members(): List<List<Member>> {
        val members = mutableListOf<List<Member>>()

        (1..5).forEach {
            val current = (1..4).map { Member(it.toString(), it.toString()) }
            members.add(current)
        }
        return members
    }
}