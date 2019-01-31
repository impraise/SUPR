//
//  ViewModelEntity.swift
//  Supr-demo
//
//  Created by Arnaud Camus on 2/20/18.
//  Copyright © 2018 Impraise. All rights reserved.
//

class ViewModelEntity<T, E> {
    let state: ViewModelEntityState
    let data: T
    let error: E

    public init(_ state: ViewModelEntityState, data: T, error: E) {
        self.state = state
        self.data = data
        self.error = error
    }
}
