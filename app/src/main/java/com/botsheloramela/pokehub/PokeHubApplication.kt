package com.botsheloramela.pokehub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokeHubApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}