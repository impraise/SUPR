//
//  UseCase.swift
//  Supr-demo
//
//  Created by Arnaud Camus on 2/22/18.
//  Copyright © 2018 Impraise. All rights reserved.
//

import RxSwift

class UseCase<Object> : DispoableUseCase {
    
    let subscriptions = CompositeDisposable()
    
    var callback: ((_ result: Object) -> Void)?
    
    func dispose() {
        if !subscriptions.isDisposed {
            subscriptions.dispose()
        }
    }
    
}
