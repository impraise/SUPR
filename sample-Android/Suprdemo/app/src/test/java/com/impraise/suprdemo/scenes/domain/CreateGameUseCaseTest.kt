package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.data.Result
import com.impraise.supr.data.ResultList
import com.impraise.suprdemo.scenes.data.model.Member
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import org.mockito.Mock

/**
 * Created by guilhermebranco on 3/11/18.
 */
class CreateGameUseCaseTest {

    @Mock
    private lateinit var membersPaginatedUseCase: MembersPaginatedUseCase

    private lateinit var useCase: CreateGameUseCase

    @Before
    fun setup() {
        val members = members()
        membersPaginatedUseCase = mock()
        createGame(members)
    }

    @Test
    fun shouldCreateGameWithFiveRounds() {
        val testObserver = useCase.get(Unit).test()

        testObserver.assertComplete()
        testObserver.assertValue {
            val success = it as Result.Success
            success.data.currentState.currentRound == 1
        }

        testObserver.assertValue {
            val success = it as Result.Success
            success.data.currentState.totalRounds == 5
        }
    }

    @Test
    fun shouldFilterGroupsWithNoAvatar() {
        val list = members().toMutableList()
        val expectedSize = list.size
        val listWithAtLeastOneAvatar = (1..4).map {
            if (it == 1) Member(it.toString(), it.toString())
            else Member(it.toString(), "")
        }

        val listWithoutAvatar = (1..4).map {
            Member(it.toString(), "")
        }

        list += listWithoutAvatar
        list += listWithAtLeastOneAvatar

        val result = GameCreationHelper().filterGroupsWithoutAvatar(list)

        Assert.assertEquals(expectedSize + 1, result.size)
    }

    private fun members(): List<List<Member>> {
        val members = mutableListOf<List<Member>>()

        (1..5).forEach {
            val current = (1..4).map { Member(it.toString(), it.toString()) }
            members.add(current)
        }
        return members
    }

    private fun createGame(members: List<List<Member>>) {
        given(membersPaginatedUseCase.get(any())).willReturn(Single.just(ResultList.Success(members)))
        useCase = CreateGameUseCase(membersPaginatedUseCase, CreateRoundUseCase())
    }
}