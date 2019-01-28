package com.impraise.supr.game.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import com.impraise.supr.data.Result
import com.impraise.supr.game.scenes.domain.model.Game
import com.impraise.supr.game.scenes.domain.model.GameState
import com.impraise.supr.game.scenes.domain.model.Score
import com.impraise.supr.game.scenes.presentation.GamePresenter
import com.impraise.supr.game.scenes.presentation.model.GameViewModel
import com.impraise.suprdemo.scenes.domain.model.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.BDDMockito.*
import java.lang.ref.WeakReference

@RunWith(JUnit4::class)
class GamePresenterTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var context: Context

    lateinit var gamePresenter: GamePresenter
    var result: GameViewModel? = null

    @Before
    fun setup() {
        context = mock<Context>().also {
            given(it.getString(any(), any())).willReturn("")
        }
        gamePresenter = GamePresenter(WeakReference(context))
        gamePresenter.viewModelStream.observeForever {
            result = it
        }
    }

    @Test
    fun shouldShowUndefinedOptionWhenNotAnswer() {
        val round = Round("", listOf(Option.Correct("correct"), Option.Wrong("wrong")))
        val game = Game(listOf(round))
        var result: GameViewModel? = null

        val currentState = game.currentState

        gamePresenter.present(Result.Success(currentState))

        assertFalse(currentState.answeredRound)
        result?.let {
            val gameStateViewModel = it as GameViewModel.GameStateViewModel
            gameStateViewModel.options.forEach {
                assertFalse(it is Option.Correct)
            }
        }
    }

    @Test
    fun shouldShowCorrectStateAnswered() {
        val option = Option.Correct("correct")
        val round = Round("", listOf(option, Option.Wrong("wrong")))
        val game = Game(listOf(round))
        var result: GameViewModel? = null

        game.answer(option)

        val currentState = game.currentState
        gamePresenter.present(Result.Success(currentState))

        assertTrue(currentState.answeredRound)
        result?.let {
            val gameStateViewModel = it as GameViewModel.GameStateViewModel
            assertTrue(gameStateViewModel.options[0] is Option.Correct)
            assertTrue(gameStateViewModel.options[1] is Option.Wrong)
        }
    }

    @Test
    fun shouldShowGameNotInitiated() {
        gamePresenter.present(Result.Success(GameState.EMPTY_GAME))
        result?.let {
            assertTrue(it is GameViewModel.GameNotStartedViewModel)
        }
    }

    @Test
    fun shouldShowScoreWhenGameOver() {
        val gameState = GameState(gameOver = true, score = Score(5))
        gamePresenter.present(Result.Success(gameState))
        result?.let {
            assertTrue(it is GameViewModel.GameOverViewModel)
        }
    }
}