package com.impraise.supr.game.scenes.presentation

import com.impraise.supr.data.Result
import com.impraise.supr.domain.DomainLayerException
import com.impraise.supr.game.scenes.domain.CreateGameUseCase
import com.impraise.supr.game.scenes.domain.model.Game
import com.impraise.supr.game.scenes.domain.model.GameState
import com.impraise.supr.presentation.Scene
import com.impraise.suprdemo.scenes.domain.model.Option
import io.reactivex.rxkotlin.addTo

/**
 * Created by guilhermebranco on 3/12/18.
 */
class GameScene(val gamePresenter: GamePresenter,
                private val createGameUseCase: CreateGameUseCase) : Scene<GameSceneInteraction>() {

    private var game: Game? = null

    override fun onInteraction(interaction: GameSceneInteraction) {
        when (interaction) {

            is GameSceneInteraction.OnLoad -> {
                createGame()
            }

            is GameSceneInteraction.StartGame -> {
                game?.let {
                    if (it.isValid()) gamePresenter.present(Result.Success(it.currentState))
                    else createGame()
                }
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

    private fun createGame() {
        createGameUseCase
                .get(Unit)
                .doOnSubscribe {
                    gamePresenter.loading()
                }
                .subscribe({ result ->
                    when (result) {
                        is Result.Success -> {
                            game = result.data
                            gamePresenter.present(Result.Success(GameState.EMPTY_GAME))
                        }

                        is Result.Error -> {
                            gamePresenter.present(Result.Error(result.error, GameState.EMPTY_GAME))
                        }
                    }
                }, {
                    gamePresenter.present(Result.Error(DomainLayerException(), GameState.EMPTY_GAME))
                }).addTo(subscriptions)
    }
}