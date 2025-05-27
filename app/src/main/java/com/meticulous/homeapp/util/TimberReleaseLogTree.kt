package com.meticulous.homeapp.util

import android.util.Log
import timber.log.Timber

class TimberReleaseLogTree : Timber.Tree() {

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        // Configure the logs for the production app here
        // Make use of priority to determine what to log
        // Integrate with external tools like Crashlytics for remote insight

        // Example to log only Warnings and Errors for release build
        if (priority == Log.WARN || priority == Log.ERROR) {
            // Log it to the external tools here
            // Crashlytics.logException(t)
        }

    }
}