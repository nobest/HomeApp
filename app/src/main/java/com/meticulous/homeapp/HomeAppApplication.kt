package com.meticulous.homeapp

import android.app.Application
import com.meticulous.homeapp.util.PreferencesHelper
import com.meticulous.homeapp.util.PreferencesHelperImpl
import com.meticulous.homeapp.util.TimberReleaseLogTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant

@HiltAndroidApp
class HomeAppApplication : Application() {
    lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate() {
        super.onCreate()
        preferencesHelper = PreferencesHelperImpl(applicationContext)

        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        } else {
            plant(TimberReleaseLogTree())
        }
    }

}