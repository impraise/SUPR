package com.impraise.suprdemo.scenes.presentation

import com.impraise.supr.data.Result
import com.impraise.supr.presentation.Scene
import com.impraise.suprdemo.scenes.domain.CreateGameUseCase
import com.impraise.suprdemo.scenes.domain.model.Game
import com.impraise.suprdemo.scenes.domain.model.GameState
import com.impraise.suprdemo.scenes.domain.model.Option
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
                    gamePresenter.present(Result.Success(it.currentState))
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    gamePresenter.loading()
                }
                .subscribe({ result ->
                    when (result) {
                        is Result.Success -> {
                            game = result.data
                            gamePresenter.present(Result.Success(result.data.currentState))
                        }

                        is Result.Error -> {
                            gamePresenter.present(Result.Error(result.error, GameState.EMPTY_GAME))
                        }
                    }
                }, {

                })
    }
}