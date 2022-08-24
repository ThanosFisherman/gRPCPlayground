package com.thanosfisherman.grpcplayground

import android.app.Application
import com.thanosfisherman.grpcplayground.data.di.dataModule
import com.thanosfisherman.grpcplayground.domain.di.domainModule
import com.thanosfisherman.grpcplayground.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class GrpcApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@GrpcApplication)
            if (BuildConfig.DEBUG) androidLogger(Level.DEBUG)
            modules(presentationModule + domainModule + dataModule)
        }
    }
}