package com.impraise.suprdemo

import android.app.Application
import android.content.Context

class GameApplication : Application() {

    companion object {
        lateinit var instance: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}