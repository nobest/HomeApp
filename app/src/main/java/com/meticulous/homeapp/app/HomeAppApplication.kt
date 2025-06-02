package com.meticulous.homeapp.app

import android.app.Application
import com.meticulous.homeapp.BuildConfig
import com.meticulous.homeapp.util.PreferencesHelper
import com.meticulous.homeapp.util.PreferencesHelperImpl
import com.meticulous.homeapp.util.TimberReleaseLogTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class HomeAppApplication : Application() {
    lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate() {
        super.onCreate()
        preferencesHelper = PreferencesHelperImpl(applicationContext)

        if (BuildConfig.DEBUG) {
            Timber.Forest.plant(Timber.DebugTree())
        } else {
            Timber.Forest.plant(TimberReleaseLogTree())
        }
    }

}