package com.example.currency

import android.app.Application
import com.example.currency.di.coreModule
import com.example.currency.di.repositoryModule
import com.example.currency.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApplication)
            modules(coreModule, repositoryModule, viewModelModule)
        }
    }
}