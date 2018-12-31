package com.impraise.supr.game.scenes.presentation

import com.impraise.supr.presentation.Interaction
import com.impraise.supr.presentation.InteractionType

/**
 * Created by guilhermebranco on 3/12/18.
 */
sealed class GameSceneInteraction(interactionType: InteractionType): Interaction(interactionType) {

    class OnLoad: GameSceneInteraction(InteractionType.LIFECYCLE)
    class Continue: GameSceneInteraction(InteractionType.ACTION)
    class StartGame: GameSceneInteraction(InteractionType.ACTION)
    class Answer(val option: String): GameSceneInteraction(InteractionType.ACTION)
}