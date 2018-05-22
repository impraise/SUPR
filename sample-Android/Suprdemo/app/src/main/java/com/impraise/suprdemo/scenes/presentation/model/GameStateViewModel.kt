package com.impraise.suprdemo.scenes.presentation.model

import com.impraise.suprdemo.scenes.domain.model.Option

/**
 * Created by guilhermebranco on 3/12/18.
 */
class GameStateViewModel(val round: String = "", val score: String = "", val options: List<Option> = emptyList(), val gameOver: Boolean = false)