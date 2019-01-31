//
//  SimpleUseCase.swift
//  Supr-demo
//
//  Created by Arnaud Camus on 2/22/18.
//  Copyright © 2018 Impraise. All rights reserved.
//

enum Result<T> {
    case success(T)
    case failure(Error)
}

protocol ParamUseCase: UseCase {
    associatedtype Param
    associatedtype Response
    typealias ParamUseCaseCallback = (Result<Response>) -> Void
    var callback: ParamUseCaseCallback? { get set }
    func doYourJob(param: Param)
}
