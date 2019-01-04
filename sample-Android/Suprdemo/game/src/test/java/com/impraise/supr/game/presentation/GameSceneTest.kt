package com.impraise.supr.game.presentation

import com.impraise.supr.data.Result
import com.impraise.supr.game.scenes.domain.CreateGameUseCase
import com.impraise.supr.game.scenes.domain.model.Game
import com.impraise.supr.game.scenes.domain.model.GameState
import com.impraise.supr.game.scenes.presentation.GamePresenter
import com.impraise.supr.game.scenes.presentation.GameScene
import com.impraise.supr.game.scenes.presentation.GameSceneInteraction
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations.*

/**
 * Created by guilhermebranco on 3/12/18.
 */
class GameSceneTest {

    @Mock
    private lateinit var createGameUseGame: CreateGameUseCase

    @Mock
    private lateinit var gamePresenter: GamePresenter

    @Mock
    private lateinit var game: Game

    private lateinit var captor: KArgumentCaptor<Result<GameState>>

    private lateinit var scene: GameScene

    @Before
    fun setup() {
        initMocks(this)
        captor = argumentCaptor()
        game = mock()
        with(game) {
            given(currentState).willReturn(GameState.EMPTY_GAME)
            given(isValid()).willReturn(true)
        }

        given(createGameUseGame.get(Unit)).willReturn(Single.just(Result.Success(game)))
        scene = GameScene(gamePresenter, createGameUseGame)
    }

    @Test
    fun `should call create game`() {
        scene.onInteraction(GameSceneInteraction.OnLoad())

        verify(createGameUseGame).get(Unit)
    }

    @Test
    fun `should emit loading`() {
        scene.onInteraction(GameSceneInteraction.OnLoad())

        verify(gamePresenter).loading()
    }

    @Test
    fun `should call game state`() {
        scene.onInteraction(GameSceneInteraction.OnLoad())
        scene.onInteraction(GameSceneInteraction.StartGame())
        verify(game).currentState
    }

    @Test
    fun `should not call game state`() {
        scene.onInteraction(GameSceneInteraction.OnLoad())

        verify(gamePresenter).present(captor.capture())

        (captor.firstValue as Result.Success<GameState>).also {
            assertTrue(it.data == GameState.EMPTY_GAME)
        }
    }

    @Test
    fun `should emmit error when result error`() {
        given(createGameUseGame.get(Unit)).willReturn(Single.just(Result.Error(Exception())))

        scene = GameScene(gamePresenter, createGameUseGame)
        scene.onInteraction(GameSceneInteraction.OnLoad())

        verify(gamePresenter).present(captor.capture())

        assertTrue(captor.firstValue is Result.Error)
    }

    @Test
    fun `should emmit error when failure`() {
        given(createGameUseGame.get(Unit)).willReturn(Single.error(Exception()))

        scene = GameScene(gamePresenter, createGameUseGame)
        scene.onInteraction(GameSceneInteraction.OnLoad())

        verify(gamePresenter).present(captor.capture())

        assertTrue(captor.firstValue is Result.Error)
    }

    @Test
    fun `should recreate the game when trying to start an invalid game`() {
        given(createGameUseGame.get(Unit)).willReturn(Single.just(Result.Success(Game(emptyList()))))

        scene.onInteraction(GameSceneInteraction.OnLoad())
        scene.onInteraction(GameSceneInteraction.StartGame())

        verify(createGameUseGame, times(2)).get(Unit)
    }
}