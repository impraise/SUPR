package com.impraise.suprdemo.scenes.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.impraise.supr.data.Result
import com.impraise.supr.presentation.Presenter
import com.impraise.suprdemo.scenes.domain.model.GameState
import com.impraise.suprdemo.scenes.domain.model.Option
import com.impraise.suprdemo.scenes.presentation.model.GameViewModel

/**
 * Created by guilhermebranco on 3/12/18.
 */
class GamePresenter : Presenter<Result<GameState>, GameViewModel> {

    private val liveData = MutableLiveData<GameViewModel>()

    override val viewModelStream: LiveData<GameViewModel>
        get() = liveData

    override fun present(param: Result<GameState>) {
        when (param) {
            is Result.Success -> liveData.postValue(param.data.toViewModel())
            is Result.Error -> liveData.postValue(GameViewModel.ErrorViewModel())
        }
    }

    override fun loading() {
        liveData.postValue(GameViewModel.LoadingViewModel())
    }

    private fun GameState.toViewModel(): GameViewModel {
        return if (this.gameOver) {
            GameViewModel.GameOverViewModel(this.score.total.toString())
        } else if (this == GameState.EMPTY_GAME) {
            GameViewModel.GameNotStartedViewModel()
        } else if (!this.answeredRound) {
            GameViewModel.GameStateViewModel("${this.currentRound}/${this.totalRounds}", this.currentOptions.map {
                Option.Undefined(it.name)
            }, this.answeredRound)
        } else {
            GameViewModel.GameStateViewModel("${this.currentRound}/${this.totalRounds}", this.currentOptions, this.answeredRound)
        }
    }
}