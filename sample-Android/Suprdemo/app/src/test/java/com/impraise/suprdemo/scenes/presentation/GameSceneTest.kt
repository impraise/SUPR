package com.impraise.suprdemo.scenes.presentation

import com.impraise.supr.data.Result
import com.impraise.suprdemo.scenes.domain.CreateGameUseCase
import com.impraise.suprdemo.scenes.domain.model.Game
import com.impraise.suprdemo.scenes.domain.model.GameState
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
        given(game.currentState).willReturn(GameState.EMPTY_GAME)
        given(createGameUseGame.get(Unit)).willReturn(Single.just(Result.Success(game)))
        scene = GameScene(gamePresenter, createGameUseGame)
    }

    @Test
    fun shouldCallCreateGame() {
        scene.onInteraction(GameSceneInteraction.OnLoad())

        verify(createGameUseGame).get(Unit)
    }

    @Test
    fun shouldEmitLoading() {
        scene.onInteraction(GameSceneInteraction.OnLoad())

        verify(gamePresenter).loading()
    }

    @Test
    fun shouldCallGameState() {
        scene.onInteraction(GameSceneInteraction.OnLoad())
        scene.onInteraction(GameSceneInteraction.StartGame())
        verify(game).currentState
    }

    @Test
    fun shouldNotCallGameState() {
        scene.onInteraction(GameSceneInteraction.OnLoad())

        verify(gamePresenter).present(captor.capture())

        (captor.firstValue as Result.Success<GameState>).also {
            assertTrue(it.data == GameState.EMPTY_GAME)
        }
    }

    @Test
    fun shouldEmmitErrorWhenResultError() {
        given(createGameUseGame.get(Unit)).willReturn(Single.just(Result.Error(Exception())))

        scene = GameScene(gamePresenter, createGameUseGame)
        scene.onInteraction(GameSceneInteraction.OnLoad())

        verify(gamePresenter).present(captor.capture())

        assertTrue(captor.firstValue is Result.Error)
    }

    @Test
    fun shouldEmmitErrorWhenFailred() {
        given(createGameUseGame.get(Unit)).willReturn(Single.error(Exception()))

        scene = GameScene(gamePresenter, createGameUseGame)
        scene.onInteraction(GameSceneInteraction.OnLoad())

        verify(gamePresenter).present(captor.capture())

        assertTrue(captor.firstValue is Result.Error)
    }
}