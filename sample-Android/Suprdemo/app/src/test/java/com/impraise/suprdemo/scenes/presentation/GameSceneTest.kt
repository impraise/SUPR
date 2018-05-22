package com.impraise.suprdemo.scenes.presentation

import com.impraise.common.presentation.ViewModelEntityState
import com.impraise.supr.domain.DomainResult
import com.impraise.suprdemo.scenes.domain.CreateGameUseCase
import com.impraise.suprdemo.scenes.domain.model.Game
import com.impraise.suprdemo.scenes.domain.model.GameState
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Created by guilhermebranco on 3/12/18.
 */
class GameSceneTest {

    @Mock
    private lateinit var createGameUseGame: CreateGameUseCase

    @Mock
    private lateinit var gamePresenter: GamePresenter

    private lateinit var domainResultCaptor: KArgumentCaptor<DomainResult<GameState>>

    private lateinit var stateCaptor: KArgumentCaptor<ViewModelEntityState>

    private lateinit var scene: GameScene

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        domainResultCaptor = argumentCaptor()
        stateCaptor = argumentCaptor()
        scene = GameScene(gamePresenter, createGameUseGame)
    }

    @Test
    fun shouldCallCreateGame() {
        scene.onInteraction(GameSceneInteraction.OnLoad())

        verify(createGameUseGame).doYourJob()
    }

    @Test
    fun shouldEmitLoading() {
        scene.onInteraction(GameSceneInteraction.OnLoad())

        verify(gamePresenter).present(stateCaptor.capture(), domainResultCaptor.capture())

        assertEquals(ViewModelEntityState.Loading, stateCaptor.firstValue)
        assertEquals(GameState.EMPTY_GAME, domainResultCaptor.firstValue.data)
    }
}