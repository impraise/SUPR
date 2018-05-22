package com.impraise.suprdemo.scenes.presentation

import com.impraise.common.presentation.ViewModelEntityState
import com.impraise.supr.domain.DomainResult
import com.impraise.supr.domain.DomainResultState
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

    init {
        useCases.add(createGameUseCase)
        createGameUseCase.callback = { domainResult ->
            if (domainResult.state == DomainResultState.Error) {
                gamePresenter.present(ViewModelEntityState.Error, DomainResult.error(domainResult.error!!, GameState.EMPTY_GAME))
            } else {
                domainResult.data?.let {
                    game = it
                    gamePresenter.present(ViewModelEntityState.Success, DomainResult.success(it.currentState))
                }
            }
        }
    }

    override fun onInteraction(interaction: GameSceneInteraction) {
        when(interaction) {
            is GameSceneInteraction.OnLoad -> {
                gamePresenter.present(ViewModelEntityState.Empty, DomainResult.success(GameState.EMPTY_GAME))
            }
            is GameSceneInteraction.StartGame -> {
                gamePresenter.present(ViewModelEntityState.Loading, DomainResult.success(GameState.EMPTY_GAME))
                createGameUseCase.doYourJob()
            }
            is GameSceneInteraction.Answer -> {
                game?.answer(Option.Undefined(interaction.option))?.let {
                    gamePresenter.present(ViewModelEntityState.Success, DomainResult.success(it))
                }
            }
            is GameSceneInteraction.Continue -> {
                game?.next()?.let {
                    gamePresenter.present(ViewModelEntityState.Success, DomainResult.success(it))
                }
            }
        }
    }
}