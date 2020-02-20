package ru.cocovella.mortalenemies.view

import android.app.Application
import org.koin.android.ext.android.startKoin
import ru.cocovella.mortalenemies.di.appModule
import ru.cocovella.mortalenemies.di.listModule
import ru.cocovella.mortalenemies.di.noteModule
import ru.cocovella.mortalenemies.di.splashModule

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule, splashModule, listModule, noteModule))
    }
}
