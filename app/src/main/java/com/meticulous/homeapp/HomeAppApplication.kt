package com.meticulous.homeapp

import android.app.Application
import com.meticulous.homeapp.util.TimberReleaseLogTree
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant


class HomeAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        } else {
            plant(TimberReleaseLogTree())
        }
    }

}