package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.data.Result
import com.impraise.suprdemo.scenes.data.model.Member
import com.impraise.suprdemo.scenes.domain.model.Option
import com.impraise.suprdemo.scenes.helper.GameTestHelper
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*

/**
 * Created by guilhermebranco on 3/11/18.
 */
class CreateRoundUseCaseTest {

    companion object {
        private const val NUMBER_OF_OPTIONS = 4
    }

    private lateinit var useCase: CreateRoundUseCase
    private lateinit var helper: RoundCreationHelper

    @Before
    fun setup() {
        helper = RoundCreationHelper(GameTestHelper.alwaysTrueCondition())
        useCase = CreateRoundUseCase(NUMBER_OF_OPTIONS, helper)
    }

    @Test
    fun `should return error`() {
        useCase = CreateRoundUseCase(1, helper)

        val testObserver = useCase.get(emptyList()).test()

        testObserver.assertComplete()
        val result = testObserver.values().first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `should create round`() {
        val members = members()
        val testObserver = useCase.get(members).test()

        testObserver.assertComplete()
        val result = testObserver.values().first()
        assertTrue(result is Result.Success)
        val round = result as Result.Success
        assertEquals(NUMBER_OF_OPTIONS, round.data.options.size)
        val correctOption = round.data.options.find { it is Option.Correct }
        val correct = members.find { it.name == correctOption?.name }
        assertEquals(round.data.avatarUrl, correct?.avatarUrl)
    }

    @Test
    fun `should choose correct option with avatar`() {
        val members = members().take(4)
        val selectedMember = members[2]
        val condition = mock<RoundCreationHelper.Condition<Member>>().apply {
            given(this.satisfied(selectedMember)).willReturn(true)
        }

        helper = object : RoundCreationHelper(condition) {
            override fun randomIndex(total: Int): Int {
                return 3
            }
        }

        val correct = helper.returnCorrectOptionOrFirstWithAvatar(members, NUMBER_OF_OPTIONS)

        assertEquals(2, correct)
    }

    @Test
    fun `should keep first option when it has avatar`() {
        helper = object : RoundCreationHelper(satisfied) {
            override fun randomIndex(total: Int): Int {
                return 1
            }
        }

        val correct = helper.returnCorrectOptionOrFirstWithAvatar(membersWithOnlyOneAvatar(1), NUMBER_OF_OPTIONS)

        assertEquals(1, correct)
    }

    private fun members(): List<Member> {
        return (1..10).map { Member(it.toString(), it.toString()) }
    }

    private fun membersWithOnlyOneAvatar(optionWithAvatar: Int): List<Member> {
        return (0..4).map {
            if (it == optionWithAvatar) Member(it.toString(), it.toString())
            else Member(it.toString(), "")
        }
    }

    val satisfied = object : RoundCreationHelper.Condition<Member> {

        override fun satisfied(param: Member): Boolean {
            return true
        }
    }
}

