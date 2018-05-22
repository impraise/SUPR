package com.impraise.suprdemo.scenes.presentation

import com.impraise.common.presentation.Interaction
import com.impraise.common.presentation.InteractionType

/**
 * Created by guilhermebranco on 3/12/18.
 */
sealed class GameSceneInteraction(interactionType: InteractionType): Interaction(interactionType) {

    class OnLoad: GameSceneInteraction(InteractionType.LIFECYCLE)
    class Continue: GameSceneInteraction(InteractionType.ACTION)
    class StartGame: GameSceneInteraction(InteractionType.ACTION)
    class Answer(val option: String): GameSceneInteraction(InteractionType.ACTION)
}