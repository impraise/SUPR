package com.impraise.supr.game.scenes.presentation.model

import com.impraise.suprdemo.scenes.domain.model.Option

/**
 * Created by guilhermebranco on 3/12/18.
 */

sealed class GameViewModel {

    class GameStateViewModel(val round: String = "", val options: List<Option> = emptyList(), val showContinueButton: Boolean = false) : GameViewModel()
    object LoadingViewModel : GameViewModel()
    object GameNotStartedViewModel : GameViewModel()
    class ErrorViewModel(val errorMessage: String = "") : GameViewModel()
    class GameOverViewModel(val score: String = "0") : GameViewModel()
}
