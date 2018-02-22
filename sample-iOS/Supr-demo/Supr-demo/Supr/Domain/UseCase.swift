//
//  UseCase.swift
//  Supr-demo
//
//  Created by Arnaud Camus on 2/22/18.
//  Copyright © 2018 Impraise. All rights reserved.
//

import RxSwift

protocol UseCase {
    var subscriptions: CompositeDisposable { get set }
}
