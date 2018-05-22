package com.impraise.suprdemo.scenes.domain

import com.impraise.suprdemo.scenes.domain.model.Game
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

    @Before
    fun setup() {
        game = Game(fiveRounds())
    }

    @Test
    fun shouldCreateGameWithCurrentState() {
        assertEquals(1, game.currentState.currentRound)
        assertEquals(5, game.currentState.totalRounds)
    }

    @Test
    fun shouldMoveToNextRound() {
        val next = game.next()
        assertEquals(2, next.currentRound)
        assertEquals(5, next.totalRounds)
    }

    @Test
    fun shouldFinishGameAfterLastRound() {
        game.next()
        game.next()
        game.next()
        val finalState = game.next()
        assertEquals(true, finalState.gameOver)

    }

    @Test
    fun shouldAnswerCorrectOption() {
        val correctIndex = 2
        val options = options(correctIndex)
        val round = Round("", options)

        game = Game(listOf(round))

        val currentState = game.answer(options[correctIndex])

        assertTrue(currentState.answeredRound)
        currentState.currentAnswers.assertNoWrongAnswers()
    }

    @Test
    fun shouldAnswerWrongOption() {
        val correctIndex = 2
        val options = options(correctIndex)
        val round = Round("", options)

        game = Game(listOf(round))


        val wrong = 1

        val currentState = game.answer(options[wrong])

        assertTrue(currentState.answeredRound)
        currentState.currentAnswers.assertWrongAnswerAt(wrong)
    }

    @Test
    fun shouldFinishGameAfterAllRounds() {
        game.next()
        game.next()
        game.next()
        game.next()

        val currentState = game.currentState
        assertEquals(true, currentState.gameOver)
    }

    @Test
    fun shouldCalculateScore() {
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

    private fun fiveRounds(): List<Round> {
        return (1..5).map { Round(it.toString(), emptyList()) }
    }

    private fun options(correctIndex: Int): List<Option> {
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