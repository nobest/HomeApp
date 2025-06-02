package com.meticulous.homeapp.util

import timber.log.Timber

class AnalyticLoggerImpl: AnalyticLogger {
    override fun logDebug(message: String) {
        Timber.i(message)
    }

    override fun logInfo(message: String) {
        Timber.i(message)
    }

    override fun logError(
        message: String,
        exception: Exception?
    ) {
        Timber.e(exception, message)
    }
}