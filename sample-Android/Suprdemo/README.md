# Heroes

![Phone Screenshot](https://github.com/impraise/SUPR/blob/master/assets/game.gif)

"Heroes" is an implementation of a game using ***SUPR*** architecture. It serves as a guideline on how to use ***SUPR*** components. The content is provided by **The Marvel Comics API**.

Before you run the app, make sure you get a valid API Key following the steps at [developer marvel]( https://developer.marvel.com/documentation/getting_started).

Update [custom_api.properties](https://github.com/impraise/SUPR/blob/master/sample-Android/Suprdemo/heroes/custom_api.properties) file with your keys, go to [SceneFactory](SUPR/sample-Android/Suprdemo/heroes/src/main/java/com/impraise/suprdemo/scenes/di/SceneFactory.kt) and remove the comment to inject the proper `MarvelApiRepository`. 

### Presentation

Scenes are created inside Activities or Fragments. A scene can be seen as a screen and is composed of presenters and use cases. `SceneBase` extends `android.arch.lifecycle.ViewModel` and therefore is created as Android's ViewModel component:

```kotlin
ViewModelProviders.of(activity, SceneFactory(user))
		.get(GameScene::class.java)
```

A `Scene` handles Interactions and delegates calls to the correct components. Also, it coordinates view states. For instance, when it receives an OnLoad interaction, it posts a ViewModelState.Loading state to the presenter so that view is notified and shows loading state:

```kotlin
override fun onInteraction(interaction: GameSceneInteraction) {
        when (interaction) {

            is GameSceneInteraction.OnLoad -> {
                ...
            }
```

### Domain
`UseCase` is the base class for use cases. When creating an use case you need to inform the type <Object> of the result object. This is the type that will be return in the callback:

```kotlin
abstract class UseCase<Object>: DisposableUseCase {

    protected val subscriptions = CompositeDisposable()

    override fun dispose() {
        if (!subscriptions.isDisposed) {
            subscriptions.dispose()
            subscriptions.clear()
        }
    }
}
```

The result data of an use case must always be wrapped in a `Result` object (You can also use `ResultList` if the result is a collection). 

There are a few classes that help you when implementing use cases:

- `SimpleUseCase` executes a business logic and return data using the callback.
- `ParameterizedUseCase` receives a parameter in order to execute its business logic. Returns data using the callback.
- `ReactiveUseCase` returns data as `io.reactivex.Single`

```kotlin
class LoadRandomPageOfMembersUseCase(...): ReactiveUseCase<Unit, ResultList<List<Member>>> {

    override fun get(param: Unit): Single<ResultList<List<Member>>> {
        return repository
                .flatMap { result ->
                    when (result) {
                        is PaginatedResult.Success -> ResultList.Success(it)
                        is PaginatedResult.Error -> Flowable.fromIterable(emptyList())
                    }
                }
    }
}
```

When subscribing to an Observable addTo(subscriptions) function must to be called so that we can use UseCase.dispose() and unsubscribe to any subscription if necessary.

### Data
The result data of a repository must always be wrapped in a DataResult object (You can also use DataResultList if the result is a collection). 
