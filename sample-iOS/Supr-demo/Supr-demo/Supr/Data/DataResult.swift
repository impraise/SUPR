//
//  DataResult.swift
//  Supr-demo
//
//  Created by Arnaud Camus on 2/22/18.
//  Copyright © 2018 Impraise. All rights reserved.
//

class DataResult<T> {
    let state: DataResultState
    let data: T?
    let error: Error?
    
    init(_ state: DataResultState, data: T? = nil, error: Error? = nil) {
        self.state = state
        self.data = data
        self.error = error
    }
    
    static func success(data: T) -> DataResult<T> {
        return DataResult(DataResultState.success, data: data)
    }
    
    static func error(error: Error, data: T?) -> DataResult<T> {
        return DataResult(DataResultState.error, data: data, error: error)
    }
}
