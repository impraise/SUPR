package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.data.Result
import com.impraise.suprdemo.scenes.data.model.Member
import com.impraise.suprdemo.scenes.domain.model.Option
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

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
        useCase = CreateRoundUseCase(NUMBER_OF_OPTIONS)
    }

    @Test
    fun shouldReturnDomainResultErrorListSizeNotSatisfied() {
        useCase = CreateRoundUseCase(1)

        val testObserver = useCase.get(emptyList()).test()

        testObserver.assertComplete()
        val result = testObserver.values().first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun shouldCreateRound() {
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
    fun shouldChooseCorrectOptionWithAvatar() {
        helper = object : RoundCreationHelper() {
            override fun randomIndex(total: Int): Int {
                return 2
            }
        }

        val correct = helper.returnCorrectOptionOrFirstWithAvatar(membersWithOnlyOneAvatar(3), NUMBER_OF_OPTIONS)

        Assert.assertEquals(3, correct)
    }

    @Test
    fun shouldKeepFirstOptionWhenItHasAvatar() {
        helper = object : RoundCreationHelper() {
            override fun randomIndex(total: Int): Int {
                return 1
            }
        }

        val correct = helper.returnCorrectOptionOrFirstWithAvatar(membersWithOnlyOneAvatar(1), NUMBER_OF_OPTIONS)

        Assert.assertEquals(1, correct)
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
}

