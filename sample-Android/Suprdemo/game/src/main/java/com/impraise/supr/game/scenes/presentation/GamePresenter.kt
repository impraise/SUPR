package com.impraise.supr.game.scenes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.Context
import com.impraise.supr.data.Result
import com.impraise.supr.game.R
import com.impraise.supr.game.scenes.domain.model.GameState
import com.impraise.supr.game.scenes.presentation.model.GameViewModel
import com.impraise.supr.presentation.Presenter
import com.impraise.suprdemo.scenes.domain.model.Option
import java.lang.ref.WeakReference

/**
 * Created by guilhermebranco on 3/12/18.
 */
class GamePresenter(private var context: WeakReference<Context>) : Presenter<Result<GameState>, GameViewModel> {

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
        liveData.postValue(GameViewModel.LoadingViewModel)
    }

    private fun GameState.toViewModel(): GameViewModel {
        return if (this.gameOver) {
            val score = context.get()?.getString(R.string.your_score, this.score.total) ?: ""
            GameViewModel.GameOverViewModel(score)
        } else if (this == GameState.EMPTY_GAME) {
            GameViewModel.GameNotStartedViewModel
        } else if (!this.answeredRound) {
            GameViewModel.GameStateViewModel(this.currentRound.avatarUrl, this.currentOptions.map {
                Option.Undefined(it.name)
            }, this.answeredRound)
        } else {
            GameViewModel.GameStateViewModel(this.currentRound.avatarUrl, this.currentOptions, this.answeredRound)
        }
    }
}