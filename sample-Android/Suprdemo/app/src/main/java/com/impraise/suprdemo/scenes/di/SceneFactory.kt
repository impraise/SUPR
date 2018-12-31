package com.impraise.suprdemo.scenes.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.impraise.supr.data.PaginatedRepository
import com.impraise.supr.game.scenes.data.model.Member
import com.impraise.supr.game.scenes.domain.*
import com.impraise.supr.game.scenes.presentation.GamePresenter
import com.impraise.supr.game.scenes.presentation.GameScene
import com.impraise.suprdemo.scenes.data.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by guilhermebranco on 3/13/18.
 */
class SceneFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameScene::class.java)) {
            return scene as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private val scene: GameScene by lazy {
        GameScene(createGameUseCase = CreateGameUseCase(loadMembersUseCase,
                CreateRoundUseCase(roundCreationHelper = RoundCreationHelper(imageAvailableCondition)),
                gameCreationHelper = GameCreationHelper(imageAvailableCondition)),
                gamePresenter = GamePresenter())
    }

    private val loadMembersUseCase: LoadRandomPageOfMembersUseCase by lazy {
        LoadRandomPageOfMembersUseCase(repository)
    }

    private val repository: PaginatedRepository<Member> by lazy {
        MarvelApiRepository(provideOkHttpClient(loggingInterceptor))
    }

    private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(NetworkInterceptor())
                .addInterceptor(interceptor)
                .build()
    }

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        loggingInterceptor
    }

    private val imageAvailableCondition: RoundCreationHelper.Condition<Member> by lazy {
        ImageAvailableCondition()
    }
}

