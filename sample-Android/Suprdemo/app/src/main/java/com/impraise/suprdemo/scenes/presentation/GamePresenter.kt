package com.impraise.suprdemo.scenes.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.impraise.common.presentation.Presenter
import com.impraise.common.presentation.ViewModelEntity
import com.impraise.common.presentation.ViewModelEntityState
import com.impraise.supr.domain.DomainResult
import com.impraise.suprdemo.scenes.domain.model.GameState
import com.impraise.suprdemo.scenes.presentation.model.GameStateViewModel

/**
 * Created by guilhermebranco on 3/12/18.
 */
class GamePresenter: Presenter<DomainResult<GameState>, GameStateViewModel> {

    private val liveData = MutableLiveData<ViewModelEntity<GameStateViewModel, String>>()

    override val viewModelStream: LiveData<ViewModelEntity<GameStateViewModel, String>>
        get() = liveData

    override fun present(state: ViewModelEntityState, param: DomainResult<GameState>) {
        when(state) {
            ViewModelEntityState.Loading, ViewModelEntityState.Empty -> liveData.postValue(ViewModelEntity(state, GameStateViewModel(), ""))
            ViewModelEntityState.Success -> liveData.postValue(ViewModelEntity(state, param.data.toViewModel(), ""))
            ViewModelEntityState.Error -> liveData.postValue(ViewModelEntity(state, GameStateViewModel(), param.error?.message))
        }
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