package com.prakash.speaksmart

import android.app.Application
import com.prakash.speaksmart.data.di.dataModule
import com.prakash.speaksmart.ui.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SpeakSmartApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SpeakSmartApplication)
            modules(presentationModule, dataModule)
        }
    }
}