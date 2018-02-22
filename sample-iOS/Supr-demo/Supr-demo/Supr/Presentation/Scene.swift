//
//  Scene.swift
//  Supr-demo
//
//  Created by Arnaud Camus on 2/22/18.
//  Copyright © 2018 Impraise. All rights reserved.
//

protocol Scene {
    var useCases: [DisposableUseCase] { get set}
    
    func onInteraction(interaction: Interaction)
}

extension Scene {
    func onCleared() {
        self.useCases.forEach {
            $0.dispose()
        }
    }
}
