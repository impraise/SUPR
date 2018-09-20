package com.impraise.suprdemo.scenes.presentation

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.impraise.supr.data.Result
import com.impraise.suprdemo.scenes.domain.model.Game
import com.impraise.suprdemo.scenes.domain.model.Option
import com.impraise.suprdemo.scenes.domain.model.Round
import com.impraise.suprdemo.scenes.presentation.model.GameViewModel
import junit.framework.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GamePresenterTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun shouldShowUndefinedOptionWhenNotAnswer() {
        val round = Round("", listOf(Option.Correct("correct"), Option.Wrong("wrong")))
        val game = Game(listOf(round))
        var result: GameViewModel? = null

        val gamePresenter = GamePresenter()

        val currentState = game.currentState
        gamePresenter.viewModelStream.observeForever {
            result = it
        }
        gamePresenter.present(Result.Success(currentState))

        Assert.assertFalse(currentState.answeredRound)
        result?.let {
            val gameStateViewModel = it as GameViewModel.GameStateViewModel
            gameStateViewModel.options.forEach {
                Assert.assertFalse(it is Option.Correct)
            }
        }
    }

    @Test
    fun shouldShowCorrectStateAnswered() {
        val option = Option.Correct("correct")
        val round = Round("", listOf(option, Option.Wrong("wrong")))
        val game = Game(listOf(round))
        var result: GameViewModel? = null

        val gamePresenter = GamePresenter()
        game.answer(option)

        val currentState = game.currentState
        gamePresenter.viewModelStream.observeForever {
            result = it
        }
        gamePresenter.present(Result.Success(currentState))

        Assert.assertTrue(currentState.answeredRound)
        result?.let {
            val gameStateViewModel = it as GameViewModel.GameStateViewModel
            gameStateViewModel.options[0] is Option.Correct
            gameStateViewModel.options[1] is Option.Wrong
        }
    }
}