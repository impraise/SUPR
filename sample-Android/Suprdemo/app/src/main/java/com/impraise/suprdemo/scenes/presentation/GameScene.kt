package com.impraise.suprdemo.scenes.presentation

import com.impraise.supr.data.Result
import com.impraise.supr.presentation.Scene
import com.impraise.suprdemo.scenes.domain.CreateGameUseCase
import com.impraise.suprdemo.scenes.domain.model.Game
import com.impraise.suprdemo.scenes.domain.model.GameState
import com.impraise.suprdemo.scenes.domain.model.Option

/**
 * Created by guilhermebranco on 3/12/18.
 */
class GameScene(val gamePresenter: GamePresenter, private val createGameUseCase: CreateGameUseCase): Scene<GameSceneInteraction>() {

    private var game: Game? = null

    override fun onInteraction(interaction: GameSceneInteraction) {
        when(interaction) {
            is GameSceneInteraction.StartGame -> {
                gamePresenter.loading()
                createGameUseCase.get(Unit).subscribe({

                    when (it) {
                        is Result.Success -> {
                            gamePresenter.present(Result.Success(it.data.currentState))
                        }

                        is Result.Error -> {
                            gamePresenter.present(Result.Error(it.error, GameState.EMPTY_GAME))
                        }
                    }
                }, {

                })
            }
            is GameSceneInteraction.Answer -> {
                game?.answer(Option.Undefined(interaction.option))?.let {
                    gamePresenter.present(Result.Success(it))
                }
            }
            is GameSceneInteraction.Continue -> {
                game?.next()?.let {
                    gamePresenter.present(Result.Success(it))
                }
            }
        }
    }
}