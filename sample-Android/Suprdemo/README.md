# SUPR - Android

### Presentation

Scenes are created inside Activities or Fragments. A scene can be seen as a screen and is composed of presenters and use cases. `SceneBase` extends `android.arch.lifecycle.ViewModel` and therefore is created as Android's ViewModel component:

```kotlin
ViewModelProviders.of(activity, SceneFactory(user))
		.get(PrivateNoteListScene::class.java)
```

A `Scene` handles Interactions and delegates calls to the correct components. Also, it coordinates view states. For instance, when it receives an OnLoad interaction, it posts a ViewModelState.Loading state to the presenter so that view is notified and shows loading state:

```kotlin
override fun onInteraction(interaction: PrivateNoteListInteraction) {    
		when(interaction) {        
			is OnLoad -> {            
				presenter.present(ViewModelEntityState.Loading, DomainResultList.success(emptyList()))            
				loadPrivateNotesUseCase.doYourJob(notesAboutUser.id)        
		}
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
class LoadPrivateNoteListUseCase(...): ParameterizedUseCase<String, ResultList<PrivateNote>>() {    
	override fun get(param: String) {        
			getPrivateNotes(param)              
			.subscribe({ domainResult ->                    
					complete(domainResult)                
			}, { error ->                    
					complete(DomainResultList.error(error, emptyList()))                
			}).addTo(subscriptions)    
	} 
   
	private fun complete(domainResultList: DomainResultList<PrivateNote>) {
	   callback?.let { it(domainResultList) } }
	}
}
```

When subscribing to an Observable addTo(subscriptions) function must to be called so that we can use UseCase.dispose() and unsubscribe to any subscription if necessary.

### Data
The result data of a repository must always be wrapped in a DataResult object (You can also use DataResultList if the result is a collection). 
