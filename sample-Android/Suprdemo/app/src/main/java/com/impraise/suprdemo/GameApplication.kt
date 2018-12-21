package com.impraise.suprdemo

import android.app.Application

class GameApplication : Application() {

    val instance by lazy {
        this
    }
}