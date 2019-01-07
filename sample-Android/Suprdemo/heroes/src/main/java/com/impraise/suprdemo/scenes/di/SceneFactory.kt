package com.impraise.suprdemo.scenes.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.impraise.supr.common.Pagination
import com.impraise.supr.data.PageDetail
import com.impraise.supr.data.PaginatedRepository
import com.impraise.supr.data.PaginatedResult
import com.impraise.supr.game.scenes.data.model.Member
import com.impraise.supr.game.scenes.domain.*
import com.impraise.supr.game.scenes.presentation.GamePresenter
import com.impraise.supr.game.scenes.presentation.GameScene
import com.impraise.suprdemo.HeroesApplication
import com.impraise.suprdemo.scenes.data.*
import com.impraise.suprdemo.scenes.domain.ImageAvailableCondition
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.lang.ref.WeakReference
import java.util.*

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
                gamePresenter = GamePresenter(WeakReference(HeroesApplication.instance)))
    }

    private val loadMembersUseCase: LoadRandomPageOfMembersUseCase by lazy {
        LoadRandomPageOfMembersUseCase(repository)
    }

    private val repository: PaginatedRepository<Member> by lazy {
        InMemoryMemberRepository()
        //MarvelApiRepository(provideOkHttpClient(loggingInterceptor))
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

class InMemoryMemberRepository: PaginatedRepository<Member> {

    override fun fetch(pagination: Pagination): Flowable<PaginatedResult<Member>> {
        val members = mutableListOf<Member>()
        for (index in 1..40) {
            val random = Random().nextInt(2)
            members.add(Member("Test $index", images[random]))
        }
        return Flowable.just(PaginatedResult.Success(members, PageDetail(false, 50)))
    }
}

val images = listOf(
        "http://images6.fanpop.com/image/photos/34400000/spongebob-spongebob-squarepants-34425372-2000-1873.jpg",
        "https://www.famousbirthdays.com/faces/carrey-jim-image.jpg",
        "https://www.writeups.org/wp-content/uploads/Rick-Sanchez-Rick-and-Morty.jpg"
)


