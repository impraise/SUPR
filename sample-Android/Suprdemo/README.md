# Heroes

![Phone Screenshot](https://github.com/impraise/SUPR/blob/master/assets/game.gif)

"Heroes" is an implementation of a game using ***SUPR*** architecture. It serves as a guideline on how to use ***SUPR*** components. The content is provided by **The Marvel Comics API**.

Before you run the app, make sure you get a valid API Key following the steps at [developer marvel]( https://developer.marvel.com/documentation/getting_started).

Update [custom_api.properties](https://github.com/impraise/SUPR/blob/master/sample-Android/Suprdemo/heroes/custom_api.properties) file with your keys, go to [SceneFactory](https://github.com/impraise/SUPR/blob/master/sample-Android/Suprdemo/heroes/src/main/java/com/impraise/suprdemo/scenes/di/SceneFactory.kt) and remove the comment to inject the proper `MarvelApiRepository`. 

### Presentation

Scenes are created inside Activities or Fragments. A scene can be seen as a screen and is composed of presenters and use cases. `SceneBase` extends `android.arch.lifecycle.ViewModel` and therefore is created as Android's ViewModel component:

```kotlin
ViewModelProviders.of(activity, SceneFactory(user))
		.get(GameScene::class.java)
```

A `Scene` handles Interactions and delegates calls to the correct components. Also, it coordinates view states. For instance, when it receives an OnLoad interaction, it calls the `loading()` function on `Presenter` so that the view is notified and shows a loading state:

```kotlin
override fun onInteraction(interaction: GameSceneInteraction) {
        when (interaction) {
            is GameSceneInteraction.OnLoad -> gamePresenter.loading()
            ...
        }
```

When subscribing to an `UseCase`, `addTo(subscriptions)` function must be called so that `Scene` can unsubscribe to any subscription if necessary:

```kotlin
	createGameUseCase
                .get()
                .doOnSubscribe {
                    gamePresenter.loading()
                }
                .subscribe(...)
		.addTo(subscriptions)
```

### Domain
`SimpleReactiveUseCase` is the base interface for use cases. When creating an use case you need to inform the `<ResponseType>` for the result object. `ResponseType` is the type of the object that is emitted by the `Single` Reactive-Stream: 

```kotlin
interface SimpleReactiveUseCase<in ParamType, ResponseType> {

    fun get(): Single<ResponseType>
}
```

You can also use `ReactiveUseCase` if you need to provide a parameter to your use case (which will be the case most of the time).

```kotlin
interface ReactiveUseCase<in ParamType, ResponseType> {

    fun get(param: ParamType): Single<ResponseType>
}
```

The result data of an use case must always be wrapped in a `Result` object (You can also use `ResultList` if the result is a collection).

```kotlin
class LoadRandomPageOfMembersUseCase(...): SimpleReactiveUseCase<ResultList<List<Member>>> {

    override fun get(): Single<ResultList<List<Member>>> {
        return repository
                .flatMap { result ->
                    when (result) {
                        is PaginatedResult.Success -> ResultList.Success(it)
                        is PaginatedResult.Error -> Result.Error(result.error, GameState.EMPTY_GAME)
                    }
                }
    }
}
```

### Data
The result data of a repository must always be wrapped in a DataResult object (You can also use DataResultList if the result is a collection). 
