package br.com.mrocigno.horizonlivemap

import android.app.Application
import br.com.mrocigno.horizonlivemap.core.helpers.KoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HorizonApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@HorizonApplication)
            modules(KoinModules.modules)
        }
    }
}