//
//  Presenter.swift
//  Supr-demo
//
//  Created by Arnaud Camus on 2/20/18.
//  Copyright © 2018 Impraise. All rights reserved.
//

protocol Presenter {
    associatedtype Domain
    associatedtype Object

    //TODO ver viewModelSteam: Rx...
    var viewModelSteam: Object? { get set }

    func present(_ state: ViewModelEntityState, data: Domain)
}
