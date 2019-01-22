<p align="center">
<img src="https://github.com/impraise/SUPR/blob/master/assets/logo_supr.png" width="30%" height="30%" alt="SUPR"/>
</p>

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

**SUPR** is a cross-platform architecture designed to facilitate collaboration between Android & iOS developers with a focus on testability. **SUPR** is based on the [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) proposed by Uncle Bob.

The main goal is to have a clear separation of concerns. There are clear boundaries between the project’s layers. These layers are isolated from each other which makes them easy to test and maintain.

The architecture has 3 main layers: Presentation, Domain, and Data. Each one of them has a clear responsibility and works separately from the others.

<p align="center">
<img src="https://github.com/impraise/SUPR/blob/master/assets/layers.png" width="60%" height="60%" alt="Flow of interaction and type of result from each layer"/>
</p>

With a cross-platform approach in mind, the architecture keeps specific implementations for Android and iOS as external layers. 

<p align="center">
<img src="https://github.com/impraise/SUPR/blob/master/assets/core_components.png" width="60%" height="60%" alt="Communication between layers and main components."/>
</p>

## Main Components

### Scene
A **Scene** is the entry point of interactions. It is responsible for orchestrating the Use Cases, triggering the Presenters accordingly and retaining state.

### Use Case
A **Use Case** is the member of our architecture responsible for performing the tasks that need to be done to fulfill our specific business rules. To reuse, replace and write tests to it, a Use Case defines its input and output ports, and it's written without the dependency of any platform related capabilities.

### Presenter
A **Presenter** formats data in a way that is close to the idea of how it should be displayed, but without knowing the concrete use of it.

### Repository
A **Repository** is responsible for managing data and making it transparent to external layers independently of the strategy used to operate. The repository decides what the best way to provide the result is and do it using an explicit interface.

## Platform implementation
It makes use of the Android / iOS Frameworks. Here all UI components are created and views are rendered. Here, scenes are created and views can react to models emitted by `Presenter`s.

## Presentation
This layer’s responsibility is to handle interactions from “outside”, most of the time UI interactions, and to communicate with Domain layer in order to retrieve and format data that will be shown to the user. It uses `UseCase` classes to retrieve data and give it to `Presenter`s where data will be formatted and emitted so that the view can consume it in a proper format.

<p align="center">
<img src="https://github.com/impraise/SUPR/blob/master/assets/presentation_flow.png" width="60%" height="60%" alt="Presentation Flow"/>
</p>

## Domain
All the business logic is here. This is pure Kotlin / Swift without any Android / iOS dependencies.

## Data
All data needed for the application comes from this layer through a Repository implementation. It makes it transparent to other layers where the data comes from. It can be retrieved from cache or API.
