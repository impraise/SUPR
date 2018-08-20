package com.impraise.suprdemo.scenes.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.impraise.supr.data.Result
import com.impraise.supr.presentation.Presenter
import com.impraise.suprdemo.scenes.domain.model.GameState
import com.impraise.suprdemo.scenes.presentation.model.GameStateViewModel

/**
 * Created by guilhermebranco on 3/12/18.
 */
class GamePresenter : Presenter<Result<GameState>, GameStateViewModel> {

    private val liveData = MutableLiveData<GameStateViewModel>()

    override val viewModelStream: LiveData<GameStateViewModel>
        get() = liveData

    override fun present(param: Result<GameState>) {
        when(param) {
            is Result.Success -> liveData.postValue(param.data.toViewModel())
            is Result.Error -> liveData.postValue(GameStateViewModel())
        }
    }

    override fun loading() {
        liveData.postValue(GameStateViewModel())
    }

    private fun GameState?.toViewModel(): GameStateViewModel {
        return this?.let {
            GameStateViewModel("${it.currentRound}/${it.totalRounds}",
                    it.score.total.toString(),
                    it.currentAnswers,
                    it.gameOver)
        } ?: GameStateViewModel()
    }
}