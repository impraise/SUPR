package com.impraise.supr.game.domain

import com.impraise.supr.game.scenes.domain.model.Game
import com.impraise.suprdemo.scenes.domain.model.Option
import com.impraise.suprdemo.scenes.domain.model.Round
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Created by guilhermebranco on 3/11/18.
 */
class GameTest {

    private lateinit var game: Game
    private var rounds: List<Round> = emptyList()

    @Before
    fun setup() {
        rounds = fiveRounds()
        game = Game(rounds)
    }

    @Test
    fun `should create game with current state`() {
        assertEquals(rounds[0], game.currentState.currentRound)
        assertEquals(5, game.currentState.totalRounds)
    }

    @Test
    fun `should move to next round`() {
        val next = game.next()
        assertEquals(rounds[1], next.currentRound)
        assertEquals(5, next.totalRounds)
    }

    @Test
    fun `should finish game after last round`() {
        (1..5).forEach { _ ->
            game.answer(Option.Wrong(""))
            game.next()
        }
        val finalState = game.next()
        assertEquals(true, finalState.gameOver)
    }

    @Test
    fun `should answer correct option`() {
        val correctIndex = 2
        val options = options(correctIndex)
        val round = Round("", options)

        game = Game(listOf(round))

        val currentState = game.answer(options[correctIndex])

        assertTrue(currentState.answeredRound)
        currentState.currentOptions.assertNoWrongAnswers()
    }

    @Test
    fun `should answer wrong option`() {
        val correctIndex = 2
        val options = options(correctIndex)
        val round = Round("", options)

        game = Game(listOf(round))


        val wrong = 1

        val currentState = game.answer(options[wrong])

        assertTrue(currentState.answeredRound)
        currentState.currentOptions.assertWrongAnswerAt(wrong)
    }

    @Test
    fun `should not finish if there is a round without answers`() {
        game.next()
        game.next()
        game.next()
        game.next()

        val currentState = game.currentState
        assertFalse(currentState.gameOver)
    }

    @Test
    fun `should calculate score`() {
        val correctIndex = 2
        val wrongIndex = 1
        val options = options(correctIndex)
        val round = Round("", options)

        game = Game(listOf(round, round, round))

        game.answer(options[correctIndex])
        game.next()
        game.answer(options[wrongIndex])
        game.next()
        game.answer(options[correctIndex])
        val currentState = game.next()

        assertTrue(currentState.gameOver)
        assertEquals(2, currentState.score.total)
    }

    @Test
    fun `should not accept more than one answer for the same round`() {
        val options = options()
        val round = Round("", options)
        val game = Game(listOf(round, round))
        val firstAnswer = game.answer(options[0])
        val secondAnswer = game.answer(options[0])

        assertEquals(firstAnswer, secondAnswer)
    }

    private fun fiveRounds(): List<Round> {
        return (1..5).map { Round(it.toString(), emptyList()) }
    }

    private fun options(correctIndex: Int = 0): List<Option> {
        val options = mutableListOf<Option>()
        for (index in 0..3) {
            if (index == correctIndex) {
                options.add(Option.Correct(index.toString()))
            } else {
                options.add(Option.Undefined(index.toString()))
            }
        }
        return options
    }

    private fun List<Option>.assertNoWrongAnswers(){
        this.forEach {
            assertFalse(it is Option.Wrong)
        }
    }

    private fun List<Option>.assertWrongAnswerAt(index: Int) {
        this.forEachIndexed { i, option ->
            if (index == i) {
                assertTrue(option is Option.Wrong)
            } else {
                assertFalse(option is Option.Wrong)
            }
        }
    }
}