package com.impraise.suprdemo.scenes.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.impraise.suprdemo.scenes.data.InMemoryMemberRepository
import com.impraise.suprdemo.scenes.data.MemberRepository
import com.impraise.suprdemo.scenes.domain.CreateGameUseCase
import com.impraise.suprdemo.scenes.domain.CreateRoundUseCase
import com.impraise.suprdemo.scenes.domain.MembersPaginatedUseCase
import com.impraise.suprdemo.scenes.presentation.GamePresenter
import com.impraise.suprdemo.scenes.presentation.GameScene

/**
 * Created by guilhermebranco on 3/13/18.
 */
class SceneFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameScene::class.java)) {
            return scene as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private val scene: GameScene by lazy {
        GameScene(createGameUseCase = CreateGameUseCase(membersPaginatedUseCase, CreateRoundUseCase()), gamePresenter = GamePresenter())
    }

    private val membersPaginatedUseCase: MembersPaginatedUseCase by lazy {
        MembersPaginatedUseCase(repository)
    }

    private val repository: MemberRepository by lazy {
        InMemoryMemberRepository()
    }
}