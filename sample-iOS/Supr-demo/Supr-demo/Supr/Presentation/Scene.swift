//
//  Scene.swift
//  Supr-demo
//
//  Created by Arnaud Camus on 2/22/18.
//  Copyright © 2018 Impraise. All rights reserved.
//

protocol Scene {
    associatedtype InteractionType
    typealias DisposableUseCase = UseCase & Disposable
    var useCases: [DisposableUseCase] { get set }
    func onInteraction(interaction: InteractionType)
}

extension Scene {
    func onCleared() {
        self.useCases.forEach { (disposable) in
            disposable.dispose()
        }
    }
}

protocol Disposable {
    func dispose()
}
