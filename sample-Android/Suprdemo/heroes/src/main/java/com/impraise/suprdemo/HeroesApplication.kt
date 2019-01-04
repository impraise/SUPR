package com.impraise.suprdemo

import android.app.Application

class HeroesApplication : Application() {

    companion object {
        lateinit var instance: HeroesApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
